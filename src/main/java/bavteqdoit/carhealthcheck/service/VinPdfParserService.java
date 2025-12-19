package bavteqdoit.carhealthcheck.service;

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
}
