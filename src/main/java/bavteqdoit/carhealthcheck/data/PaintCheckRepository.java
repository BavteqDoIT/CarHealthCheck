package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.PaintCheck;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaintCheckRepository extends JpaRepository<PaintCheck, Long> {

    @EntityGraph(attributePaths = "damages")
    Optional<PaintCheck> findByCarId(Long carId);
}
