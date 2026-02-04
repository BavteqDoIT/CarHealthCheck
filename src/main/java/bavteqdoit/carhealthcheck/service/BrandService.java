package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.BrandRepository;
import bavteqdoit.carhealthcheck.model.Brand;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Brand with ID " + id + " not found"));
    }

    public Brand add(Brand brand) {
        if (brandRepository.findByBrandNameIgnoreCase(brand.getBrandName()).isPresent()) {
            throw new EntityExistsException("Brand with name '" + brand.getBrandName() + "' already exists");
        } else {
            return brandRepository.save(brand);
        }
    }

    @Transactional
    public void update(Brand brand) {
        Brand existing = brandRepository.findById(brand.getId())
                .orElseThrow(() -> new EntityNotFoundException("admin.editBrand.error"));

        String newName = brand.getBrandName();
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("admin.updateBrand.emptyName");
        }
        newName = newName.trim();

        if (brandRepository.existsByBrandNameIgnoreCaseAndIdNot(newName, brand.getId())) {
            throw new EntityExistsException("admin.updateBrand.exists");
        }

        existing.setBrandName(newName);
        brandRepository.save(existing);
    }


    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}
