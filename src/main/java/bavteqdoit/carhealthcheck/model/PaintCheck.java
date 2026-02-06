package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class PaintCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "car_id", unique = true)
    private Car car;

    private boolean hoodDifferent;
    private boolean roofDifferent;

    private boolean frontLeftDoorDifferent;
    private boolean rearLeftDoorDifferent;
    private boolean frontRightDoorDifferent;
    private boolean rearRightDoorDifferent;

    private boolean trunkDifferent;

    private boolean frontLeftWheelArchDifferent;
    private boolean frontRightWheelArchDifferent;
    private boolean rearLeftWheelArchDifferent;
    private boolean rearRightWheelArchDifferent;

    private boolean pillarALeftDifferent;
    private boolean pillarARightDifferent;
    private boolean pillarBLeftDifferent;
    private boolean pillarBRightDifferent;
    private boolean pillarCLeftDifferent;
    private boolean pillarCRightDifferent;
    private boolean frontBumperDifferent;
    private boolean rearBumperDifferent;

    private boolean noThicknessMeasurements;

    @OneToMany(mappedBy = "paintCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaintDamage> damages = new ArrayList<>();

    private Integer minHoodThickness;
    private Integer maxHoodThickness;
    private Integer minRoofThickness;
    private Integer maxRoofThickness;
    private Integer minFrontLeftDoorThickness;
    private Integer maxFrontLeftDoorThickness;
    private Integer minRearLeftDoorThickness;
    private Integer maxRearLeftDoorThickness;
    private Integer minFrontRightDoorThickness;
    private Integer maxFrontRightDoorThickness;
    private Integer minRearRightDoorThickness;
    private Integer maxRearRightDoorThickness;
    private Integer minTrunkThickness;
    private Integer maxTrunkThickness;
    private Integer minPillarALeftThickness;
    private Integer maxPillarALeftThickness;
    private Integer minPillarARightThickness;
    private Integer maxPillarARightThickness;
    private Integer minPillarBLeftThickness;
    private Integer maxPillarBLeftThickness;
    private Integer minPillarBRightThickness;
    private Integer maxPillarBRightThickness;
    private Integer minPillarCLeftThickness;
    private Integer maxPillarCLeftThickness;
    private Integer minPillarCRightThickness;
    private Integer maxPillarCRightThickness;
    private Integer minFrontLeftWheelArchThickness;
    private Integer maxFrontLeftWheelArchThickness;
    private Integer minFrontRightWheelArchThickness;
    private Integer maxFrontRightWheelArchThickness;
    private Integer minRearLeftWheelArchThickness;
    private Integer maxRearLeftWheelArchThickness;
    private Integer minRearRightWheelArchThickness;
    private Integer maxRearRightWheelArchThickness;
    private Integer minFrontBumperThickness;
    private Integer maxFrontBumperThickness;
    private Integer minRearBumperThickness;
    private Integer maxRearBumperThickness;


    @AssertTrue(message = "{paint.not.blank.message}")
    public boolean isMinMaxValid() {
        if (noThicknessMeasurements) {
            return true;
        }

        return isMinMaxCorrect(minHoodThickness, maxHoodThickness) &&
                isMinMaxCorrect(minRoofThickness, maxRoofThickness) &&
                isMinMaxCorrect(minFrontLeftDoorThickness, maxFrontLeftDoorThickness) &&
                isMinMaxCorrect(minRearLeftDoorThickness, maxRearLeftDoorThickness) &&
                isMinMaxCorrect(minFrontRightDoorThickness, maxFrontRightDoorThickness) &&
                isMinMaxCorrect(minRearRightDoorThickness, maxRearRightDoorThickness) &&
                isMinMaxCorrect(minTrunkThickness, maxTrunkThickness) &&
                isMinMaxCorrect(minPillarALeftThickness, maxPillarALeftThickness) &&
                isMinMaxCorrect(minPillarARightThickness, maxPillarARightThickness) &&
                isMinMaxCorrect(minPillarBLeftThickness, maxPillarBLeftThickness) &&
                isMinMaxCorrect(minPillarBRightThickness, maxPillarBRightThickness) &&
                isMinMaxCorrect(minPillarCLeftThickness, maxPillarCLeftThickness) &&
                isMinMaxCorrect(minPillarCRightThickness, maxPillarCRightThickness) &&
                isMinMaxCorrect(minFrontLeftWheelArchThickness, maxFrontLeftWheelArchThickness) &&
                isMinMaxCorrect(minFrontRightWheelArchThickness, maxFrontRightWheelArchThickness) &&
                isMinMaxCorrect(minRearLeftWheelArchThickness, maxRearLeftWheelArchThickness) &&
                isMinMaxCorrect(minRearRightWheelArchThickness, maxRearRightWheelArchThickness) &&
                isMinMaxCorrect(minFrontBumperThickness, maxFrontBumperThickness) &&
                isMinMaxCorrect(minRearBumperThickness, maxRearBumperThickness);
    }

    private boolean isMinMaxCorrect(Integer min, Integer max) {
        if (min == null || max == null) return false;
        return min <= max;
    }

    public void forEachThickness(java.util.function.Consumer<Integer> consumer) {
        Integer[] values = {
                getMaxHoodThickness(),
                getMaxRoofThickness(),
                getMaxFrontLeftDoorThickness(),
                getMaxFrontRightDoorThickness(),
                getMaxRearLeftDoorThickness(),
                getMaxRearRightDoorThickness(),
                getMaxTrunkThickness(),
                getMaxFrontLeftWheelArchThickness(),
                getMaxFrontRightWheelArchThickness(),
                getMaxRearLeftWheelArchThickness(),
                getMaxRearRightWheelArchThickness(),
                getMaxPillarALeftThickness(),
                getMaxPillarARightThickness(),
                getMaxPillarBLeftThickness(),
                getMaxPillarBRightThickness(),
                getMaxPillarCLeftThickness(),
                getMaxPillarCRightThickness(),
                getMaxFrontBumperThickness(),
                getMaxRearBumperThickness()
        };

        for (Integer v : values) {
            if (v != null) consumer.accept(v);
        }
    }

    public void forEachThicknessMinMax(java.util.function.Consumer<Integer> consumer) {
        Integer[] values = {
                // Hood
                getMinHoodThickness(), getMaxHoodThickness(),
                // Roof
                getMinRoofThickness(), getMaxRoofThickness(),
                // Doors
                getMinFrontLeftDoorThickness(), getMaxFrontLeftDoorThickness(),
                getMinRearLeftDoorThickness(), getMaxRearLeftDoorThickness(),
                getMinFrontRightDoorThickness(), getMaxFrontRightDoorThickness(),
                getMinRearRightDoorThickness(), getMaxRearRightDoorThickness(),
                // Trunk
                getMinTrunkThickness(), getMaxTrunkThickness(),
                // Wheel arches
                getMinFrontLeftWheelArchThickness(), getMaxFrontLeftWheelArchThickness(),
                getMinFrontRightWheelArchThickness(), getMaxFrontRightWheelArchThickness(),
                getMinRearLeftWheelArchThickness(), getMaxRearLeftWheelArchThickness(),
                getMinRearRightWheelArchThickness(), getMaxRearRightWheelArchThickness(),
                // Pillars
                getMinPillarALeftThickness(), getMaxPillarALeftThickness(),
                getMinPillarARightThickness(), getMaxPillarARightThickness(),
                getMinPillarBLeftThickness(), getMaxPillarBLeftThickness(),
                getMinPillarBRightThickness(), getMaxPillarBRightThickness(),
                getMinPillarCLeftThickness(), getMaxPillarCLeftThickness(),
                getMinPillarCRightThickness(), getMaxPillarCRightThickness(),
                // Bumpers
                getMinFrontBumperThickness(), getMaxFrontBumperThickness(),
                getMinRearBumperThickness(), getMaxRearBumperThickness()
        };

        for (Integer v : values) {
            if (v != null) consumer.accept(v);
        }
    }

    public List<Integer> getAllThicknessValues() {
        List<Integer> values = new ArrayList<>();
        forEachThicknessMinMax(values::add);
        return values;
    }

    public List<Integer> getAllMaxThicknessValues() {
        List<Integer> values = new ArrayList<>();
        forEachThickness(values::add);
        return values;
    }


    public long countDifferentParts() {
        return java.util.stream.Stream.of(
                isHoodDifferent(),
                isRoofDifferent(),
                isFrontLeftDoorDifferent(),
                isFrontRightDoorDifferent(),
                isRearLeftDoorDifferent(),
                isRearRightDoorDifferent(),
                isTrunkDifferent(),
                isFrontLeftWheelArchDifferent(),
                isFrontRightWheelArchDifferent(),
                isRearLeftWheelArchDifferent(),
                isRearRightWheelArchDifferent(),
                isPillarALeftDifferent(),
                isPillarARightDifferent(),
                isPillarBLeftDifferent(),
                isPillarBRightDifferent(),
                isPillarCLeftDifferent(),
                isPillarCRightDifferent(),
                isFrontBumperDifferent(),
                isRearBumperDifferent()
        ).filter(Boolean.TRUE::equals).count();
    }
}
