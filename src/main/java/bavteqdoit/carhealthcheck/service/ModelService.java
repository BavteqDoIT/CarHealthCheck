package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.BrandRepository;
import bavteqdoit.carhealthcheck.data.EngineTypeRepository;
import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
import bavteqdoit.carhealthcheck.model.Brand;
import bavteqdoit.carhealthcheck.model.EngineType;
import bavteqdoit.carhealthcheck.model.ModelType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ModelService {

    private final ModelTypeRepository modelTypeRepository;
    private final BrandRepository brandRepository;
    private final EngineTypeRepository engineTypeRepository;

    public ModelService(ModelTypeRepository modelTypeRepository,
                        BrandRepository brandRepository,
                        EngineTypeRepository engineTypeRepository) {
        this.modelTypeRepository = modelTypeRepository;
        this.brandRepository = brandRepository;
        this.engineTypeRepository = engineTypeRepository;
    }

    public List<ModelType> findAll() {
        return modelTypeRepository.findAll();
    }

    public ModelType findById(Long id) {
        return modelTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Model not found with id: " + id));
    }

    public void deleteById(Long id) {
        modelTypeRepository.deleteById(id);
    }

    public List<ModelType> findAllSorted(String sortField, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return modelTypeRepository.findAll(sort);
    }

    @Transactional
    public void updateModel(Long id, ModelType updatedModel) {
        ModelType existingModel = modelTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Model not found with id: " + id));

        existingModel.setModelName(updatedModel.getModelName());

        if (updatedModel.getBrand() != null && updatedModel.getBrand().getId() != null) {
            Brand brand = brandRepository.findById(updatedModel.getBrand().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
            existingModel.setBrand(brand);
        }

        if (updatedModel.getEngineTypes() != null && !updatedModel.getEngineTypes().isEmpty()) {
            List<EngineType> engines = engineTypeRepository.findAllById(
                    updatedModel.getEngineTypes().stream()
                            .map(EngineType::getId)
                            .toList()
            );
            existingModel.setEngineTypes(new HashSet<>(engines));
        } else {
            existingModel.getEngineTypes().clear();
        }

        modelTypeRepository.save(existingModel);
    }

    @Transactional
    public void addModel(ModelType newModel) {
        if (newModel.getBrand() != null && newModel.getBrand().getId() != null) {
            Brand brand = brandRepository.findById(newModel.getBrand().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
            newModel.setBrand(brand);
        }

        if (newModel.getEngineTypes() != null && !newModel.getEngineTypes().isEmpty()) {
            Set<EngineType> engines = new HashSet<>(engineTypeRepository.findAllById(
                    newModel.getEngineTypes().stream()
                            .map(EngineType::getId)
                            .toList()
            ));
            newModel.setEngineTypes(engines);
        }

        modelTypeRepository.save(newModel);
    }
}
