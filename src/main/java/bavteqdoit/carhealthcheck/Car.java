package bavteqdoit.carhealthcheck;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Car {
    private final Long id;
    private final Brand brand;
    private final Model model;
    private final Integer year;
    private final Color color;
    private final Double engineCapacity;
    private final EngineType engineType;
    private final GearType gearType;
    private final BigDecimal price;
    private final BodyType bodyType;
    private final Long mileage;
    private final MileageUnit mileageUnit;
    private final Boolean isAccidentFree;
    private final Integer doors;
    private final Integer seats;
    private final String vin;
    private final LocalDate registrationDate;
    private final SteeringSide steeringSide;

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

    private enum MileageUnit {
        KILOMETERS, MILES
    }

    private enum SteeringSide {
        RIGHT, LEFT
    }
}
