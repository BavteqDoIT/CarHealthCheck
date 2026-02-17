package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.dto.VinResolveForm;
import bavteqdoit.carhealthcheck.dto.VinValidationView;
import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.VinMileageEntry;
import bavteqdoit.carhealthcheck.model.VinReportData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VinReportApplyService {

    private final BrandRepository brandRepository;
    private final ModelTypeRepository modelTypeRepository;

    private final CarRepository carRepository;
    private final VinReportDataRepository vinReportDataRepository;
    private final VinMileageEntryRepository vinMileageEntryRepository;

    @Transactional
    public void apply(Long carId, VinResolveForm form) {
        Car car = carRepository.findById(carId).orElseThrow();
        VinReportData data = vinReportDataRepository.findByCarId(carId).orElseThrow();

        List<VinMileageEntry> mileageEntries =
                vinMileageEntryRepository.findByCarIdOrderByReadingDateDescMileageKmDesc(carId);

        apply(car, data, mileageEntries, form);

        carRepository.save(car);
    }

    public void apply(Car car,
                      VinReportData data,
                      List<VinMileageEntry> mileageEntries,
                      VinResolveForm form) {

        applyBrandAndModel(car, data, form);
        applyProductionYear(car, data, form);
        applyMileage(car, data, mileageEntries, form);
        applyVin(car, data, form);
        applyFirstRegistration(car, data, form);
    }

    private void applyBrandAndModel(Car car, VinReportData data, VinResolveForm form) {
        var brandRes = form.getResolution().get("BRAND");

        if (brandRes == VinValidationView.Resolution.USE_REPORT
                && data.getBrandFromReport() != null) {

            brandRepository
                    .findByBrandNameIgnoreCase(data.getBrandFromReport().trim())
                    .ifPresent(car::setBrand);
        }

        var modelRes = form.getResolution().get("MODEL");

        if (modelRes == VinValidationView.Resolution.USE_REPORT
                && data.getModelFromReport() != null
                && car.getBrand() != null) {

            modelTypeRepository
                    .findByModelNameIgnoreCaseAndBrandId(
                            data.getModelFromReport().trim(),
                            car.getBrand().getId()
                    )
                    .ifPresent(car::setModelType);
        }
    }

    private void applyProductionYear(Car car, VinReportData data, VinResolveForm form) {
        var res = form.getResolution().get("PRODUCTION_YEAR");

        if (res == VinValidationView.Resolution.USE_REPORT
                && data.getProductionYearFromReport() != null) {
            car.setProductionYear(data.getProductionYearFromReport());
        }
    }

    private void applyMileage(Car car,
                              VinReportData data,
                              List<VinMileageEntry> entries,
                              VinResolveForm form) {

        var res = form.getResolution().get("MILEAGE");
        if (res == null) return;

        Integer maxTimeline = entries.stream()
                .map(VinMileageEntry::getMileageKm)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(null);

        Integer maxReport = max(data.getLastOdometerKm(), maxTimeline);

        if (res == VinValidationView.Resolution.USE_REPORT && maxReport != null) {
            car.setMileage(maxReport);
        }
    }

    private void applyVin(Car car, VinReportData data, VinResolveForm form) {
        var res = form.getResolution().get("VIN");

        if (res == VinValidationView.Resolution.USE_REPORT
                && data.getVinFromReport() != null) {
            car.setVin(data.getVinFromReport());
        }
    }

    private Integer max(Integer a, Integer b) {
        if (a == null) return b;
        if (b == null) return a;
        return Math.max(a, b);
    }

    private void applyFirstRegistration(Car car, VinReportData data, VinResolveForm form) {
        var res = form.getResolution().get("FIRST_REG_PL");

        if (res == VinValidationView.Resolution.USE_REPORT && data.getFirstRegistrationFromReport() != null) {
            car.setFirstRegistrationDate(data.getFirstRegistrationFromReport());
        } else if (res == VinValidationView.Resolution.MANUAL) {
            String v = form.getManualValue().get("FIRST_REG_PL");
            if (v != null) car.setFirstRegistrationDate(LocalDate.parse(v));
        }
    }
}
