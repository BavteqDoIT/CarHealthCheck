package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.dto.VinResolveForm;
import bavteqdoit.carhealthcheck.model.*;
import bavteqdoit.carhealthcheck.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/design")
public class DesignCarController {

    private final BrandRepository brandRepository;
    private final ModelTypeRepository modelTypeRepository;
    private final ColorRepository colorRepository;
    private final EngineTypeRepository engineTypeRepository;
    private final BodyTypeRepository bodyTypeRepository;
    private final DriveTypeRepository driveTypeRepository;
    private final GearboxTypeRepository gearboxTypeRepository;
    private final QuestionRepository questionRepository;
    private final InspectionSummaryService inspectionSummaryService;
    private final VinReportUploadService vinReportUploadService;
    private final VinReportApplyService vinReportApplyService;
    private final VinReportViewService vinReportViewService;
    private final CarService carService;
    private final VinReportFlowService vinReportFlowService;
    private final PaintCheckService paintCheckService;
    private final QuestionAnswerService questionAnswerService;
    private final CarRepository carRepository;

    @GetMapping
    public String showDesignForm(Model model) {
        model.addAttribute("car", new Car());

        return "design";
    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute("car") Car car,
                                Errors errors,
                                @AuthenticationPrincipal org.springframework.security.core.userdetails.User authUser) {
        if (errors.hasErrors()) {
            return "design";
        }

        Car saved = carService.createForUsername(car, authUser.getUsername());

        return saved.isForeignRegistered()
                ? "redirect:/design/paint?carId=" + saved.getId()
                : "redirect:/design/raportVin?carId=" + saved.getId();
    }

    @GetMapping("/raportVin")
    public String showRaportVin(@RequestParam Long carId,
                                Model model,
                                Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        var view = vinReportViewService.build(carId, principal.getName());

        if (view.redirectUrl() != null) {
            return "redirect:" + view.redirectUrl();
        }

        model.addAllAttributes(view.toModelMap());
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
    public String processRaportVin(@RequestParam Long carId,
                                   RedirectAttributes ra,
                                   Principal principal) {

        if (principal == null) return "redirect:/login";

        String msg = vinReportFlowService.blockingMessage(carId, principal.getName());
        if (msg != null) {
            ra.addFlashAttribute("errorMessage", msg);
            return "redirect:/design/raportVin?carId=" + carId;
        }
        return "redirect:/design/paint?carId=" + carId;
    }

    @GetMapping("/paint")
    public String showPaintForm(@RequestParam Long carId,
                                Model model,
                                Principal principal) {

        if (principal == null) return "redirect:/login";

        var data = paintCheckService.preparePaintForm(carId, principal.getName());

        model.addAttribute("car", data.car());
        model.addAttribute("paintCheck", data.paintCheck());

        return "paint";
    }


    @PostMapping("/paint")
    public String processPaintForm(@RequestParam Long carId,
                                   @Valid @ModelAttribute PaintCheck paintCheck,
                                   Errors errors,
                                   Model model,
                                   Principal principal) {

        if (principal == null) return "redirect:/login";

        if (errors.hasErrors()) {
            model.addAttribute("car", carService.getUserCar(carId, principal.getName()));
            return "paint";
        }

        paintCheckService.savePaintCheck(carId, principal.getName(), paintCheck);

        return "redirect:/design/questions?carId=" + carId;
    }

    @GetMapping("/questions")
    public String showQuestionsByCategory(@RequestParam Long carId,
                                          @RequestParam(defaultValue = "consumables") String mainCategory,
                                          Model model,
                                          Principal principal) {

        if (principal == null) return "redirect:/login";

        List<Question> questions = questionRepository.findByMainCategoryOrderByIdAsc(mainCategory);

        model.addAttribute("questions", questions);
        model.addAttribute("carId", carId);
        model.addAttribute("category", mainCategory);

        Car car = carService.getUserCar(carId, principal.getName());
        model.addAttribute("car", car);

        return "questionsByCategory";
    }

    @PostMapping("/questions/save")
    public String saveCategoryResponses(@RequestParam Long carId,
                                        @RequestParam String category,
                                        @RequestParam Map<String, String> allRequestParams,
                                        HttpServletRequest request) {

        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");

        questionAnswerService.saveCategoryResponses(carId, allRequestParams, isAdmin);

        String next = nextCategory(category);
        if (next == null) {
            return "redirect:/design/summary?carId=" + carId;
        }
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
    public String showSummary(@RequestParam Long carId, Model model, Locale locale, Principal principal) {
        if (principal == null) return "redirect:/login";

        Car car = carService.getUserCar(carId, principal.getName());
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

    @ModelAttribute
    public void commonData(Model model) {
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("models", modelTypeRepository.findAll());
        model.addAttribute("colors", colorRepository.findAll());
        model.addAttribute("engineTypes", engineTypeRepository.findAll());
        model.addAttribute("bodyTypes", bodyTypeRepository.findAll());
        model.addAttribute("driveTypes", driveTypeRepository.findAll());
        model.addAttribute("gearboxTypes", gearboxTypeRepository.findAll());
    }
}
