package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
