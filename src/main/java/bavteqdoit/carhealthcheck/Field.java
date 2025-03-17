package bavteqdoit.carhealthcheck;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Field {
    private final Long id;
    private final String name;
    private final Type type;

    public enum Type {
        BRAND, MODEL, COLOR, ENGINE_TYPE, GEAR_TYPE, BODY_TYPE, MILEAGE_UNIT, STEERING_SIDE
    }
}
