package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.dto.VinValidationView;
import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.ValidationField;
import bavteqdoit.carhealthcheck.model.VinMileageEntry;
import bavteqdoit.carhealthcheck.model.VinReportData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class VinReportValidationService {

    public VinValidationView build(Car car, VinReportData data, List<VinMileageEntry> mileageEntries) {
        VinValidationView view = new VinValidationView();

        Integer maxTimeline = mileageEntries == null ? null :
                mileageEntries.stream()
                        .map(VinMileageEntry::getMileageKm)
                        .filter(Objects::nonNull)
                        .max(Comparator.naturalOrder())
                        .orElse(null);

        Integer maxReportMileage = maxOf(data != null ? data.getLastOdometerKm() : null, maxTimeline);

        addVinCheck(view, car, data);

        addMileageCheck(view, car, maxReportMileage);

        addProductionYearCheck(view, car, data);

        addBrandCheck(view, car, data);

        addModelCheck(view, car, data);

        addFirstRegistrationCheck(view, car, data);

        boolean hasBlock = view.getItems().stream().anyMatch(i -> i.getSeverity() == VinValidationView.Severity.BLOCK);
        view.setHasBlockingIssues(hasBlock);

        return view;
    }

    private void addVinCheck(VinValidationView view, Car car, VinReportData data) {
        String formVin = normVin(car.getVin());
        String reportVin = normVin(data != null ? data.getVinFromReport() : null);
        if (formVin.equals(reportVin)) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.VIN,  safe(formVin), safe(reportVin), VinValidationView.Severity.OK,
                    "raport.validation.valid", VinValidationView.Resolution.USE_FORM
            ));
        } else {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.VIN,  safe(formVin), safe(reportVin), VinValidationView.Severity.BLOCK,
                    "raport.validation.vin.mismatch", VinValidationView.Resolution.USE_FORM
            ));
        }
    }

    private void addMileageCheck(VinValidationView view, Car car, Integer maxReportMileage) {
        Integer form = car.getMileage();
        if (maxReportMileage == null) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.MILEAGE,  String.valueOf(form), "-",
                    VinValidationView.Severity.WARN,
                    "raport.validation.mileage.not.found", VinValidationView.Resolution.USE_FORM
            ));
            return;
        }

        if (form < maxReportMileage) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.MILEAGE,  String.valueOf(form), String.valueOf(maxReportMileage),
                    VinValidationView.Severity.WARN,
                    "raport.validation.mileage.lower", VinValidationView.Resolution.USE_REPORT
            ));
        } else if (!Objects.equals(form, maxReportMileage)) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.MILEAGE,  String.valueOf(form), String.valueOf(maxReportMileage),
                    VinValidationView.Severity.WARN,
                    "raport.validation.mileage.mismatch", VinValidationView.Resolution.USE_FORM
            ));
        } else {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.MILEAGE,  String.valueOf(form), String.valueOf(maxReportMileage),
                    VinValidationView.Severity.OK,
                    "raport.validation.valid", VinValidationView.Resolution.USE_FORM
            ));
        }
    }

    private void addProductionYearCheck(VinValidationView view, Car car, VinReportData data) {
        Integer form = car.getProductionYear();
        Integer report = data != null ? data.getProductionYearFromReport() : null;

        if (report == null) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.PRODUCTION_YEAR,  String.valueOf(form), "-",
                    VinValidationView.Severity.WARN,
                    "raport.validation.production.not.found", VinValidationView.Resolution.USE_FORM
            ));
            return;
        }

        if (Objects.equals(form, report)) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.PRODUCTION_YEAR,  String.valueOf(form), String.valueOf(report),
                    VinValidationView.Severity.OK,
                    "raport.validation.valid", VinValidationView.Resolution.USE_FORM
            ));
        } else {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.PRODUCTION_YEAR,  String.valueOf(form), String.valueOf(report),
                    VinValidationView.Severity.WARN,
                    "raport.validation.production.mismatch", VinValidationView.Resolution.USE_REPORT
            ));
        }
    }

    private void addBrandCheck(VinValidationView view, Car car, VinReportData data) {
        String form = car.getBrand() != null ? car.getBrand().getBrandName() : null;
        String report = data != null ? data.getBrandFromReport() : null;

        if (report == null || report.isBlank()) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.BRAND,  safe(form), "-",
                    VinValidationView.Severity.WARN,
                    "raport.validation.brand.not.found", VinValidationView.Resolution.USE_FORM
            ));
            return;
        }

        if (normText(form).equals(normText(report))) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.BRAND,  safe(form), safe(report),
                    VinValidationView.Severity.OK,
                    "raport.validation.valid", VinValidationView.Resolution.USE_FORM
            ));
        } else {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.BRAND,  safe(form), safe(report),
                    VinValidationView.Severity.WARN,
                    "raport.validation.brand.mismatch", VinValidationView.Resolution.USE_REPORT
            ));
        }
    }

    private void addModelCheck(VinValidationView view, Car car, VinReportData data) {
        String form = car.getModelType() != null ? car.getModelType().getModelName() : null;
        String report = data != null ? data.getModelFromReport() : null;

        if (report == null || report.isBlank()) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.MODEL,  safe(form), "-",
                    VinValidationView.Severity.WARN,
                    "raport.validation.model.not.found", VinValidationView.Resolution.USE_FORM
            ));
            return;
        }

        if (normText(form).equals(normText(report))) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.MODEL, safe(form), safe(report),
                    VinValidationView.Severity.OK,
                    "raport.validation.valid", VinValidationView.Resolution.USE_FORM
            ));
        } else {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.MODEL,  safe(form), safe(report),
                    VinValidationView.Severity.WARN,
                    "raport.validation.model.mismatch", VinValidationView.Resolution.USE_REPORT
            ));
        }
    }

    private void addFirstRegistrationCheck(VinValidationView view, Car car, VinReportData data) {
        LocalDate form = car.getFirstRegistrationDate();
        LocalDate report = data != null ? data.getFirstRegistrationFromReport() : null;

        String formS = form != null ? form.toString() : "-";
        String repS = report != null ? report.toString() : "-";

        if (report == null) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.FIRST_REG_PL, formS, "-",
                    VinValidationView.Severity.WARN,
                    "raport.validation.reg.not.found",
                    VinValidationView.Resolution.USE_FORM
            ));
            return;
        }

        if (form == null) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.FIRST_REG_PL, "-", repS,
                    VinValidationView.Severity.WARN,
                    "raport.validation.reg.date.not.found",
                    VinValidationView.Resolution.USE_REPORT
            ));
            return;
        }

        if (form.equals(report)) {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.FIRST_REG_PL, formS, repS,
                    VinValidationView.Severity.OK,
                    "raport.validation.valid",
                    VinValidationView.Resolution.USE_FORM
            ));
        } else {
            view.getItems().add(new VinValidationView.Item(
                    ValidationField.FIRST_REG_PL, formS, repS,
                    VinValidationView.Severity.WARN,
                    "raport.validation.reg.date.mismatch",
                    VinValidationView.Resolution.USE_REPORT
            ));
        }
    }


    private Integer maxOf(Integer a, Integer b) {
        if (a == null) return b;
        if (b == null) return a;
        return Math.max(a, b);
    }

    private String normVin(String vin) {
        if (vin == null) return "";
        return vin.trim().toUpperCase();
    }

    private String normText(String s) {
        if (s == null) return "";
        return s.trim().replaceAll("\\s+", " ").toUpperCase();
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}
