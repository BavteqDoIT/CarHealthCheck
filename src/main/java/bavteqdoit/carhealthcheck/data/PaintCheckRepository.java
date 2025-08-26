package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.PaintCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintCheckRepository extends JpaRepository<PaintCheck, Long> {
}
