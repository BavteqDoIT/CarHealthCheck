package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VinReportUploadService {

    private final CarRepository carRepository;
    private final VinReportFileRepository vinReportFileRepository;
    private final VinReportDataRepository vinReportDataRepository;

    private final VinPdfTextService vinPdfTextService;
    private final VinPdfParserService vinPdfParserService;
    private final VinTimelineParserService vinTimelineParserService;
    private final VinReportMileageService vinReportMileageService;

    @Transactional
    public UploadResult uploadAndParse(Long carId, MultipartFile file) {
        log.info("[VIN][UPLOAD] start carId={}", carId);

        if (file == null || file.isEmpty()) {
            return UploadResult.fail("Plik jest pusty.");
        }

        Car car = carRepository.findById(carId).orElseThrow();

        VinReportFile reportFile = vinReportFileRepository.findByCarId(carId)
                .orElseGet(VinReportFile::new);

        reportFile.setCar(car);
        reportFile.setOriginalFilename(
                file.getOriginalFilename() == null ? "report.pdf" : file.getOriginalFilename()
        );
        reportFile.setContentType(
                file.getContentType() == null ? "application/pdf" : file.getContentType()
        );
        reportFile.setStatus(VinReportStatus.UPLOADED);
        reportFile.setUploadedAt(LocalDateTime.now());
        reportFile.setParseError(null);

        try {
            reportFile.setPdfBytes(file.getBytes());
        } catch (IOException e) {
            log.error("[VIN][UPLOAD] cannot read bytes", e);
            return UploadResult.fail("Nie udało się odczytać pliku.");
        }

        vinReportFileRepository.save(reportFile);

        try {
            String text = vinPdfTextService.extractText(reportFile.getPdfBytes());
            log.info("[VIN][PARSE] extracted length={}", text.length());

            VinReportData data = vinReportDataRepository.findByCarId(carId)
                    .orElseGet(VinReportData::new);

            fillVinDataFromText(data, car, reportFile.getId(), text);
            vinReportDataRepository.save(data);

            reportFile.setStatus(VinReportStatus.PARSED_OK);
            reportFile.setParsedAt(LocalDateTime.now());
            reportFile.setParseError(null);
            vinReportFileRepository.save(reportFile);

            int savedMileage = parseAndReplaceMileage(carId, car, text);

            log.info("[VIN][DONE] parsed ok, mileageSaved={}", savedMileage);
            return UploadResult.ok(savedMileage);

        } catch (Exception e) {
            reportFile.setStatus(VinReportStatus.PARSED_ERROR);
            reportFile.setParsedAt(LocalDateTime.now());
            reportFile.setParseError(safeMsg(e));
            vinReportFileRepository.save(reportFile);

            log.error("[VIN][PARSE] error msg={}", e.getMessage(), e);
            return UploadResult.fail("Błąd parsowania raportu: " + safeMsg(e));
        }
    }

    private void fillVinDataFromText(VinReportData data,
                                     Car car,
                                     Long reportFileId,
                                     String text) {

        data.setCar(car);
        data.setSourceReportFileId(reportFileId);

        data.setProductionYearFromReport(vinPdfParserService.extractProductionYear(text));
        data.setFirstRegistrationFromReport(vinPdfParserService.extractFirstRegistration(text));
        data.setPlateNumber(vinPdfParserService.extractPlateNumber(text));

        data.setRegistrationStatus(vinPdfParserService.extractRegistrationStatusEnum(text));
        data.setOcStatus(vinPdfParserService.extractOcStatusEnum(text));
        data.setTechnicalInspectionStatus(vinPdfParserService.extractTechnicalInspectionStatusEnum(text));
        data.setOcValidUntil(vinPdfParserService.extractOcValidUntil(text));
        data.setLastOdometerKm(vinPdfParserService.extractLastOdometerKm(text));

        data.setTheft(vinPdfParserService.extractTheftRisk(text));
        data.setScrapped(vinPdfParserService.extractScrappedRisk(text));
        data.setAccident(vinPdfParserService.extractAccidentRisk(text));
        data.setDamaged(vinPdfParserService.extractDamagedRisk(text));
        data.setOdometerMismatch(vinPdfParserService.extractOdometerMismatchRisk(text));
        data.setNotRoadworthy(vinPdfParserService.extractNotRoadworthyRisk(text));
        data.setTaxi(vinPdfParserService.extractTaxiRisk(text));
        data.setTotalLoss(vinPdfParserService.extractTotalLossRisk(text));
        data.setVinChecksumError(vinPdfParserService.extractVinChecksumErrorRisk(text));
        data.setServiceActions(vinPdfParserService.extractServiceActionsRisk(text));

        String vinFromReport = vinPdfParserService.extractVinFromReport(text);
        data.setVinFromReport(vinFromReport);

        var bm = vinPdfParserService.extractBrandAndModel(text);
        if (bm != null) {
            data.setBrandFromReport(bm.brand());
            data.setModelFromReport(bm.model());
        }
    }

    private int parseAndReplaceMileage(Long carId, Car car, String text) {
        var parsed = vinTimelineParserService.extractMileageEntries(text);
        log.info("[VIN][MILEAGE] parsed={}", parsed.size());

        List<VinMileageEntry> entriesToSave = new ArrayList<>();
        for (var pm : parsed) {
            VinMileageEntry e = new VinMileageEntry();
            e.setCar(car);
            e.setReadingDate(pm.readingDate());
            e.setMileageKm(pm.mileageKm());
            e.setSource(pm.source());
            e.setEventTitle(pm.eventTitle());
            entriesToSave.add(e);
        }

        var uniq = new LinkedHashMap<String, VinMileageEntry>();
        for (VinMileageEntry e : entriesToSave) {
            String key = e.getReadingDate() + "|" + e.getMileageKm();
            uniq.putIfAbsent(key, e);
        }

        entriesToSave = new ArrayList<>(uniq.values());
        return vinReportMileageService.replaceMileageEntries(carId, car, entriesToSave);
    }

    private String safeMsg(Exception e) {
        if (e == null || e.getMessage() == null) return "unknown";
        return e.getMessage().length() > 400 ? e.getMessage().substring(0, 400) : e.getMessage();
    }

    public record UploadResult(boolean ok, String message, Integer mileageSaved) {
        public static UploadResult ok(int mileageSaved) {
            return new UploadResult(true, "Raport VIN wczytany poprawnie.", mileageSaved);
        }
        public static UploadResult fail(String msg) {
            return new UploadResult(false, msg, null);
        }
    }
}
