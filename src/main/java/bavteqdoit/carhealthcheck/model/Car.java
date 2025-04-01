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

    @NotBlank(message = "Car name cannot be empty")
    @Size(min = 5, max = 50, message = "Car name must be between 5 and 50 characters")
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    @NotNull(message = "You have to select your car brand")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_type_id", nullable = false)
    @NotNull(message = "You have to select your car model")
    private ModelType modelType;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    @NotNull(message = "You have to select a color")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "engine_type_id", nullable = false)
    @NotNull(message = "You have to select an engine type")
    private EngineType engineType;

    @ManyToOne
    @JoinColumn(name = "body_type_id", nullable = false)
    @NotNull(message = "You have to select a body type")
    private BodyType bodyType;


}
