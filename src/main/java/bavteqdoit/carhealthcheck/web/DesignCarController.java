package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.Car;
import bavteqdoit.carhealthcheck.Field.Type;
import bavteqdoit.carhealthcheck.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                new Field(1000L,"Skoda", Type.BRAND),
                new Field(1001L,"Mercedes", Type.BRAND),
                new Field(1002L,"BMW", Type.BRAND),
                new Field(1003L,"Minicooper", Type.BRAND),
                new Field(1004L,"Audi", Type.BRAND),
                new Field(1005L,"Opel", Type.BRAND),
                new Field(2000L,"Astra", Type.MODEL),
                new Field(2001L,"Corsa", Type.MODEL),
                new Field(2002L, "Mokka", Type.MODEL),
                new Field(2003L, "Fabia", Type.MODEL),
                new Field(2004L, "Octavia", Type.MODEL),
                new Field(2005L, "SuperB", Type.MODEL),
                new Field(2006L, "Vito", Type.MODEL),
                new Field(2007L, "SLK", Type.MODEL),
                new Field(2008L, "GT", Type.MODEL),
                new Field(2009L, "Series 3", Type.MODEL),
                new Field(2010L, "Series 5", Type.MODEL),
                new Field(2011L, "Series 7", Type.MODEL),
                new Field(2012L, "Cabrio", Type.MODEL),
                new Field(2013L, "Clubmann", Type.MODEL),
                new Field(2014L, "Countrymann", Type.MODEL),
                new Field(2015L, "A3", Type.MODEL),
                new Field(2016L, "A5", Type.MODEL),
                new Field(2017L, "A7", Type.MODEL),
                new Field(3000L, "Blue", Type.COLOR),
                new Field(3001L, "Green", Type.COLOR),
                new Field(3002L, "Yellow", Type.COLOR),
                new Field(3003L, "Orange", Type.COLOR),
                new Field(3004L, "Violet", Type.COLOR),
                new Field(3005L, "Black", Type.COLOR),
                new Field(3006L, "Red", Type.COLOR),
                new Field(3007L, "Gold", Type.COLOR),
                new Field(3008L, "White", Type.COLOR),
                new Field(4000L, "Petrol", Type.ENGINE_TYPE),
                new Field(4001L, "Petrol with CNG", Type.ENGINE_TYPE),
                new Field(4002L, "Petrol with LPG", Type.ENGINE_TYPE),
                new Field(4003L, "Diesel", Type.ENGINE_TYPE),
                new Field(4004L, "Electric", Type.ENGINE_TYPE),
                new Field(4005L, "Ethanol", Type.ENGINE_TYPE),
                new Field(4006L, "Hybrid", Type.ENGINE_TYPE),
                new Field(4007L, "Plug-in Hybrid", Type.ENGINE_TYPE),
                new Field(4008L, "Hydrogen", Type.ENGINE_TYPE),
                new Field(5000L, "Automatic", Type.GEAR_TYPE),
                new Field(5001L, "Manual", Type.GEAR_TYPE),
                new Field(6000L, "Sedan", Type.BODY_TYPE),
                new Field(6001L, "Hatchback", Type.BODY_TYPE),
                new Field(6002L, "Wagon", Type.BODY_TYPE),
                new Field(6003L, "Coupe", Type.BODY_TYPE),
                new Field(6004L, "Convertible", Type.BODY_TYPE),
                new Field(6005L, "SUV", Type.BODY_TYPE),
                new Field(6006L, "Crossover", Type.BODY_TYPE),
                new Field(6007L, "Pickup", Type.BODY_TYPE),
                new Field(6008L, "Minivan", Type.BODY_TYPE),
                new Field(6009L, "Van", Type.BODY_TYPE),
                new Field(6010L, "Off road", Type.BODY_TYPE),
                new Field(6011L, "Roadster", Type.BODY_TYPE),
                new Field(7000L, "Kilometers", Type.MILEAGE_UNIT),
                new Field(7001L, "Miles", Type.MILEAGE_UNIT),
                new Field(8000L, "Left", Type.STEERING_SIDE),
                new Field(8001L, "Right", Type.STEERING_SIDE)
                );

        Type[] types = Field.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(fields, type));
        }
        model.addAttribute("design",new Car());
        return "design";
    }

    @PostMapping
    public String processDesign(Car design) {
        log.info("Proccesing car: " + design);
        return "redirect:/car/current";
    }

    private List<Field> filterByType(List<Field> fields, Type type) {
        return fields.stream()
                .filter(field -> field.getType() == type)
                .collect(Collectors.toList());
    }
}