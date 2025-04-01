package bavteqdoit.carhealthcheck.web;

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
    public List<ModelType> getModelsByBrand(@PathVariable Long brandId) {
        return modelTypeRepository.findByBrandId(brandId);
    }
}
