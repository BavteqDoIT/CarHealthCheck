package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
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
@RequestMapping("/api/engine")
@RequiredArgsConstructor
public class EngineRestController {
    private final MessageSource messageSource;
    private final ModelTypeRepository modelTypeRepository;

    @GetMapping("/{modelId}")
    public ResponseEntity<Set<Map<String, Object>>> getEnginesByModel(
            @PathVariable Long modelId,
            Locale locale) {

        return modelTypeRepository.findById(modelId)
                .map(model -> model.getEngineTypes().stream().map(engine -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", engine.getId());
                    map.put("name", engine.getName());
                    map.put("hp", engine.getHorsepowerHp());
                    map.put("kw", engine.getPowerKw());
                    String baseName = messageSource.getMessage(
                            engine.getName().replace(" ", "_"),
                            null,
                            engine.getName(),
                            locale
                    );

                    String powerPart = "";
                    if (engine.getHorsepowerHp() != null && engine.getPowerKw() != null) {
                        powerPart = " (" + engine.getHorsepowerHp() + " HP / "
                                + engine.getPowerKw() + " kW)";
                    }

                    map.put("displayName", baseName + powerPart);
                    return map;
                }).collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
