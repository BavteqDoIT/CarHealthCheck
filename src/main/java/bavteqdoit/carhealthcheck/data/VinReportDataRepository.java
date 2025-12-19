package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.VinReportData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VinReportDataRepository extends JpaRepository<VinReportData, Long> {
    Optional<VinReportData> findByCarId(Long carId);
}
