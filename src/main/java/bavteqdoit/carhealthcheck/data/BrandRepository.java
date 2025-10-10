package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByBrandNameIgnoreCase(String brandName);
}
