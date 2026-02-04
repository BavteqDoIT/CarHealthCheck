package bavteqdoit.carhealthcheck.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EngineTypeForm {

    private Long id;

    @NotNull(message = "{admin.engine.brand.required}")
    private Long brandId;

    @NotBlank(message = "{admin.engine.name.required}")
    private String name;

    @NotNull(message = "{admin.engine.fuel.required}")
    private Long fuelTypeId;

    @NotNull(message = "{admin.engine.capacity.required}") @Min(1)
    private Integer capacity;

    @NotNull(message = "{admin.engine.hp.required}") @Min(1)
    private Integer horsepowerHp;

    @NotNull(message = "{admin.engine.kw.required}") @Min(1)
    private Integer powerKw;
}

