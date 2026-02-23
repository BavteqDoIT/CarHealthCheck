package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.EngineTypeRepository;
import bavteqdoit.carhealthcheck.dto.EngineTypeForm;
import bavteqdoit.carhealthcheck.model.EngineType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EngineService {
    private final EngineTypeRepository engineTypeRepository;
    private final BrandService brandService;
    private final FuelService fuelService;

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

    @Transactional
    public void createFromForm(EngineTypeForm form) {
        EngineType e = new EngineType();
        applyForm(e, form);
        engineTypeRepository.save(e);
    }

    @Transactional
    public void updateFromForm(EngineTypeForm form) {
        EngineType e = engineTypeRepository.findById(form.getId())
                .orElseThrow(() -> new EntityNotFoundException("Engine not found: " + form.getId()));
        applyForm(e, form);
        engineTypeRepository.save(e);
    }

    private void applyForm(EngineType e, EngineTypeForm form) {
        e.setBrand(brandService.findById(form.getBrandId()));
        e.setFuelType(fuelService.findById(form.getFuelTypeId()));
        e.setName(form.getName());
        e.setCapacity(form.getCapacity());
        e.setHorsepowerHp(form.getHorsepowerHp());
        e.setPowerKw(form.getPowerKw());
    }
}
