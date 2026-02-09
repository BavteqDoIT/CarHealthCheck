package bavteqdoit.carhealthcheck.dto;

import bavteqdoit.carhealthcheck.model.ValidationField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VinValidationView {
    private boolean hasBlockingIssues;
    private List<Item> items = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class Item {
        private ValidationField field;
        private String formValue;
        private String reportValue;
        private Severity severity;
        private String messageKey;
        private Resolution currentResolution;
    }

    public enum Severity { OK, WARN, BLOCK }
    public enum Resolution { USE_FORM, USE_REPORT, MANUAL }
}

