package bavteqdoit.carhealthcheck.model;

public enum ValidationField {
    VIN("validation.field.vin"),
    MILEAGE("validation.field.mileage"),
    PRODUCTION_YEAR("validation.field.productionYear"),
    FIRST_REG_PL("validation.field.firstRegPl"),
    BRAND("validation.field.brand"),
    MODEL("validation.field.model"),;

    private final String labelKey;

    ValidationField(String labelKey) {
        this.labelKey = labelKey;
    }

    public String labelKey() {
        return labelKey;
    }
}

