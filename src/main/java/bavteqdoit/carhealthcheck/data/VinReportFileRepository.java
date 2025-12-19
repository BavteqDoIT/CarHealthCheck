package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.VinReportFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VinReportFileRepository extends JpaRepository<VinReportFile, Long> {
    Optional<VinReportFile> findByCarId(Long carId);
}
