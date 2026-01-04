package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.model.VinMileageEntry;
import bavteqdoit.carhealthcheck.model.VinMileageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class VinTimelineParserService {

    private static final Pattern EVENT_START = Pattern.compile("^\\s*(\\d{2}\\.\\d{2}\\.\\d{4})\\s+(.+)$");
    private static final Pattern MILEAGE_LINE = Pattern.compile("Odczytany\\s+stan\\s+licznika:\\s*([\\d\\s]+)\\s*km");

    public List<ParsedMileage> extractMileageEntries(String fullText) {
        String[] lines = fullText.split("\\R");
        List<ParsedMileage> out = new ArrayList<>();

        LocalDate currentDate = null;
        String currentTitle = null;
        StringBuilder block = new StringBuilder();

        for (String raw : lines) {
            String line = raw.trim();

            var mStart = EVENT_START.matcher(line);
            if (mStart.matches()) {
                if (currentDate != null) {
                    out.addAll(parseMileageFromBlock(currentDate, currentTitle, block.toString()));
                }
                currentDate = parsePlDate(mStart.group(1));
                currentTitle = mStart.group(2).trim();
                block.setLength(0);
                continue;
            }

            if (currentDate != null) {
                block.append(line).append("\n");
            }
        }

        if (currentDate != null) {
            out.addAll(parseMileageFromBlock(currentDate, currentTitle, block.toString()));
        }

        return out;
    }

    private List<ParsedMileage> parseMileageFromBlock(LocalDate date, String title, String blockText) {
        List<ParsedMileage> res = new ArrayList<>();

        var m = MILEAGE_LINE.matcher(blockText);
        while (m.find()) {
            int km = Integer.parseInt(m.group(1).replaceAll("\\s+", ""));

            VinMileageSource source = detectSource(title, blockText);

            res.add(new ParsedMileage(date, km, source, title));
        }
        return res;
    }

    private VinMileageSource detectSource(String title, String blockText) {
        String t = (title == null ? "" : title).toLowerCase();
        String b = (blockText == null ? "" : blockText).toLowerCase();

        if (t.contains("kontroli drogowej") || b.contains("policja")) {
            return VinMileageSource.POLICE_CONTROL;
        }
        if (t.contains("badanie techniczne")) {
            return VinMileageSource.TECHNICAL_INSPECTION;
        }
        return VinMileageSource.UNKNOWN;
    }

    private LocalDate parsePlDate(String ddMMyyyy) {
        String[] p = ddMMyyyy.split("\\.");
        return LocalDate.of(Integer.parseInt(p[2]), Integer.parseInt(p[1]), Integer.parseInt(p[0]));
    }

    public record ParsedMileage(LocalDate readingDate, int mileageKm, VinMileageSource source, String eventTitle) {}
}
