package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.VinMileageEntryRepository;
import bavteqdoit.carhealthcheck.data.VinReportDataRepository;
import bavteqdoit.carhealthcheck.data.VinReportFileRepository;
import bavteqdoit.carhealthcheck.dto.RaportVinView;
import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.VinReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VinReportViewService {

    private final CarService carService;

    private final VinMileageEntryRepository vinMileageEntryRepository;
    private final VinReportDataRepository vinReportDataRepository;
    private final VinReportFileRepository vinReportFileRepository;
    private final VinReportValidationService vinReportValidationService;

    @Transactional(readOnly = true)
    public RaportVinView build(Long carId, String username) {

        Car car = carService.getUserCar(carId, username);

        if (car.getFirstRegistrationDate() == null) {
            return new RaportVinView(car, java.util.List.of(), null, null, null,
                    "/design/paint?carId=" + carId);
        }

        var entries = vinMileageEntryRepository
                .findByCarIdOrderByReadingDateDescMileageKmDesc(carId);

        var vinDataOpt = vinReportDataRepository.findByCarId(carId);
        var reportFileOpt = vinReportFileRepository.findByCarId(carId);

        Object validation = null;

        if (reportFileOpt.isPresent()
                && reportFileOpt.get().getStatus() == VinReportStatus.PARSED_OK
                && vinDataOpt.isPresent()) {

            validation = vinReportValidationService.build(car, vinDataOpt.get(), entries);
        }

        return new RaportVinView(
                car,
                entries,
                vinDataOpt.orElse(null),
                reportFileOpt.orElse(null),
                validation,
                null
        );
    }
}
