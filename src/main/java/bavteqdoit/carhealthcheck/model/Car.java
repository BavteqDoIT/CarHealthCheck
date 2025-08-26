package bavteqdoit.carhealthcheck.model;

import bavteqdoit.carhealthcheck.validation.Year;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{car.name.null}")
    @Size(min = 5, max = 50, message = "{car.name.size}")
    private String name;

    @NotNull(message = "{car.year.null}")
    @Year(min = 1900, allowFuture = false, message = "{car.year.invalid}")
    private Integer productionYear;

    @NotNull(message = "{car.mileage.null}")
    @Min(value = 0, message = "{car.mileage.min}")
    @Max(value = 99999999, message = "{car.mileage.max}")
    private Integer mileage;

    @NotBlank(message = "{car.vin.null}")
    @Size(min = 17, max = 17, message = "{car.vin.size}")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "{car.vin.invalid}")
    private String vin;

    private LocalDate firstRegistrationDate;

    private boolean foreignRegistered;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    @NotNull(message = "{car.brand.null}")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_type_id", nullable = false)
    @NotNull(message = "{car.brand.null}")
    private ModelType modelType;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    @NotNull(message = "{car.color.null}")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "engine_type_id", nullable = false)
    @NotNull(message = "{car.engine.null}")
    private EngineType engineType;

    @ManyToOne
    @JoinColumn(name = "body_type_id", nullable = false)
    @NotNull(message = "{car.body.null}")
    private BodyType bodyType;

    @ManyToOne
    @JoinColumn(name = "drive_type_id", nullable = false)
    @NotNull(message = "{car.drive.null}")
    private DriveType driveType;

    @ManyToOne
    @JoinColumn(name = "gearbox_type_id", nullable = false)
    @NotNull(message = "{car.gearbox.null}")
    private GearboxType gearboxType;
}
