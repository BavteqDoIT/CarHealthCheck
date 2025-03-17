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

    public enum Brand {
        OPEL, SKODA, MERCEDES, BMW, MINICOOPER, AUDI
    }

    public enum Model {
        ASTRA, CORSA, MOKKA, FABIA, OCTAVIA, SUPERB, VITO, SLK, GT, SERIES3, SERIES5, SERIES7, CABRIO, CLUBMAN, COUNTRYMAN, A3, A5, S3
    }

    public enum Color {
        BLUE, RED, YELLOW, GREEN, BLACK, WHITE, ORANGE, GOLD, SILVER, BROWN, MAGENTA, PURPLE, PINK
    }

    public enum EngineType {
        PETROL, PETROL_CNG, PETROL_LPG, DIESEL, ELECTRIC, ETHANOL, HYBRID, PLUG_IN_HYBRID, HYDROGEN
    }

    public enum GearType {
        AUTOMATIC, MANUAL
    }

    public enum BodyType {
        SEDAN, HATCHBACK, WAGON, COUPE, CONVERTIBLE, SUV, CROSSOVER, PICKUP, MINIVAN, VAN, OFF_ROAD, ROADSTER
    }

    public enum MileageUnit {
        KILOMETERS, MILES
    }

    public enum SteeringSide {
        RIGHT, LEFT
    }
}
