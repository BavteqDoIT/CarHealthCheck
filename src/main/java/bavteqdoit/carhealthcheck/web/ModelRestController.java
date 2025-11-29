package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.dto.ModelDto;
import bavteqdoit.carhealthcheck.model.ModelType;
import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
public class ModelRestController {

    private final ModelTypeRepository modelTypeRepository;

    public ModelRestController(ModelTypeRepository modelTypeRepository) {
        this.modelTypeRepository = modelTypeRepository;
    }

    @GetMapping("/{brandId}")
    public List<ModelDto> getModelsByBrand(@PathVariable Long brandId) {
        return modelTypeRepository.findByBrandId(brandId)
                .stream()
                .map(model -> new ModelDto(model.getId(), model.getModelName()))
                .toList();
    }
}
