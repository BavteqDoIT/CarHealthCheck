package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.ModelVariantRepository;
import bavteqdoit.carhealthcheck.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaselineScoreResolver {

    public static final int DEFAULT_BASIC_SCORE = 60;

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
                if (mv.getBasicScore() != null) {
                    return new BaselineResult(clamp(mv.getBasicScore()), BaselineResult.Source.VARIANT);
                }
            }
        }

        Integer modelBasic = car.getModelType().getBasicScore();
        if (modelBasic != null) {
            return new BaselineResult(clamp(modelBasic), BaselineResult.Source.MODEL_TYPE);
        }

        return new BaselineResult(DEFAULT_BASIC_SCORE, BaselineResult.Source.DEFAULT);
    }

    private int clamp(int v) { return Math.max(0, Math.min(100, v)); }
}
