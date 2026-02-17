package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.dto.VinResolveForm;
import bavteqdoit.carhealthcheck.model.*;
import bavteqdoit.carhealthcheck.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

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
    private final VinMileageEntryRepository vinMileageEntryRepository;
    private final InspectionSummaryService inspectionSummaryService;
    private final VinReportValidationService vinReportValidationService;
    private final VinReportUploadService vinReportUploadService;
    private final VinReportApplyService vinReportApplyService;

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
                               UserRepository userRepository,
                               VinReportDataRepository vinReportDataRepository,
                               VinReportFileRepository vinReportFileRepository,
                               VinMileageEntryRepository vinMileageEntryRepository,
                               InspectionSummaryService inspectionSummaryService,
                               VinReportValidationService vinReportValidationService,
                               VinReportUploadService vinReportUploadService,
                               VinReportApplyService vinReportApplyService) {
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
        this.vinMileageEntryRepository = vinMileageEntryRepository;
        this.inspectionSummaryService = inspectionSummaryService;
        this.vinReportValidationService = vinReportValidationService;
        this.vinReportUploadService = vinReportUploadService;
        this.vinReportApplyService = vinReportApplyService;
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

        var entries = vinMileageEntryRepository.findByCarIdOrderByReadingDateDescMileageKmDesc(carId);
        model.addAttribute("mileageEntries", entries);

        if (car.getFirstRegistrationDate() == null) {
            return "redirect:/design/paint?carId=" + carId;
        }

        var vinDataOpt = vinReportDataRepository.findByCarId(carId);
        vinDataOpt.ifPresent(data -> model.addAttribute("vinData", data));

        var reportFileOpt = vinReportFileRepository.findByCarId(carId);
        reportFileOpt.ifPresent(file -> model.addAttribute("reportFile", file));

        if (reportFileOpt.isPresent()
                && reportFileOpt.get().getStatus() == VinReportStatus.PARSED_OK
                && vinDataOpt.isPresent()) {

            var validation = vinReportValidationService.build(car, vinDataOpt.get(), entries);
            model.addAttribute("validation", validation);
        }

        return "raportVin";
    }

    @PostMapping("/raportVin/upload")
    public String uploadReport(@RequestParam Long carId,
                               @RequestParam("file") MultipartFile file,
                               RedirectAttributes ra) {

        var result = vinReportUploadService.uploadAndParse(carId, file);

        if (result.ok()) {
            ra.addFlashAttribute("successMessage", result.message());
        } else {
            ra.addFlashAttribute("errorMessage", result.message());
        }

        return "redirect:/design/raportVin?carId=" + carId;
    }


    @PostMapping("/raportVin/apply")
    public String apply(@RequestParam Long carId,
                        @ModelAttribute VinResolveForm form,
                        RedirectAttributes ra) {

        try {
            vinReportApplyService.apply(carId, form);
            ra.addFlashAttribute("successMessage", "Dane z raportu VIN zostały zastosowane.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Nie udało się zastosować danych: " + e.getMessage());
        }

        return "redirect:/design/raportVin?carId=" + carId;
    }

    @PostMapping("/raportVin")
    public String processRaportVin(@RequestParam Long carId, RedirectAttributes ra) {
        Car car = carRepository.findById(carId).orElseThrow();

        VinReportData data = vinReportDataRepository.findByCarId(carId).orElse(null);
        List<VinMileageEntry> entries =
                vinMileageEntryRepository.findByCarIdOrderByReadingDateDescMileageKmDesc(carId);

        if (data != null) {
            var validation = vinReportValidationService.build(car, data, entries);
            if (validation.isHasBlockingIssues()) {
                ra.addFlashAttribute("errorMessage", "Najpierw rozwiąż krytyczne niezgodności w raporcie VIN.");
                return "redirect:/design/raportVin?carId=" + carId;
            }
        }

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
        model.addAttribute("viewMode", false);
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
