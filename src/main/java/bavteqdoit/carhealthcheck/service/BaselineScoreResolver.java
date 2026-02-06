package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.ModelVariantRepository;
import bavteqdoit.carhealthcheck.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaselineScoreResolver {

    public static final int DEFAULT_BASIC_SCORE = 60;

    private static final int K = 10;                 //
    private static final double NEG_MULT = 3.0;      // neg multiplayer
    private static final double NEG_BOOST = 0.10;    // boost for neg review
    private static final int MAX_POS_ADJ = 6;        // max +pkt
    private static final int MAX_NEG_ADJ = 12;       // max -pkt

    private final ModelVariantRepository modelVariantRepository;

    public record BaselineResult(int scoreUsed, Source source) {
        public enum Source { VARIANT, MODEL_TYPE, DEFAULT }
    }

    public BaselineResult resolve(Car car) {
        if (car == null || car.getModelType() == null) {
            return new BaselineResult(DEFAULT_BASIC_SCORE, BaselineResult.Source.DEFAULT);
        }

        if (car.getEngineType() != null && car.getBodyType() != null) {
            var list = modelVariantRepository.findBestCandidates(
                    car.getModelType().getId(),
                    car.getEngineType().getId(),
                    car.getBodyType().getId(),
                    car.getProductionYear()
            );

            if (!list.isEmpty()) {
                var mv = list.get(0);
                Integer mvBasic = mv.getBasicScore();

                if (mvBasic != null) {
                    int baseline = clamp(mvBasic);

                    int adjusted = adjustBaselineBySource(baseline, mv.getSourceRatingsCount(), mv.getSourceAvgRating());

                    return new BaselineResult(adjusted, BaselineResult.Source.VARIANT);
                }
            }
        }

        Integer modelBasic = car.getModelType().getBasicScore();
        if (modelBasic != null) {
            return new BaselineResult(clamp(modelBasic), BaselineResult.Source.MODEL_TYPE);
        }

        return new BaselineResult(DEFAULT_BASIC_SCORE, BaselineResult.Source.DEFAULT);
    }

    private int adjustBaselineBySource(int baseline, Integer ratingsCount, Double avgRating) {
        if (ratingsCount == null || avgRating == null) return baseline;
        if (ratingsCount <= 0) return baseline;
        if (avgRating.isNaN()) return baseline;

        // Rating from 1-5 -> 20-100
        int variantScore = (int) Math.round(avgRating * 20.0);

        // sanity clamp
        variantScore = clamp(variantScore);

        int delta = variantScore - baseline;

        // base weight from the number of ratings
        double w = (double) ratingsCount / (ratingsCount + (double) K);

        // asymmetrical judgment (bad rating more impact)
        if (delta < 0) {
            w = Math.min(1.0, w * NEG_MULT + NEG_BOOST);
        }

        int blended = (int) Math.round(baseline * (1.0 - w) + variantScore * w);

        // limits of correction
        int minAllowed = baseline - MAX_NEG_ADJ;
        int maxAllowed = baseline + MAX_POS_ADJ;

        blended = clamp(blended, minAllowed, maxAllowed);

        // final clamp 0..100
        return clamp(blended);
    }

    private int clamp(int v) { return Math.max(0, Math.min(100, v)); }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
