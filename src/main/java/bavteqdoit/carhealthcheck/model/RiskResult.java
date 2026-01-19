package bavteqdoit.carhealthcheck.model;

import lombok.Data;

import java.util.List;

@Data
public class RiskResult {
    private int finalScore;
    private RiskLevel riskLevel;
    private List<String> riskReasons;
}
