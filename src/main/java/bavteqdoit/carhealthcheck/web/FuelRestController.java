package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
import bavteqdoit.carhealthcheck.model.EngineType;
import bavteqdoit.carhealthcheck.model.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fuels")
@RequiredArgsConstructor
public class FuelRestController {

    private final ModelTypeRepository modelTypeRepository;
    private final MessageSource messageSource;

    @GetMapping("/{modelId}")
    public ResponseEntity<Set<Map<String, Object>>> getFuelsByModel(
            @PathVariable Long modelId,
            Locale locale
    ) {
        return modelTypeRepository.findById(modelId)
                .map(model -> model.getEngineTypes().stream()
                        .map(EngineType::getFuelType)
                        .filter(f -> f != null)
                        .collect(Collectors.toMap(
                                FuelType::getId,
                                f -> f,
                                (a, b) -> a
                        ))
                        .values()
                        .stream()
                        .map(fuel -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", fuel.getId());
                            map.put("name", fuel.getFuel());
                            map.put("displayName", messageSource.getMessage(
                                    "fuel." + fuel.getFuel().toLowerCase(),
                                    null,
                                    fuel.getFuel(),
                                    locale
                            ));
                            return map;
                        })
                        .collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

