package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.VinMileageEntryRepository;
import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.VinMileageEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VinReportMileageService {

    private final VinMileageEntryRepository vinMileageEntryRepository;

    @Transactional
    public int replaceMileageEntries(Long carId, Car car, List<VinMileageEntry> newEntries) {
        log.info("[VIN][ETAP6.2] deleting old mileage entries for carId={}", carId);
        vinMileageEntryRepository.deleteByCarId(carId);

        log.info("[VIN][ETAP6.2] saving new mileage entries count={}", newEntries.size());
        vinMileageEntryRepository.saveAll(newEntries);

        return newEntries.size();
    }
}
