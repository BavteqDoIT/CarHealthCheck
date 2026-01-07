package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.model.*;
import bavteqdoit.carhealthcheck.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignCarController {

    private final BrandRepository brandRepository;
    private final ModelTypeRepository modelTypeRepository;
    private final ColorRepository colorRepository;
    private final EngineTypeRepository engineTypeRepository;
    private final BodyTypeRepository bodyTypeRepository;
    private final DriveTypeRepository driveTypeRepository;
    private final GearboxTypeRepository gearboxTypeRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final UserRepository userRepository;
    private final VinReportDataRepository vinReportDataRepository;
    private final VinReportFileRepository vinReportFileRepository;
    private final VinPdfTextService vinPdfTextService;
    private final VinPdfParserService vinPdfParserService;
    private final VinTimelineParserService vinTimelineParserService;
    private final VinMileageEntryRepository vinMileageEntryRepository;
    private final VinReportMileageService vinReportMileageService;
    private final InspectionSummaryService inspectionSummaryService;

    public DesignCarController(BrandRepository brandRepository,
                               ModelTypeRepository modelTypeRepository,
                               ColorRepository colorRepository,
                               EngineTypeRepository engineTypeRepository,
                               BodyTypeRepository bodyTypeRepository,
                               CarRepository carRepository,
                               DriveTypeRepository driveTypeRepository,
                               GearboxTypeRepository gearboxTypeRepository,
                               QuestionOptionRepository questionOptionRepository,
                               QuestionRepository questionRepository,
                               QuestionAnswerRepository questionAnswerRepository,
                               UserRepository userRepository, VinReportDataRepository vinReportDataRepository, VinReportFileRepository vinReportFileRepository, VinPdfTextService vinPdfTextService, VinPdfParserService vinPdfParserService,
                               VinTimelineParserService vinTimelineParserService,
                               VinMileageEntryRepository vinMileageEntryRepository,
                               VinReportMileageService vinReportMileageService,
                               InspectionSummaryService inspectionSummaryService) {
        this.brandRepository = brandRepository;
        this.modelTypeRepository = modelTypeRepository;
        this.colorRepository = colorRepository;
        this.engineTypeRepository = engineTypeRepository;
        this.bodyTypeRepository = bodyTypeRepository;
        this.carRepository = carRepository;
        this.driveTypeRepository = driveTypeRepository;
        this.gearboxTypeRepository = gearboxTypeRepository;
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.userRepository = userRepository;
        this.vinReportDataRepository = vinReportDataRepository;
        this.vinReportFileRepository = vinReportFileRepository;
        this.vinPdfTextService = vinPdfTextService;
        this.vinPdfParserService = vinPdfParserService;
        this.vinTimelineParserService = vinTimelineParserService;
        this.vinMileageEntryRepository = vinMileageEntryRepository;
        this.vinReportMileageService = vinReportMileageService;
        this.inspectionSummaryService = inspectionSummaryService;
    }

    @GetMapping
    public String showDesignForm(Model model) {

        loadingDataAndAddAttributes(model);

        model.addAttribute("car", new Car());

        return "design";
    }

    private final CarRepository carRepository;

    @PostMapping
    public String processDesign(@Valid Car car, Errors errors, Model model,  @AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser) {
        if (errors.hasErrors()) {

            loadingDataAndAddAttributes(model);

            return "design";
        }
        User user = userRepository.findByUsername(authUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        car.setOwner(user);
        carRepository.save(car);

        log.info("Processing car: {}", car);

        if (car.isForeignRegistered()) {
            return "redirect:/design/paint?carId=" + car.getId();
        } else {
            return "redirect:/design/raportVin?carId=" + car.getId();
        }
    }

    @GetMapping("/raportVin")
    public String showRaportVin(@RequestParam Long carId, Model model) {
        Car car = carRepository.findById(carId).orElseThrow();
        model.addAttribute("car", car);
        model.addAttribute("mileageEntries",
                vinMileageEntryRepository.findByCarIdOrderByReadingDateDescMileageKmDesc(carId));
        if (car.getFirstRegistrationDate() == null) {
            return "redirect:/design/paint?carId=" + carId;
        } else {
            vinReportDataRepository.findByCarId(carId)
                    .ifPresent(data -> model.addAttribute("vinData", data));
            vinReportFileRepository.findByCarId(carId)
                    .ifPresent(file -> model.addAttribute("reportFile", file));
            return "raportVin";
        }
    }

    @PostMapping("/raportVin/upload")
    public String uploadReport(@RequestParam Long carId,
                               @RequestParam("file")MultipartFile file){
        log.info("[VIN][ETAP1] upload start carId={}", carId);

        if(file == null){
            return "redirect:/design/raportVin?carId=" + carId;
        }

        log.info("[VIN][ETAP1] filename={}, size={}, contentType={}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        if(file.isEmpty()){
            return "redirect:/design/raportVin?carId=" + carId;
        }

        log.info("[VIN][ETAP2] saving PDF to DB...");

        Car car = carRepository.findById(carId).orElseThrow();

        VinReportFile reportFile = vinReportFileRepository.findByCarId(carId)
                .orElseGet(VinReportFile::new);

        reportFile.setCar(car);
        reportFile.setOriginalFilename(file.getOriginalFilename() == null ? "report.pdf" : file.getOriginalFilename());
        reportFile.setContentType(file.getContentType() == null ? "application/pdf" : file.getContentType());
        reportFile.setStatus(VinReportStatus.UPLOADED);
        reportFile.setUploadedAt(java.time.LocalDateTime.now());
        reportFile.setParseError(null);

        try{
            reportFile.setPdfBytes((file.getBytes()));
        } catch (IOException e){
            log.error("[VIN][ETAP2] IOException while reading bytes", e);
            return "redirect:/design/raportVin?carId=" + carId;
        }

        vinReportFileRepository.save(reportFile);

        log.info("[VIN][ETAP2] saved VinReportFile id={}, status{}", reportFile.getId(), reportFile.getStatus());
        try {
            String text = vinPdfTextService.extractText(reportFile.getPdfBytes());
            log.info("[VIN][ETAP3] extracted length={}", text.length());

            Integer year = vinPdfParserService.extractProductionYear(text);
            LocalDate firstReg = vinPdfParserService.extractFirstRegistration(text);
            log.info("[VIN][ETAP4] parsed year={}, firstReg={}", year, firstReg);

            VinReportData data = vinReportDataRepository.findByCarId(carId)
                    .orElseGet(VinReportData::new);
            data.setCar(car);
            data.setProductionYearFromReport(year);
            data.setFirstRegistrationFromReport(firstReg);
            data.setSourceReportFileId(reportFile.getId());
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
            vinReportDataRepository.save(data);

            reportFile.setStatus(VinReportStatus.PARSED_OK);
            reportFile.setParsedAt(LocalDateTime.now());
            reportFile.setParseError(null);
            vinReportFileRepository.save(reportFile);

            log.info("[VIN][ETAP5] status updated to PARSED_OK");
            log.info(
                    "[VIN][ETAP6.1] plate={}, regStatus={}, ocStatus={}, inspStatus={}, ocUntil={}, lastKm={}",
                    data.getPlateNumber(),
                    data.getRegistrationStatus(),
                    data.getOcStatus(),
                    data.getTechnicalInspectionStatus(),
                    data.getOcValidUntil(),
                    data.getLastOdometerKm()
            );

            var parsed = vinTimelineParserService.extractMileageEntries(text);
            log.info("[VIN][ETAP6.2] mileage entries parsed={}", parsed.size());

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

            int before = entriesToSave.size();

            var uniq = new java.util.LinkedHashMap<String, VinMileageEntry>();
            for (VinMileageEntry e : entriesToSave) {
                String key = e.getReadingDate() + "|" + e.getMileageKm();
                if (!uniq.containsKey(key)) {
                    uniq.put(key, e);
                } else {
                    log.warn("[VIN][ETAP6.2b] DUPLICATE mileage ignored date={}, km={}, title={}",
                            e.getReadingDate(), e.getMileageKm(), e.getEventTitle());
                }
            }

            entriesToSave = new ArrayList<>(uniq.values());
            int after = entriesToSave.size();

            log.info("[VIN][ETAP6.2b] mileage dedup before={}, after={}", before, after);

            int saved = vinReportMileageService.replaceMileageEntries(carId, car, entriesToSave);

            log.info("[VIN][ETAP6.2] saved={}", saved);

        } catch (Exception e) {
            reportFile.setStatus(VinReportStatus.PARSED_ERROR);
            reportFile.setParsedAt(LocalDateTime.now());
            reportFile.setParseError(e.getMessage());
            vinReportFileRepository.save(reportFile);

            log.error("[VIN][ETAP5] status updated to PARSED_ERROR, msg={}", e.getMessage(),e);
        }
        return "redirect:/design/raportVin?carId=" + carId;
    }

    @PostMapping("/raportVin")
    public String processRaportVin(@RequestParam Long carId) {
        Car car = carRepository.findById(carId).orElseThrow();
        carRepository.save(car);
        return "redirect:/design/paint?carId=" + car.getId();
    }

    @GetMapping("/paint")
    public String showPaintForm(@RequestParam Long carId, Model model) {
        Car car = carRepository.findById(carId).orElseThrow();

        PaintCheck paintCheck = car.getPaintCheck();
        if (paintCheck == null) {
            paintCheck = new PaintCheck();
            paintCheck.setCar(car);
            car.setPaintCheck(paintCheck);
        }

        model.addAttribute("car", car);
        model.addAttribute("paintCheck", paintCheck);

        return "paint";
    }

    @PostMapping("/paint")
    public String processPaintForm(@RequestParam Long carId,
                                   @Valid @ModelAttribute PaintCheck paintCheck,
                                   Errors errors,
                                   Model model) {

        Car car = carRepository.findById(carId).orElseThrow();

        paintCheck.setCar(car);
        paintCheck.getDamages().forEach(d -> d.setPaintCheck(paintCheck));

        car.setPaintCheck(paintCheck);

        if (errors.hasErrors()) {
            model.addAttribute("car", car);
            return "paint";
        }

        carRepository.save(car);

        return "redirect:/design/questions?carId=" + carId;
    }

    @GetMapping("/questions")
    public String showQuestionsByCategory(@RequestParam Long carId,
                                          @RequestParam(defaultValue = "consumables") String mainCategory,
                                          Model model) {

        List<Question> questions = questionRepository.findByMainCategoryOrderByIdAsc(mainCategory);

        model.addAttribute("questions", questions);
        model.addAttribute("carId", carId);
        model.addAttribute("category", mainCategory);
        Car car = carRepository.findById(carId).orElseThrow();
        model.addAttribute("car", car);

        return "questionsByCategory";
    }

    @PostMapping("/questions/save")
    public String saveCategoryResponses(@RequestParam Long carId,
                                        @RequestParam String category,
                                        @RequestParam Map<String, String> allRequestParams) {

        Car car = carRepository.findById(carId).orElseThrow();

        for (String paramName : allRequestParams.keySet()) {
            if (paramName.startsWith("questionId_")) {
                Long questionId = Long.valueOf(allRequestParams.get(paramName));

                Question question = questionRepository.findById(questionId).orElseThrow();
                QuestionAnswer answer = new QuestionAnswer();
                answer.setCar(car);
                answer.setQuestion(question);

                String optionParam = allRequestParams.get("selectedOption_" + questionId);
                if (optionParam != null && !optionParam.isEmpty()) {
                    answer.setSelectedOption(questionOptionRepository.findById(Long.valueOf(optionParam)).orElse(null));
                }

                String textValue = allRequestParams.get("answerValue_" + questionId);
                if (textValue != null) answer.setAnswerValue(textValue);

                String numericValue = allRequestParams.get("numericValue_" + questionId);
                if (numericValue != null && !numericValue.isEmpty()) {
                    answer.setNumericValue(Float.valueOf(numericValue));
                }

                questionAnswerRepository.save(answer);
            }
        }

        String next = nextCategory(category);
        if (next == null) return "redirect:/design/summary?carId=" + carId;
        return "redirect:/design/questions?carId=" + carId + "&mainCategory=" + next;
    }

    private String nextCategory(String current) {
        return switch (current) {
            case "consumables" -> "interior";
            case "interior" -> "mechanics";
            case "mechanics" -> null;
            default -> null;
        };
    }

    @GetMapping("/summary")
    public String showSummary(@RequestParam Long carId, Model model, Locale locale) {
        Car car = carRepository.findById(carId).orElseThrow();
        model.addAttribute("car", car);
        var summary = inspectionSummaryService.buildSummary(carId, locale);
        model.addAttribute("summary", summary);
        return "summary";
    }

    @PostMapping("/summary/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
        return "redirect:/";
    }

    private void loadingDataAndAddAttributes(Model model) {
        List<Brand> brands = brandRepository.findAll();
        List<ModelType> models = modelTypeRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        List<EngineType> engineTypes = engineTypeRepository.findAll();
        List<BodyType> bodyTypes = bodyTypeRepository.findAll();
        List<DriveType> driveTypes = driveTypeRepository.findAll();
        List<GearboxType> gearboxTypes = gearboxTypeRepository.findAll();

        model.addAttribute("brands", brands);
        model.addAttribute("models", models);
        model.addAttribute("colors", colors);
        model.addAttribute("engineTypes", engineTypes);
        model.addAttribute("bodyTypes", bodyTypes);
        model.addAttribute("driveTypes", driveTypes);
        model.addAttribute("gearboxTypes", gearboxTypes);
    }
}
