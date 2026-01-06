package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.PaintCheckRepository;
import bavteqdoit.carhealthcheck.data.QuestionAnswerRepository;
import bavteqdoit.carhealthcheck.data.VinReportDataRepository;
import bavteqdoit.carhealthcheck.dto.InspectionSummaryDto;
import bavteqdoit.carhealthcheck.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionSummaryService {

    private final VinReportDataRepository vinReportDataRepository;
    private final PaintCheckRepository paintCheckRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    public InspectionSummaryDto buildSummary(Long carId) {
        var dto = new InspectionSummaryDto();

        var vin = vinReportDataRepository.findByCarId(carId).orElse(null);
        var paint = paintCheckRepository.findByCarId(carId).orElse(null);
        var answers = questionAnswerRepository.findAllByCarIdWithDetails(carId);

        applyVin(dto, vin);
        applyPaint(dto, paint);
        applyQuestions(dto, answers);

        // Opcjonalnie: jeśli nic nie wyszło, daj notkę
        if (dto.getRedFlags().isEmpty() && dto.getYellowFlags().isEmpty()) {
            dto.green("Brak wykrytych problemów w dostępnych danych.");
        }

        return dto;
    }

    // ===== VIN =====
    private void applyVin(InspectionSummaryDto dto, VinReportData vin) {
        if (vin == null) {
            dto.yellow("Brak wgranego raportu VIN (brak danych z CEPIK).");
            return;
        }

        // statusy
        if (vin.getOcStatus() != null) {
            switch (vin.getOcStatus()) {
                case INVALID -> dto.red("OC: nieaktualne.");
                case NO_DATA -> dto.yellow("OC: brak danych.");
                default -> dto.green("OC: aktualne.");
            }
        }

        if (vin.getTechnicalInspectionStatus() != null) {
            switch (vin.getTechnicalInspectionStatus()) {
                case INVALID -> dto.red("Badanie techniczne: nieaktualne.");
                case NO_DATA -> dto.yellow("Badanie techniczne: brak danych.");
                default -> dto.green("Badanie techniczne: aktualne.");
            }
        }

        if (vin.getRegistrationStatus() != null) {
            switch (vin.getRegistrationStatus()) {
                case NOT_REGISTERED -> dto.red("Status rejestracji: niezarejestrowany.");
                case NO_DATA -> dto.yellow("Status rejestracji: brak danych.");
                default -> dto.green("Status rejestracji: zarejestrowany.");
            }
        }

        // dane zagraniczne OR (CarRisk: FOUNDED/NOT_FOUND)
        if (isFounded(vin.getTheft())) dto.red("Dane zagraniczne: odnotowano kradzież.");
        if (isFounded(vin.getTotalLoss())) dto.red("Dane zagraniczne: odnotowano szkodę całkowitą.");
        if (isFounded(vin.getOdometerMismatch())) dto.red("Dane zagraniczne: odnotowano rozbieżność licznika.");
        if (isFounded(vin.getScrapped())) dto.red("Dane zagraniczne: odnotowano złomowanie.");
        if (isFounded(vin.getNotRoadworthy())) dto.red("Dane zagraniczne: odnotowano niedopuszczenie do ruchu.");
        if (isFounded(vin.getVinChecksumError())) dto.yellow("Dane zagraniczne: odnotowano błąd cyfry kontrolnej VIN.");
        if (isFounded(vin.getServiceActions())) dto.yellow("Dane zagraniczne: odnotowano akcje serwisowe.");
        if (isFounded(vin.getTaxi())) dto.yellow("Dane zagraniczne: odnotowano użytkowanie jako taxi.");
        if (isFounded(vin.getAccident())) dto.yellow("Dane zagraniczne: odnotowano powypadkowy.");
        if (isFounded(vin.getDamaged())) dto.yellow("Dane zagraniczne: odnotowano uszkodzenia.");
    }

    private boolean isFounded(CarRisk r) {
        return r != null && r == CarRisk.FOUNDED;
    }

    // ===== LAKIER =====
    private void applyPaint(InspectionSummaryDto dto, PaintCheck paint) {
        if (paint == null) {
            dto.yellow("Brak wypełnionej kontroli lakieru.");
            return;
        }

        if (paint.isNoThicknessMeasurements()) {
            dto.yellow("Lakier: brak pomiarów grubości (tryb bez pomiarów).");
        }

        paintFlag(dto, "Maska", paint.isHoodDifferent(), paint.getMinHoodThickness(), paint.getMaxHoodThickness());
        paintFlag(dto, "Dach", paint.isRoofDifferent(), paint.getMinRoofThickness(), paint.getMaxRoofThickness());

        paintFlag(dto, "Drzwi przód lewy", paint.isFrontLeftDoorDifferent(),
                paint.getMinFrontLeftDoorThickness(), paint.getMaxFrontLeftDoorThickness());
        paintFlag(dto, "Drzwi tył lewy", paint.isRearLeftDoorDifferent(),
                paint.getMinRearLeftDoorThickness(), paint.getMaxRearLeftDoorThickness());
        paintFlag(dto, "Drzwi przód prawy", paint.isFrontRightDoorDifferent(),
                paint.getMinFrontRightDoorThickness(), paint.getMaxFrontRightDoorThickness());
        paintFlag(dto, "Drzwi tył prawy", paint.isRearRightDoorDifferent(),
                paint.getMinRearRightDoorThickness(), paint.getMaxRearRightDoorThickness());

        paintFlag(dto, "Klapa bagażnika", paint.isTrunkDifferent(),
                paint.getMinTrunkThickness(), paint.getMaxTrunkThickness());

        paintFlag(dto, "Słupek A lewy", paint.isPillarALeftDifferent(),
                paint.getMinPillarALeftThickness(), paint.getMaxPillarALeftThickness());
        paintFlag(dto, "Słupek A prawy", paint.isPillarARightDifferent(),
                paint.getMinPillarARightThickness(), paint.getMaxPillarARightThickness());
        paintFlag(dto, "Słupek B lewy", paint.isPillarBLeftDifferent(),
                paint.getMinPillarBLeftThickness(), paint.getMaxPillarBLeftThickness());
        paintFlag(dto, "Słupek B prawy", paint.isPillarBRightDifferent(),
                paint.getMinPillarBRightThickness(), paint.getMaxPillarBRightThickness());
        paintFlag(dto, "Słupek C lewy", paint.isPillarCLeftDifferent(),
                paint.getMinPillarCLeftThickness(), paint.getMaxPillarCLeftThickness());
        paintFlag(dto, "Słupek C prawy", paint.isPillarCRightDifferent(),
                paint.getMinPillarCRightThickness(), paint.getMaxPillarCRightThickness());

        paintFlag(dto, "Nadkole przód lewe", paint.isFrontLeftWheelArchDifferent(),
                paint.getMinFrontLeftWheelArchThickness(), paint.getMaxFrontLeftWheelArchThickness());
        paintFlag(dto, "Nadkole przód prawe", paint.isFrontRightWheelArchDifferent(),
                paint.getMinFrontRightWheelArchThickness(), paint.getMaxFrontRightWheelArchThickness());
        paintFlag(dto, "Nadkole tył lewe", paint.isRearLeftWheelArchDifferent(),
                paint.getMinRearLeftWheelArchThickness(), paint.getMaxRearLeftWheelArchThickness());
        paintFlag(dto, "Nadkole tył prawe", paint.isRearRightWheelArchDifferent(),
                paint.getMinRearRightWheelArchThickness(), paint.getMaxRearRightWheelArchThickness());

        paintFlag(dto, "Zderzak przedni", paint.isFrontBumperDifferent(),
                paint.getMinFrontBumperThickness(), paint.getMaxFrontBumperThickness());
        paintFlag(dto, "Zderzak tylny", paint.isRearBumperDifferent(),
                paint.getMinRearBumperThickness(), paint.getMaxRearBumperThickness());

        // Uszkodzenia lakieru (masz je w encji)
        if (paint.getDamages() != null) {
            for (PaintDamage d : paint.getDamages()) {
                dto.yellow("Uszkodzenie lakieru: " + safe(d.getBodyPart()) + " / " +
                        safe(d.getDamageType()) + " / " + safe(d.getSize()));
            }
        }
    }

    private String safe(String s) {
        return s == null ? "brak" : s;
    }

    private void paintFlag(InspectionSummaryDto dto,
                           String part,
                           boolean different,
                           Integer min,
                           Integer max) {
        if (!different) return;

        Integer worst = worstThickness(min, max);

        if (worst != null && worst >= 350) {
            dto.red("Lakier: " + part + " różni się, zakres " + formatRange(min, max) + " µm (podejrzenie naprawy).");
        } else {
            dto.yellow("Lakier: " + part + " różni się, zakres " + formatRange(min, max) + " µm.");
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

    // ===== PYTANIA =====
    private void applyQuestions(InspectionSummaryDto dto, List<QuestionAnswer> answers) {
        if (answers == null || answers.isEmpty()) {
            dto.yellow("Brak wypełnionej checklisty pytań.");
            return;
        }

        for (var a : answers) {
            var q = a.getQuestion();
            if (q == null) continue;

            String key = q.getQuestionKey(); // np. "oil_level"
            String opt = (a.getSelectedOption() != null) ? a.getSelectedOption().getValue() : null;

            // jeśli nie wybrano opcji
            if (opt == null) {
                dto.yellow("Checklist: brak odpowiedzi dla: " + key);
                continue;
            }

            // reguły MVP na podstawie option value (Twoje wartości)
            // Zasada: "ok/good/none/clean/correct/smooth/quiet/effective/precise/stable/normal/readable/coherent"
            //         => green
            //         "single/partial/light/borderline/low/weak/slow/minor/temporary/occasional/play/resistance/dark"
            //         => yellow
            //         "not_working/damaged/cracked/worn/mismatch/missing/major/problem/stalling/constant/faulty/slipping/loud_noise/strong"
            //         => red

            RiskBand band = classifyOption(opt);

            switch (band) {
                case GREEN -> dto.green("Checklist OK: " + key);
                case YELLOW -> dto.yellow("Checklist UWAGA: " + key + " = " + opt);
                case RED -> dto.red("Checklist PROBLEM: " + key + " = " + opt);
            }
        }
    }

    private enum RiskBand { GREEN, YELLOW, RED }

    private RiskBand classifyOption(String opt) {
        String o = opt.toLowerCase();

        // RED
        if (o.contains("not_working") || o.contains("damaged") || o.contains("cracked") ||
                o.contains("worn") || o.contains("mismatch") || o.contains("missing") ||
                o.contains("major") || o.contains("problem") || o.contains("stalling") ||
                o.contains("constant") || o.contains("faulty") || o.contains("slipping") ||
                o.contains("loud_noise") || o.contains("strong")) {
            return RiskBand.RED;
        }

        // YELLOW
        if (o.contains("single") || o.contains("partial") || o.contains("light") ||
                o.contains("borderline") || o.contains("low") || o.contains("weak") ||
                o.contains("slow") || o.contains("minor") || o.contains("temporary") ||
                o.contains("occasional") || o.contains("play") || o.contains("resistance") ||
                o.contains("dark") || o.contains("distorted") || o.contains("jerky") ||
                o.contains("unstable") || o.contains("long") || o.contains("high")) {
            return RiskBand.YELLOW;
        }

        // GREEN (domyślnie)
        return RiskBand.GREEN;
    }
}

