package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.model.CarRisk;
import bavteqdoit.carhealthcheck.model.OcStatus;
import bavteqdoit.carhealthcheck.model.RegistrationStatus;
import bavteqdoit.carhealthcheck.model.TechnicalInspectionStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VinPdfParserService {
    public Integer extractProductionYear(String text){
        Matcher productionYear = Pattern.compile("Rok\\s+produkcji\\s*[:\\-]?\\s*(\\d{4})").matcher(text);
        return productionYear.find() ? Integer.parseInt(productionYear.group(1)) : null;
    }

    public record BrandModel(String brand, String model) {}

    public LocalDate extractFirstRegistration(String text){
        Matcher firstRegistration = Pattern
                .compile("(\\d{2}\\.\\d{2}\\.\\d{4})\\s+Pierwsza\\s+rejestracja\\s+w\\s+Polsce", Pattern.CASE_INSENSITIVE)
                .matcher(text);

        if(firstRegistration.find()){
            String[] p = firstRegistration.group(1).split("\\.");
            return LocalDate.of(Integer.parseInt(p[2]), Integer.parseInt(p[1]), Integer.parseInt(p[0]));
        }

        Matcher iso = Pattern.compile("Data\\s+pierwszej\\s+rejestracji\\s*[:\\-]?\\s*(\\d{4}-\\d{2}-\\d{2})").matcher(text);
        if(iso.find()){
            return LocalDate.parse(iso.group(1));
        }

        Matcher pl = Pattern.compile("Data\\s+pierwszej\\s+rejestracji\\s*[:\\-]?\\s*(\\d{2}\\.\\d{2}\\.\\d{4})").matcher(text);
        if(pl.find()){
            String[] p = pl.group(1).split("\\.");
            return LocalDate.of(Integer.parseInt(p[2]), Integer.parseInt(p[1]), Integer.parseInt(p[0]));
        }

        return null;
    }

    public String extractPlateNumber(String text) {
        var m = java.util.regex.Pattern
                .compile("Numer\\s+rejestracyjny\\s+([A-Z0-9]+)")
                .matcher(text);
        return m.find() ? m.group(1) : null;
    }

    public java.time.LocalDate extractOcValidUntil(String text) {
        var m = java.util.regex.Pattern
                .compile("Data\\s+ważności\\s+polisy\\s+(\\d{2}\\.\\d{2}\\.\\d{4})")
                .matcher(text);
        if (!m.find()) return null;

        String[] p = m.group(1).split("\\.");
        return java.time.LocalDate.of(
                Integer.parseInt(p[2]),
                Integer.parseInt(p[1]),
                Integer.parseInt(p[0])
        );
    }

    public Integer extractLastOdometerKm(String text) {
        var m = java.util.regex.Pattern
                .compile("Ostatni\\s+stan\\s+licznika\\s+([\\d\\s]+)\\s*km")
                .matcher(text);
        if (!m.find()) return null;

        return Integer.parseInt(m.group(1).replaceAll("\\s+", ""));
    }

    public TechnicalInspectionStatus extractTechnicalInspectionStatusEnum(String text) {
        String lower = text.toLowerCase();

        if (lower.contains("badanie techniczne: aktualne")) return TechnicalInspectionStatus.VALID;
        if (lower.contains("badanie techniczne: nieaktualne")) return TechnicalInspectionStatus.INVALID;

        return TechnicalInspectionStatus.NO_DATA;
    }

    public RegistrationStatus extractRegistrationStatusEnum(String text) {
        String lower = text.toLowerCase();

        if (lower.contains("status rejestracji: zarejestrowany")) return RegistrationStatus.REGISTERED;
        if (lower.contains("status rejestracji: niezarejestrowany")) return RegistrationStatus.NOT_REGISTERED;

        return RegistrationStatus.NO_DATA;
    }

    public OcStatus extractOcStatusEnum(String text) {
        String lower = text.toLowerCase();

        if (lower.contains("polisa oc: aktualna")) return OcStatus.VALID;
        if (lower.contains("polisa oc: nieaktualna")) return OcStatus.INVALID;

        return OcStatus.NO_DATA;
    }

    public String extractVinFromReport(String text) {
        if (text == null) return null;

        text = text.replace('\u00A0', ' ');

        Matcher m = Pattern.compile(
                "(?is)Numer\\s*VIN\\s*,?[^A-Z0-9]*([A-HJ-NPR-Z0-9](?:\\s*[A-HJ-NPR-Z0-9]){16})"
        ).matcher(text);

        if (m.find()) {
            return m.group(1).replaceAll("\\s+", "").toUpperCase();
        }

        int idx = text.toLowerCase().indexOf("numer vin");
        if (idx >= 0) {
            int end = Math.min(text.length(), idx + 250);
            String window = text.substring(idx, end);

            Matcher m2 = Pattern.compile("([A-HJ-NPR-Z0-9]{17})").matcher(window);
            if (m2.find()) return m2.group(1).toUpperCase();

            Matcher m3 = Pattern.compile("([A-HJ-NPR-Z0-9](?:\\s*[A-HJ-NPR-Z0-9]){16})").matcher(window);
            if (m3.find()) return m3.group(1).replaceAll("\\s+", "").toUpperCase();
        }

        return null;
    }


    public BrandModel extractBrandAndModel(String text) {
        if (text == null) return null;

        Matcher m = Pattern
                .compile("(?mi)^\\s*Pojazd\\s+([^,\\r\\n]+)\\s*,\\s*([^,\\r\\n]+)\\s*,")
                .matcher(text);

        if (!m.find()) return null;

        String brand = normalizeLabel(m.group(1));
        String model = normalizeLabel(m.group(2));

        return new BrandModel(brand, model);
    }

    private String normalizeLabel(String s) {
        if (s == null) return null;
        return s.trim().replaceAll("\\s{2,}", " ");
    }

    public CarRisk extractTheftRisk(String text) {
        return extractForeignRiskOr(text, "Kradzież");
    }

    public CarRisk extractScrappedRisk(String text) {
        return extractForeignRiskOr(text, "Złomowanie");
    }

    public CarRisk extractAccidentRisk(String text) {
        return extractForeignRiskOr(text, "Powypadkowy");
    }

    public CarRisk extractDamagedRisk(String text) {
        return extractForeignRiskOr(text, "Uszkodzony");
    }

    public CarRisk extractOdometerMismatchRisk(String text) {
        return extractForeignRiskOr(text, "Rozbieżność licznika");
    }

    public CarRisk extractNotRoadworthyRisk(String text) {
        return extractForeignRiskOr(text, "Niedopuszczony do ruchu");
    }

    public CarRisk extractTaxiRisk(String text) {
        return extractForeignRiskOr(text, "Służył jako taxi");
    }

    public CarRisk extractTotalLossRisk(String text) {
        return extractForeignRiskOr(text, "Szkoda całkowita");
    }

    public CarRisk extractVinChecksumErrorRisk(String text) {
        return extractForeignRiskOr(text, "Błąd cyfry kontrolnej w numerze VIN");
    }

    public CarRisk extractServiceActionsRisk(String text) {
        return extractForeignRiskOr(text, "Akcje serwisowe");
    }


    private CarRisk extractForeignRiskOr(String fullText, String label) {
        String foreign = substringFrom(fullText, "Dane zagraniczne");

        if (foreign.isBlank()) return CarRisk.NOT_FOUND;

        String carfaxBlock = substringBetween(foreign, "Ryzyka według Carfax", "Ryzyka według autoDNA");
        String autodnaBlock = substringFrom(foreign, "Ryzyka według autoDNA");

        boolean hasAny = !(carfaxBlock.isBlank() && autodnaBlock.isBlank());
        if (!hasAny) return CarRisk.NOT_FOUND;

        if (isNoted(carfaxBlock, label) || isNoted(autodnaBlock, label)) {
            return CarRisk.FOUNDED;
        }

        return CarRisk.NOT_FOUND;
    }

    private boolean isNoted(String block, String label) {
        if (block == null || block.isBlank()) return false;

        Pattern p = Pattern.compile(
                "(?mi)^\\s*" + Pattern.quote(label) + "\\s+(odnotowano|nie odnotowano)\\s*$"
        );
        Matcher m = p.matcher(block);
        if (!m.find()) return false;

        String status = m.group(1).toLowerCase();
        return status.equals("odnotowano");
    }

    private String substringFrom(String text, String marker) {
        if (text == null) return "";
        int idx = text.indexOf(marker);
        return idx >= 0 ? text.substring(idx) : "";
    }

    private String substringBetween(String text, String start, String end) {
        if (text == null) return "";
        int s = text.indexOf(start);
        if (s < 0) return "";
        int e = text.indexOf(end, s + start.length());
        return e >= 0 ? text.substring(s, e) : text.substring(s);
    }
}
