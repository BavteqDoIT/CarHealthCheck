package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.Car;
import bavteqdoit.carhealthcheck.Field;
import bavteqdoit.carhealthcheck.Field.Type;
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
        List<Field> fields = Arrays.asList(
                new Field(1000L, "Skoda", Type.BRAND),
                new Field(1001L, "Mercedes", Type.BRAND),
                new Field(1002L, "BMW", Type.BRAND),
                new Field(1003L, "Minicooper", Type.BRAND),
                new Field(1004L, "Audi", Type.BRAND),
                new Field(1005L, "Opel", Type.BRAND),
                new Field(2000L, "Astra", Type.MODEL),
                new Field(2001L, "Corsa", Type.MODEL),
                new Field(2002L, "Mokka", Type.MODEL),
                new Field(2003L, "Fabia", Type.MODEL),
                new Field(2004L, "Octavia", Type.MODEL),
                new Field(3000L, "Blue", Type.COLOR),
                new Field(3001L, "Green", Type.COLOR),
                new Field(3002L, "Yellow", Type.COLOR),
                new Field(4000L, "Petrol", Type.ENGINE_TYPE),
                new Field(4001L, "Diesel", Type.ENGINE_TYPE),
                new Field(4002L, "Electric", Type.ENGINE_TYPE),
                new Field(6000L, "Sedan", Type.BODY_TYPE),
                new Field(6001L, "Hatchback", Type.BODY_TYPE),
                new Field(6002L, "SUV", Type.BODY_TYPE)
        );

        Type[] types = Field.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(fields, type));
        }

        model.addAttribute("car", new Car());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Car car, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Type[] types = Field.Type.values();
            List<Field> fields = Arrays.asList(
                    new Field(1000L, "Skoda", Type.BRAND),
                    new Field(1001L, "Mercedes", Type.BRAND),
                    new Field(1002L, "BMW", Type.BRAND),
                    new Field(1003L, "Minicooper", Type.BRAND),
                    new Field(1004L, "Audi", Type.BRAND),
                    new Field(1005L, "Opel", Type.BRAND),
                    new Field(2000L, "Astra", Type.MODEL),
                    new Field(2001L, "Corsa", Type.MODEL),
                    new Field(2002L, "Mokka", Type.MODEL),
                    new Field(2003L, "Fabia", Type.MODEL),
                    new Field(2004L, "Octavia", Type.MODEL),
                    new Field(3000L, "Blue", Type.COLOR),
                    new Field(3001L, "Green", Type.COLOR),
                    new Field(3002L, "Yellow", Type.COLOR),
                    new Field(4000L, "Petrol", Type.ENGINE_TYPE),
                    new Field(4001L, "Diesel", Type.ENGINE_TYPE),
                    new Field(4002L, "Electric", Type.ENGINE_TYPE),
                    new Field(6000L, "Sedan", Type.BODY_TYPE),
                    new Field(6001L, "Hatchback", Type.BODY_TYPE),
                    new Field(6002L, "SUV", Type.BODY_TYPE)
            );

            for (Type type : types) {
                model.addAttribute(type.toString().toLowerCase(),
                        filterByType(fields, type));
            }

            return "design";
        }

        log.info("Processing car: " + car);
        return "redirect:/cars/current";
    }

    private List<Field> filterByType(List<Field> fields, Type type) {
        return fields.stream()
                .filter(field -> field.getType() == type)
                .collect(Collectors.toList());
    }
}
