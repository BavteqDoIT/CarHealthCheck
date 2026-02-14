package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskScoringService {

    private final BaselineScoreResolver baselineScoreResolver;

    public RiskResult compute(
            Car car,
            VinReportData vin,
            PaintCheck paint,
            List<QuestionAnswer> answers
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

        RiskResult result = new RiskResult();
        result.setFinalScore(finalScore);
        result.setRiskLevel(level);
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
            out.add(new Penalty(100, "risk.vin.theft"));

        if (isFounded(vin.getTotalLoss()))
            out.add(new Penalty(100, "risk.vin.total_loss"));

        if (isFounded(vin.getOdometerMismatch()))
            out.add(new Penalty(100, "risk.vin.odometer_mismatch"));

        if (isFounded(vin.getScrapped()))
            out.add(new Penalty(100, "risk.vin.scrapped"));

        if (isFounded(vin.getNotRoadworthy()))
            out.add(new Penalty(100, "risk.vin.not_roadworthy"));
    }

    private void applyPaintPenalties(PaintCheck paint, List<Penalty> out) {
        if (paint == null) {
            out.add(new Penalty(5, "risk.paint.no_data"));
            return;
        }
        if (paint.isNoThicknessMeasurements()) {
            out.add(new Penalty(5, "risk.paint.no_measurements"));
            return;
        }
        double med = median(paint.getAllThicknessValues());
        boolean hasMedian = !Double.isNaN(med) && med > 0;

        List<Integer> worsts = paint.getAllMaxThicknessValues();

        boolean fillerAdded = false;
        boolean repaintAdded = false;


        for (Integer worst : worsts) {
            if (worst == null || worst <= 0) continue;
            if (!hasMedian) {
                if (!fillerAdded && worst >= 300) {
                    out.add(new Penalty(25, "risk.paint.filler"));
                    fillerAdded = true;
                } else if (!repaintAdded && worst >= 200) {
                    out.add(new Penalty(14, "risk.paint.repaint"));
                    repaintAdded = true;
                }
                continue;
            }
            if (!fillerAdded && worst >= 300 && worst >= 1.6 * med) {
                out.add(new Penalty(25, "risk.paint.filler"));
                fillerAdded = true;
                repaintAdded = true;
            } else if (!repaintAdded && worst >= 200 && worst >= 1.4 * med) {
                out.add(new Penalty(14, "risk.paint.repaint"));
                repaintAdded = true;
            }
        }

        int diffCount = (int) paint.countDifferentParts();
        if (diffCount < 3){
            out.add(new Penalty(3 * diffCount,"risk.paint.parts_different"));
        } else {
            out.add(new Penalty(10 + (diffCount * 2), "risk.paint.many_parts_different"));
        }
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

    private double median(List<Integer> values) {
        if (values == null) return Double.NaN;

        List<Integer> sorted = values.stream()
                .filter(v -> v != null && v > 0)
                .sorted()
                .toList();

        int n = sorted.size();
        if (n == 0) return Double.NaN;

        if (n % 2 == 1) return sorted.get(n / 2);
        return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
    }

    private int questionWeight(String key) {
        return switch (key) {
            case "vin_readable",        //critical issues
                 "service_docs" -> 999;

            case "gearbox_operation",
                 "brakes_operation",
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

