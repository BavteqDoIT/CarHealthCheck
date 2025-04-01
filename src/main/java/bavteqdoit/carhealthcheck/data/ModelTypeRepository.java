package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelTypeRepository extends JpaRepository<ModelType, Long> {
    List<ModelType> findByBrandId(Long brandId);
}
