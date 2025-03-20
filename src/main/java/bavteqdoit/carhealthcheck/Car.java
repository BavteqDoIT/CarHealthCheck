package bavteqdoit.carhealthcheck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Car {
    @NotNull(message = "You have to select your car brand")
    private Long brand;

    @NotNull(message = "You have to select your car model")
    private Long model;

    @NotNull(message = "You have to select a color")
    private Long color;

    @NotNull(message = "You have to select an engine type")
    private Long engineType;

    @NotNull(message = "You have to select a body type")
    private Long bodyType;

    @NotBlank(message = "Car name cannot be empty")
    @Size(min = 5, max = 50, message = "Car name must be between 5 and 50 characters")
    private String name;
}
