package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RiskScoringService {

    private final MessageSource messageSource;
    private final BaselineScoreResolver baselineScoreResolver;

    public RiskResult compute(
            Car car,
            VinReportData vin,
            PaintCheck paint,
            List<QuestionAnswer> answers,
            Locale locale
    ) {
        BaselineScoreResolver.BaselineResult baselineResult = baselineScoreResolver.resolve(car);

        int baseline = baselineResult.scoreUsed();
        List<Penalty> penalties = new ArrayList<>();

        applyVinPenalties(vin, penalties);
        applyPaintPenalties(paint, penalties);
        applyChecklistPenalties(answers, penalties);

        int totalPenalty = penalties.stream()
                .mapToInt(Penalty::points)
                .sum();

        int finalScore = clamp(baseline - totalPenalty, 0, 100);
        RiskLevel level = classifyLevel(finalScore);

        List<String> reasons = penalties.stream()
                .sorted(Comparator.comparingInt(Penalty::points).reversed())
                .limit(6)
                .map(p -> messageSource.getMessage(p.reasonKey(), null, locale))
                .toList();

        RiskResult result = new RiskResult();
        result.setFinalScore(finalScore);
        result.setRiskLevel(level);
        result.setRiskReasons(reasons);
        return result;
    }


    private RiskLevel classifyLevel(int score) {
        if (score >= 75) return RiskLevel.LOW;
        if (score >= 45) return RiskLevel.MEDIUM;
        return RiskLevel.HIGH;
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    private void applyVinPenalties(VinReportData vin, List<Penalty> out) {
        if (vin == null) {
            out.add(new Penalty(10, "risk.vin.no_data"));
            return;
        }

        if (isFounded(vin.getTheft()))
            out.add(new Penalty(60, "risk.vin.theft"));

        if (isFounded(vin.getTotalLoss()))
            out.add(new Penalty(55, "risk.vin.total_loss"));

        if (isFounded(vin.getOdometerMismatch()))
            out.add(new Penalty(45, "risk.vin.odometer_mismatch"));

        if (isFounded(vin.getScrapped()))
            out.add(new Penalty(55, "risk.vin.scrapped"));

        if (isFounded(vin.getNotRoadworthy()))
            out.add(new Penalty(50, "risk.vin.not_roadworthy"));
    }

    private void applyPaintPenalties(PaintCheck paint, List<Penalty> out) {
        if (paint == null) {
            out.add(new Penalty(5, "risk.paint.no_data"));
            return;
        }

        if (paint.isNoThicknessMeasurements()) {
            out.add(new Penalty(5, "risk.paint.no_measurements"));
        }

        paint.forEachThickness(worst -> {
            if (worst >= 300)
                out.add(new Penalty(25, "risk.paint.filler"));
            else if (worst >= 200)
                out.add(new Penalty(14, "risk.paint.repaint"));
        });

        long diffCount = paint.countDifferentParts();
        if (diffCount >= 3)
            out.add(new Penalty(10, "risk.paint.many_parts_different"));
    }

    private void applyChecklistPenalties(List<QuestionAnswer> answers, List<Penalty> out) {
        if (answers == null) return;

        for (QuestionAnswer a : answers) {
            if (a.getSelectedOption() == null) continue;

            RiskBand band = a.getSelectedOption().getRiskBand();
            if (band == RiskBand.GREEN) continue;

            int base = switch (band) {
                case YELLOW -> 2;
                case RED -> 6;
                default -> 0;
            };

            int weight = questionWeight(a.getQuestion().getQuestionKey());
            int penalty = base * weight;

            if (penalty > 0) {
                out.add(new Penalty(
                        penalty,
                        "risk.checklist." + a.getQuestion().getQuestionKey()
                ));
            }
        }
    }

    private int questionWeight(String key) {
        return switch (key) {
            case "gearbox_operation",
                 "brakes_operation",
                 "vin_readable",
                 "visible_leaks",
                 "exhaust_smoke" -> 3;

            case "suspension_behavior",
                 "steering_response" -> 2;

            default -> 1;
        };
    }

    private boolean isFounded(CarRisk risk) {
        return risk == CarRisk.FOUNDED;
    }

}

