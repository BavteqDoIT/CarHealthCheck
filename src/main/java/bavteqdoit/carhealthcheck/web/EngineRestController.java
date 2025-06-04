package bavteqdoit.carhealthcheck.web;

import bavteqdoit.carhealthcheck.data.ModelTypeRepository;
import bavteqdoit.carhealthcheck.model.EngineType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/engine")
@RequiredArgsConstructor
public class EngineRestController {
    private final ModelTypeRepository modelTypeRepository;

    @GetMapping("/{modelId}")
    public ResponseEntity<Set<EngineType>> getEnginesByModel(@PathVariable Long modelId) {
        return modelTypeRepository.findById(modelId)
                .map(model -> ResponseEntity.ok(model.getEngineTypes()))
                .orElse(ResponseEntity.notFound().build());
    }
}
