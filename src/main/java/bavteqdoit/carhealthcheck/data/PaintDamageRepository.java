package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.PaintDamage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintDamageRepository extends JpaRepository<PaintDamage, Long> {
}
