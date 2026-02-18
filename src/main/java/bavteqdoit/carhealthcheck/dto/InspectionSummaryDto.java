package bavteqdoit.carhealthcheck.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class InspectionSummaryDto {

    private List<String> redFlags = new ArrayList<>();
    private List<String> yellowFlags = new ArrayList<>();
    private List<String> greenNotes = new ArrayList<>();
    private List<String> paintDamageDetails = new ArrayList<>();

    private Integer basicScoreUsed;

    private Integer finalScore;
    private String riskLevel;
    private Integer riskScore;
    private List<String> riskReasons = new ArrayList<>();

    public void red(String msg) {
        redFlags.add(msg);
    }

    public void yellow(String msg) {
        yellowFlags.add(msg);
    }

    public void green(String msg) {
        greenNotes.add(msg);
    }
}
