package bavteqdoit.carhealthcheck.service;

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


}
