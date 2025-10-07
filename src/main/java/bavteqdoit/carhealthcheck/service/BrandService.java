package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.BrandRepository;
import bavteqdoit.carhealthcheck.model.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    private BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(Long id) {
        return brandRepository.findById(id).get();
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}
