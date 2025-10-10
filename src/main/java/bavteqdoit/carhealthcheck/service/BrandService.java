package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.BrandRepository;
import bavteqdoit.carhealthcheck.model.Brand;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

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

    public Brand add(Brand brand) {
        if (brandRepository.findByBrandNameIgnoreCase(brand.getBrandName()).isPresent()) {
            throw new EntityExistsException("Brand with name '" + brand.getBrandName() + "' already exists");
        } else {
            return brandRepository.save(brand);
        }
    }

    public void update(Brand brand) {
        if (!brandRepository.existsById(brand.getId())) {
            throw new EntityNotFoundException("Brand with ID " + brand.getId() + " not found");
        }
        brand.setBrandName(brand.getBrandName());
        brandRepository.save(brand);

    }


    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}
