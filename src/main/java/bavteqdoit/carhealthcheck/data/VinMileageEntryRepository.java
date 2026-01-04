package bavteqdoit.carhealthcheck.data;

import bavteqdoit.carhealthcheck.model.VinMileageEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VinMileageEntryRepository extends JpaRepository<VinMileageEntry, Long> {
    List<VinMileageEntry> findByCarIdOrderByReadingDateDescMileageKmDesc(Long carId);
    void deleteByCarId(Long carId);
}
