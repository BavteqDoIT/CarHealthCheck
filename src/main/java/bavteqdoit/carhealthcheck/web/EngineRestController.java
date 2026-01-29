package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
import bavteqdoit.carhealthcheck.model.EngineType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/engine")
@RequiredArgsConstructor
public class EngineRestController {
    private final MessageSource messageSource;
    private final ModelTypeRepository modelTypeRepository;

    @GetMapping("/{modelId}")
    public ResponseEntity<List<Map<String, Object>>> getEnginesByModel(
            @PathVariable Long modelId,
            @RequestParam(required = false) Long fuelId,
            Locale locale
    ) {
        return modelTypeRepository.findById(modelId)
                .map(model -> model.getEngineTypes().stream()
                        .filter(engine -> fuelId == null
                                || (engine.getFuelType() != null && fuelId.equals(engine.getFuelType().getId())))
                        .sorted(Comparator.comparing(
                                EngineType::getCapacity,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed())
                        .map(engine -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", engine.getId());
                            map.put("name", engine.getName());
                            map.put("hp", engine.getHorsepowerHp());
                            map.put("kw", engine.getPowerKw());
                            map.put("capacity", engine.getCapacity());

                            String baseName = messageSource.getMessage(
                                    engine.getName().replace(" ", "_"),
                                    null,
                                    engine.getName(),
                                    locale
                            );

                            String powerPart = "";
                            if (engine.getHorsepowerHp() != null && engine.getPowerKw() != null) {
                                powerPart = " (" + engine.getHorsepowerHp() + " HP / " + engine.getPowerKw() + " kW)";
                            }


                            map.put("displayName", baseName + powerPart);
                            return map;
                        })
                        .collect(Collectors.toList())
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
