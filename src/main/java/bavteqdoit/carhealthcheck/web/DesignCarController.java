package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.model.Car;
import bavteqdoit.carhealthcheck.model.Brand;
import bavteqdoit.carhealthcheck.model.ModelType;
import bavteqdoit.carhealthcheck.model.Color;
import bavteqdoit.carhealthcheck.model.EngineType;
import bavteqdoit.carhealthcheck.model.BodyType;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    public DesignCarController(BrandRepository brandRepository,
                               ModelTypeRepository modelTypeRepository,
                               ColorRepository colorRepository,
                               EngineTypeRepository engineTypeRepository,
                               BodyTypeRepository bodyTypeRepository, CarRepository carRepository) {
        this.brandRepository = brandRepository;
        this.modelTypeRepository = modelTypeRepository;
        this.colorRepository = colorRepository;
        this.engineTypeRepository = engineTypeRepository;
        this.bodyTypeRepository = bodyTypeRepository;
        this.carRepository = carRepository;
    }

    @GetMapping
    public String showDesignForm(Model model) {

        loadingDataAndAddAttributes(model);

        model.addAttribute("car", new Car());

        return "design";
    }

    private final CarRepository carRepository; // Repozytorium do zapisu obiektów Car

    @PostMapping
    public String processDesign(@Valid Car car, Errors errors, Model model) {
        if (errors.hasErrors()) {

            loadingDataAndAddAttributes(model);

            return "design";
        }

        // Zapisanie samochodu w bazie danych
        carRepository.save(car);

        log.info("Processing car: {}", car);

        return "redirect:/";
    }

    private void loadingDataAndAddAttributes(Model model) {
        List<Brand> brands = brandRepository.findAll();
        List<ModelType> models = modelTypeRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        List<EngineType> engineTypes = engineTypeRepository.findAll();
        List<BodyType> bodyTypes = bodyTypeRepository.findAll();

        model.addAttribute("brands", brands);
        model.addAttribute("models", models);
        model.addAttribute("colors", colors);
        model.addAttribute("engineTypes", engineTypes);
        model.addAttribute("bodyTypes", bodyTypes);
    }
}
