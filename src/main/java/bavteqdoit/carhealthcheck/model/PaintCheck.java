package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "paintCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaintDamage> damages = new ArrayList<>();

    private Integer hoodThickness;
    private Integer roofThickness;
    private Integer frontLeftDoorThickness;
    private Integer rearLeftDoorThickness;
    private Integer frontRightDoorThickness;
    private Integer rearRightDoorThickness;
    private Integer trunkThickness;
    private Integer pillarALeftThickness;
    private Integer pillarARightThickness;
    private Integer pillarBLeftThickness;
    private Integer pillarBRightThickness;
    private Integer pillarCLeftThickness;
    private Integer pillarCRightThickness;
    private Integer frontLeftWheelArchThickness;
    private Integer frontRightWheelArchThickness;
    private Integer rearLeftWheelArchThickness;
    private Integer rearRightWheelArchThickness;
}
