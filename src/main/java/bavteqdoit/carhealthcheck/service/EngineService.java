package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.EngineTypeRepository;
import bavteqdoit.carhealthcheck.model.EngineType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EngineService {
    private final EngineTypeRepository engineTypeRepository;
    public EngineService( EngineTypeRepository engineTypeRepository) {
        this.engineTypeRepository = engineTypeRepository;
    }

    public EngineType findById(Long id) {
        return engineTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("EngineType not found for id: " + id));
    }

    public List<EngineType> findAll() {
        return engineTypeRepository.findAll();
    }

    public  EngineType save(EngineType engineType) {
        return engineTypeRepository.save(engineType);
    }

    public void delete(EngineType engineType) {
        engineTypeRepository.delete(engineType);
    }

    @Transactional
    public void deleteIfUnused(Long id) {

        EngineType engine = engineTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono silnika o id: " + id));

        if (engine.getModelTypes() != null && !engine.getModelTypes().isEmpty()) {
            throw new IllegalStateException("Nie można usunąć silnika, który jest używany w modelach.");
        }

        engineTypeRepository.delete(engine);
    }
}
