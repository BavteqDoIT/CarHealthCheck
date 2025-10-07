package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.FuelTypeRepository;
import bavteqdoit.carhealthcheck.model.FuelType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuelService {
    private final FuelTypeRepository fuelTypeRepository;

    public FuelService(FuelTypeRepository fuelTypeRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
    }

    public List<FuelType> findAll() {
        return fuelTypeRepository.findAll();
    }

    public FuelType findById(Long id) {
        return fuelTypeRepository.findById(id).get();
    }
}
