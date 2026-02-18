package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.CarRepository;
import bavteqdoit.carhealthcheck.data.PaintCheckRepository;
import bavteqdoit.carhealthcheck.data.QuestionAnswerRepository;
import bavteqdoit.carhealthcheck.data.VinReportDataRepository;
import bavteqdoit.carhealthcheck.dto.InspectionSummaryDto;
import bavteqdoit.carhealthcheck.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class InspectionSummaryService {

    private final VinReportDataRepository vinReportDataRepository;
    private final PaintCheckRepository paintCheckRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final MessageSource messageSource;
    private final CarRepository carRepository;
    private final BaselineScoreResolver baselineScoreResolver;
    private final RiskScoringService riskScoringService;


    public InspectionSummaryDto buildSummary(Long carId, Locale locale) {
        var dto = new InspectionSummaryDto();

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

        var baseline = baselineScoreResolver.resolve(car);
        dto.setBasicScoreUsed(baseline.scoreUsed());


        if (baseline.source() == BaselineScoreResolver.BaselineResult.Source.DEFAULT) {
            dto.yellow(msg(locale, "summary.basicScore.missing"));
        }

        var vin = vinReportDataRepository.findByCarId(carId).orElse(null);
        var paint = paintCheckRepository.findByCarId(carId).orElse(null);
        var answers = questionAnswerRepository.findAllByCarIdWithDetails(carId);
        var risk = riskScoringService.compute(car, vin, paint, answers);

        applyVin(dto, vin, locale);
        applyPaint(dto, paint, locale);
        applyQuestions(dto, answers, locale);

        dto.setFinalScore(risk.getFinalScore());
        dto.setRiskLevel(msg(locale, "risk.level." + risk.getRiskLevel().name()));
        dto.setRiskScore(risk.getRiskScore());
        dto.setRiskReasons(risk.getRiskReasons());

        if (dto.getRedFlags().isEmpty() && dto.getYellowFlags().isEmpty()) {
            dto.green(msg(locale, "summary.ok.no_issues"));
        }

        return dto;
    }

    private void applyVin(InspectionSummaryDto dto, VinReportData vin, Locale locale) {
        if (vin == null) {
            dto.yellow(msg(locale, "summary.vin.missing"));
            return;
        }

        if (vin.getOcStatus() != null) {
            switch (vin.getOcStatus()) {
                case INVALID -> dto.red(msg(locale, "summary.vin.oc.invalid"));
                case NO_DATA -> dto.yellow(msg(locale, "summary.vin.oc.no_data"));
                default -> dto.green(msg(locale, "summary.vin.oc.valid"));
            }
        }

        if (vin.getTechnicalInspectionStatus() != null) {
            switch (vin.getTechnicalInspectionStatus()) {
                case INVALID -> dto.red(msg(locale, "summary.vin.ti.invalid"));
                case NO_DATA -> dto.yellow(msg(locale, "summary.vin.ti.no_data"));
                default -> dto.green(msg(locale, "summary.vin.ti.valid"));
            }
        }

        if (vin.getRegistrationStatus() != null) {
            switch (vin.getRegistrationStatus()) {
                case NOT_REGISTERED -> dto.red(msg(locale, "summary.vin.reg.not_registered"));
                case NO_DATA -> dto.yellow(msg(locale, "summary.vin.reg.no_data"));
                default -> dto.green(msg(locale, "summary.vin.reg.registered"));
            }
        }

        if (isFounded(vin.getTheft())) dto.red(msg(locale, "summary.foreign.theft"));
        if (isFounded(vin.getTotalLoss())) dto.red(msg(locale, "summary.foreign.total_loss"));
        if (isFounded(vin.getOdometerMismatch())) dto.red(msg(locale, "summary.foreign.odometer_mismatch"));
        if (isFounded(vin.getScrapped())) dto.red(msg(locale, "summary.foreign.scrapped"));
        if (isFounded(vin.getNotRoadworthy())) dto.red(msg(locale, "summary.foreign.not_roadworthy"));

        if (isFounded(vin.getVinChecksumError())) dto.yellow(msg(locale, "summary.foreign.vin_checksum_error"));
        if (isFounded(vin.getServiceActions())) dto.yellow(msg(locale, "summary.foreign.service_actions"));
        if (isFounded(vin.getTaxi())) dto.yellow(msg(locale, "summary.foreign.taxi"));
        if (isFounded(vin.getAccident())) dto.yellow(msg(locale, "summary.foreign.accident"));
        if (isFounded(vin.getDamaged())) dto.yellow(msg(locale, "summary.foreign.damaged"));
    }

    private boolean isFounded(CarRisk r) {
        return r != null && r == CarRisk.FOUNDED;
    }

    private void applyPaint(InspectionSummaryDto dto, PaintCheck paint, Locale locale) {
        if (paint == null) {
            dto.yellow(msg(locale, "summary.paint.no.measurement"));
            return;
        }

        if (paint.isNoThicknessMeasurements()) {
            dto.yellow(msg(locale, "summary.paint.no.measurement"));
        }

        paintFlag(dto, locale, "paint.hood", paint.isHoodDifferent(), paint.getMinHoodThickness(), paint.getMaxHoodThickness());
        paintFlag(dto,locale, "paint.roof", paint.isRoofDifferent(), paint.getMinRoofThickness(), paint.getMaxRoofThickness());

        paintFlag(dto,locale, "paint.front.left.door", paint.isFrontLeftDoorDifferent(),
                paint.getMinFrontLeftDoorThickness(), paint.getMaxFrontLeftDoorThickness());
        paintFlag(dto,locale, "paint.rear.left.door", paint.isRearLeftDoorDifferent(),
                paint.getMinRearLeftDoorThickness(), paint.getMaxRearLeftDoorThickness());
        paintFlag(dto,locale, "paint.front.right.door", paint.isFrontRightDoorDifferent(),
                paint.getMinFrontRightDoorThickness(), paint.getMaxFrontRightDoorThickness());
        paintFlag(dto,locale, "paint.rear.right.door", paint.isRearRightDoorDifferent(),
                paint.getMinRearRightDoorThickness(), paint.getMaxRearRightDoorThickness());

        paintFlag(dto,locale, "paint.trunk", paint.isTrunkDifferent(),
                paint.getMinTrunkThickness(), paint.getMaxTrunkThickness());

        paintFlag(dto,locale, "paint.pillar.a.left", paint.isPillarALeftDifferent(),
                paint.getMinPillarALeftThickness(), paint.getMaxPillarALeftThickness());
        paintFlag(dto,locale, "paint.pillar.a.right", paint.isPillarARightDifferent(),
                paint.getMinPillarARightThickness(), paint.getMaxPillarARightThickness());
        paintFlag(dto,locale, "paint.pillar.b.left", paint.isPillarBLeftDifferent(),
                paint.getMinPillarBLeftThickness(), paint.getMaxPillarBLeftThickness());
        paintFlag(dto,locale, "paint.pillar.b.right", paint.isPillarBRightDifferent(),
                paint.getMinPillarBRightThickness(), paint.getMaxPillarBRightThickness());
        paintFlag(dto,locale, "paint.pillar.c.left", paint.isPillarCLeftDifferent(),
                paint.getMinPillarCLeftThickness(), paint.getMaxPillarCLeftThickness());
        paintFlag(dto,locale, "paint.pillar.c.right", paint.isPillarCRightDifferent(),
                paint.getMinPillarCRightThickness(), paint.getMaxPillarCRightThickness());

        paintFlag(dto,locale, "paint.front.left.arch", paint.isFrontLeftWheelArchDifferent(),
                paint.getMinFrontLeftWheelArchThickness(), paint.getMaxFrontLeftWheelArchThickness());
        paintFlag(dto,locale, "paint.front.right.arch", paint.isFrontRightWheelArchDifferent(),
                paint.getMinFrontRightWheelArchThickness(), paint.getMaxFrontRightWheelArchThickness());
        paintFlag(dto,locale, "paint.rear.left.arch", paint.isRearLeftWheelArchDifferent(),
                paint.getMinRearLeftWheelArchThickness(), paint.getMaxRearLeftWheelArchThickness());
        paintFlag(dto,locale, "paint.rear.right.arch", paint.isRearRightWheelArchDifferent(),
                paint.getMinRearRightWheelArchThickness(), paint.getMaxRearRightWheelArchThickness());

        paintFlag(dto,locale, "paint.bumper.front", paint.isFrontBumperDifferent(),
                paint.getMinFrontBumperThickness(), paint.getMaxFrontBumperThickness());
        paintFlag(dto,locale, "paint.bumper.rear", paint.isRearBumperDifferent(),
                paint.getMinRearBumperThickness(), paint.getMaxRearBumperThickness());

        if (paint.getDamages() != null && !paint.getDamages().isEmpty()) {

            for (PaintDamage d : paint.getDamages()) {
                String part = safeKey(locale, "paint.", normalizeDamageKey(d.getBodyPart()));
                String type = safeKey(locale, "paint.label.damage.", normalizeDamageKey(d.getDamageType()));
                String size = safeKey(locale, "paint.size.", normalizeDamageKey(d.getSize()));

                dto.getPaintDamageDetails().add(
                        msg(locale, "summary.paint.damage", part, type, size)
                );
            }

            dto.yellow(msg(locale, "summary.paint.damage.count", dto.getPaintDamageDetails().size()));
        }

        applySideMismatchChecks(dto, paint, locale);
    }

    private String normalizeDamageKey(String raw) {
        if (raw == null) return null;
        return raw.trim().toLowerCase().replace(" ", "_");
    }

    private String safeKey(Locale locale, String prefix, String raw) {
        if (raw == null || raw.isBlank()) return msg(locale, "summary.value.missing");
        return msg(locale, prefix + raw);
    }

    private void paintFlag(InspectionSummaryDto dto,
                           Locale locale,
                           String partKey,
                           boolean different,
                           Integer min,
                           Integer max) {

        Integer worst = worstThickness(min, max);
        String part = msg(locale, partKey);

        if (worst != null) {
            if (worst >= 300) {
                dto.red(msg(locale, "summary.paint.filler", part, formatRange(min, max)));
                return;
            }
            if (worst >= 200) {
                dto.yellow(msg(locale, "summary.paint.repaint", part, formatRange(min, max)));
                return;
            }
        }

        if (different) {
            dto.yellow(msg(locale, "summary.paint.part_different", part));
        }
    }

    private boolean isSideMismatch(Integer leftMin, Integer leftMax,
                                   Integer rightMin, Integer rightMax) {

        Integer left = worstThickness(leftMin, leftMax);
        Integer right = worstThickness(rightMin, rightMax);

        if (left == null || right == null) return false;

        int diff = Math.abs(left - right);

        if (diff < 50) return false;

        return left < 200 && right < 200;
    }

    private void applySideMismatchChecks(InspectionSummaryDto dto, PaintCheck paint, Locale locale) {
        List<SidePair> pairs = List.of(
                new SidePair("summary.part.front.doors",
                        PaintCheck::getMinFrontLeftDoorThickness, PaintCheck::getMaxFrontLeftDoorThickness,
                        PaintCheck::getMinFrontRightDoorThickness, PaintCheck::getMaxFrontRightDoorThickness),

                new SidePair("summary.part.rear.doors",
                        PaintCheck::getMinRearLeftDoorThickness, PaintCheck::getMaxRearLeftDoorThickness,
                        PaintCheck::getMinRearRightDoorThickness, PaintCheck::getMaxRearRightDoorThickness),

                new SidePair("summary.part.front.wheel_arch",
                        PaintCheck::getMinFrontLeftWheelArchThickness, PaintCheck::getMaxFrontLeftWheelArchThickness,
                        PaintCheck::getMinFrontRightWheelArchThickness, PaintCheck::getMaxFrontRightWheelArchThickness),

                new SidePair("summary.part.rear.wheel_arch",
                        PaintCheck::getMinRearLeftWheelArchThickness, PaintCheck::getMaxRearLeftWheelArchThickness,
                        PaintCheck::getMinRearRightWheelArchThickness, PaintCheck::getMaxRearRightWheelArchThickness),

                new SidePair("Słupek A",
                        PaintCheck::getMinPillarALeftThickness, PaintCheck::getMaxPillarALeftThickness,
                        PaintCheck::getMinPillarARightThickness, PaintCheck::getMaxPillarARightThickness),

                new SidePair("Słupek B",
                        PaintCheck::getMinPillarBLeftThickness, PaintCheck::getMaxPillarBLeftThickness,
                        PaintCheck::getMinPillarBRightThickness, PaintCheck::getMaxPillarBRightThickness),

                new SidePair("Słupek C",
                        PaintCheck::getMinPillarCLeftThickness, PaintCheck::getMaxPillarCLeftThickness,
                        PaintCheck::getMinPillarCRightThickness, PaintCheck::getMaxPillarCRightThickness)
        );

        for (SidePair p : pairs) {
            Integer lMin = p.leftMin().apply(paint);
            Integer lMax = p.leftMax().apply(paint);
            Integer rMin = p.rightMin().apply(paint);
            Integer rMax = p.rightMax().apply(paint);

            if (isSideMismatch(lMin, lMax, rMin, rMax)) {
                int left = worstThickness(lMin, lMax);
                int right = worstThickness(rMin, rMax);
                int diff = Math.abs(left - right);

                String partName = msg(locale, p.label());

                dto.yellow(
                        msg(locale,
                                "summary.paint.side_mismatch",
                                partName,
                                diff,
                                formatRange(lMin, lMax),
                                formatRange(rMin, rMax)
                        )
                );
            }
        }
    }

    private Integer worstThickness(Integer min, Integer max) {
        if (min == null && max == null) return null;
        return (max != null) ? max : min;
    }

    private String formatRange(Integer min, Integer max) {
        if (min == null && max == null) return "brak pomiaru";
        if (min != null && max != null) return min + "–" + max;
        if (min != null) return "min " + min;
        return "max " + max;
    }

    private void applyQuestions(InspectionSummaryDto dto, List<QuestionAnswer> answers, Locale locale) {
        if (answers == null || answers.isEmpty()) {
            dto.yellow("Brak wypełnionej checklisty pytań.");
            return;
        }

        for (var a : answers) {
            var q = a.getQuestion();
            if (q == null) continue;

            String key = q.getQuestionKey();
            String opt = a.getSelectedOption() != null ? a.getSelectedOption().getValue() : null;

            if (opt == null) {
                dto.yellow(msg(locale, "summary.checklist.no_answer", msg(locale, "question." + key)));
                continue;
            }

            String sentenceKey = "answer." + key + "." + opt;
            String sentence = msg(locale, sentenceKey);

            RiskBand band = RiskBand.GREEN;
            if (a.getSelectedOption() != null && a.getSelectedOption().getRiskBand() != null) {
                band = a.getSelectedOption().getRiskBand();
            }

            switch (band) {
                case GREEN -> dto.green(sentence);
                case YELLOW -> dto.yellow(sentence);
                case RED -> dto.red(sentence);
            }
        }
    }

    private String msg(Locale locale, String key, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }
}

