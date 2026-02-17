package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.VinMileageEntryRepository;
import bavteqdoit.carhealthcheck.data.VinReportDataRepository;
import bavteqdoit.carhealthcheck.dto.VinValidationView; // jeśli masz, jeśli nie to usuń
import bavteqdoit.carhealthcheck.model.VinMileageEntry;
import bavteqdoit.carhealthcheck.model.VinReportData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VinReportFlowService {

    private final CarService carService;
    private final VinReportDataRepository vinReportDataRepository;
    private final VinMileageEntryRepository vinMileageEntryRepository;
    private final VinReportValidationService vinReportValidationService;


    @Transactional(readOnly = true)
    public String blockingMessage(Long carId, String username) {
        var car = carService.getUserCar(carId, username);

        var data = vinReportDataRepository.findByCarId(carId).orElse(null);
        var entries = vinMileageEntryRepository.findByCarIdOrderByReadingDateDescMileageKmDesc(carId);

        if (data != null) {
            var validation = vinReportValidationService.build(car, data, entries);
            if (validation.isHasBlockingIssues()) {
                return "Najpierw rozwiąż krytyczne niezgodności w raporcie VIN.";
            }
        }
        return null;
    }
}
