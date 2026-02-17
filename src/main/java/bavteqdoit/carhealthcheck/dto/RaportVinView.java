package bavteqdoit.carhealthcheck.dto;

import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.VinMileageEntry;
import bavteqdoit.carhealthcheck.model.VinReportData;
import bavteqdoit.carhealthcheck.model.VinReportFile;

import java.util.List;
import java.util.Map;

public record RaportVinView(
        Car car,
        List<VinMileageEntry> mileageEntries,
        VinReportData vinData,
        VinReportFile reportFile,
        Object validation,
        String redirectUrl
) {
    public Map<String, Object> toModelMap() {
        var map = new java.util.HashMap<String, Object>();
        map.put("car", car);
        map.put("mileageEntries", mileageEntries);
        if (vinData != null) map.put("vinData", vinData);
        if (reportFile != null) map.put("reportFile", reportFile);
        if (validation != null) map.put("validation", validation);
        return map;
    }
}
