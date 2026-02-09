package bavteqdoit.carhealthcheck.dto;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class VinResolveForm {
    private Map<String, VinValidationView.Resolution> resolution = new HashMap<>();
    private Map<String, String> manualValue = new HashMap<>();
}
