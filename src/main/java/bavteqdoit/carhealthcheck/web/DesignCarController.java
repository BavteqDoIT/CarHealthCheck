package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.model.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public DesignCarController(BrandRepository brandRepository,
                               ModelTypeRepository modelTypeRepository,
                               ColorRepository colorRepository,
                               EngineTypeRepository engineTypeRepository,
                               BodyTypeRepository bodyTypeRepository,
                               CarRepository carRepository,
                               DriveTypeRepository driveTypeRepository,
                               GearboxTypeRepository gearboxTypeRepository) {
        this.brandRepository = brandRepository;
        this.modelTypeRepository = modelTypeRepository;
        this.colorRepository = colorRepository;
        this.engineTypeRepository = engineTypeRepository;
        this.bodyTypeRepository = bodyTypeRepository;
        this.carRepository = carRepository;
        this.driveTypeRepository = driveTypeRepository;
        this.gearboxTypeRepository = gearboxTypeRepository;
    }

    @GetMapping
    public String showDesignForm(Model model) {

        loadingDataAndAddAttributes(model);

        model.addAttribute("car", new Car());

        return "design";
    }

    private final CarRepository carRepository;

    @PostMapping
    public String processDesign(@Valid Car car, Errors errors, Model model) {
        if (errors.hasErrors()) {

            loadingDataAndAddAttributes(model);

            return "design";
        }

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
        if (car.getFirstRegistrationDate() == null) {
            return "redirect:/design/paint?carId=" + carId;
        } else {
            return "raportVin";
        }
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
                                   @ModelAttribute PaintCheck paintCheck) {
        Car car = carRepository.findById(carId).orElseThrow();

        paintCheck.setCar(car);
        paintCheck.getDamages().forEach(d -> d.setPaintCheck(paintCheck));

        car.setPaintCheck(paintCheck);
        carRepository.save(car);

        return "redirect:/design/nextStep?carId=" + carId;
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
