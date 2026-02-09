package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModelTypeRepository extends JpaRepository<ModelType, Long> {
    List<ModelType> findByBrandId(Long brandId);

    Optional<ModelType> findByModelNameIgnoreCaseAndBrandId(String modelName, Long brandId);
}
