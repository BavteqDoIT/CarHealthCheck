package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{car.name.null}")
    @Size(min = 5, max = 50, message = "{car.name.size}")
    private String name;

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
