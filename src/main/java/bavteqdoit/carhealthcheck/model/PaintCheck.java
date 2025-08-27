package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "paintCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaintDamage> damages = new ArrayList<>();

    @NotNull(message = "{paint.not.null.message}")
    private Integer hoodThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer roofThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer frontLeftDoorThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer rearLeftDoorThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer frontRightDoorThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer rearRightDoorThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer trunkThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer pillarALeftThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer pillarARightThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer pillarBLeftThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer pillarBRightThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer pillarCLeftThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer pillarCRightThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer frontLeftWheelArchThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer frontRightWheelArchThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer rearLeftWheelArchThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer rearRightWheelArchThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer frontBumperThickness;
    @NotNull(message = "{paint.not.null.message}")
    private Integer rearBumperThickness;
}
