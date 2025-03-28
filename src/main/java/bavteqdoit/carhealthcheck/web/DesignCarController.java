package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.Car;
import bavteqdoit.carhealthcheck.CarAttribute;
import bavteqdoit.carhealthcheck.CarAttribute.Type;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignCarController {

    @GetMapping
    public String showDesignForm(Model model) {
        List<CarAttribute> carAttributes = Arrays.asList(
                new CarAttribute(1000L, "Skoda", Type.BRAND),
                new CarAttribute(1001L, "Mercedes", Type.BRAND),
                new CarAttribute(1002L, "BMW", Type.BRAND),
                new CarAttribute(1003L, "Minicooper", Type.BRAND),
                new CarAttribute(1004L, "Audi", Type.BRAND),
                new CarAttribute(1005L, "Opel", Type.BRAND),
                new CarAttribute(2000L, "Astra", Type.MODEL),
                new CarAttribute(2001L, "Corsa", Type.MODEL),
                new CarAttribute(2002L, "Mokka", Type.MODEL),
                new CarAttribute(2003L, "Fabia", Type.MODEL),
                new CarAttribute(2004L, "Octavia", Type.MODEL),
                new CarAttribute(3000L, "Blue", Type.COLOR),
                new CarAttribute(3001L, "Green", Type.COLOR),
                new CarAttribute(3002L, "Yellow", Type.COLOR),
                new CarAttribute(4000L, "Petrol", Type.ENGINE_TYPE),
                new CarAttribute(4001L, "Diesel", Type.ENGINE_TYPE),
                new CarAttribute(4002L, "Electric", Type.ENGINE_TYPE),
                new CarAttribute(6000L, "Sedan", Type.BODY_TYPE),
                new CarAttribute(6001L, "Hatchback", Type.BODY_TYPE),
                new CarAttribute(6002L, "SUV", Type.BODY_TYPE)
        );

        Type[] types = CarAttribute.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(carAttributes, type));
        }

        model.addAttribute("car", new Car());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Car car, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Type[] types = CarAttribute.Type.values();
            List<CarAttribute> carAttributes = Arrays.asList(
                    new CarAttribute(1000L, "Skoda", Type.BRAND),
                    new CarAttribute(1001L, "Mercedes", Type.BRAND),
                    new CarAttribute(1002L, "BMW", Type.BRAND),
                    new CarAttribute(1003L, "Minicooper", Type.BRAND),
                    new CarAttribute(1004L, "Audi", Type.BRAND),
                    new CarAttribute(1005L, "Opel", Type.BRAND),
                    new CarAttribute(2000L, "Astra", Type.MODEL),
                    new CarAttribute(2001L, "Corsa", Type.MODEL),
                    new CarAttribute(2002L, "Mokka", Type.MODEL),
                    new CarAttribute(2003L, "Fabia", Type.MODEL),
                    new CarAttribute(2004L, "Octavia", Type.MODEL),
                    new CarAttribute(3000L, "Blue", Type.COLOR),
                    new CarAttribute(3001L, "Green", Type.COLOR),
                    new CarAttribute(3002L, "Yellow", Type.COLOR),
                    new CarAttribute(4000L, "Petrol", Type.ENGINE_TYPE),
                    new CarAttribute(4001L, "Diesel", Type.ENGINE_TYPE),
                    new CarAttribute(4002L, "Electric", Type.ENGINE_TYPE),
                    new CarAttribute(6000L, "Sedan", Type.BODY_TYPE),
                    new CarAttribute(6001L, "Hatchback", Type.BODY_TYPE),
                    new CarAttribute(6002L, "SUV", Type.BODY_TYPE)
            );

            for (Type type : types) {
                model.addAttribute(type.toString().toLowerCase(),
                        filterByType(carAttributes, type));
            }

            return "design";
        }

        log.info("Processing car: " + car);
        return "redirect:/cars/current";
    }

    private List<CarAttribute> filterByType(List<CarAttribute> carAttributes, Type type) {
        return carAttributes.stream()
                .filter(carAttribute -> carAttribute.getType() == type)
                .collect(Collectors.toList());
    }
}
