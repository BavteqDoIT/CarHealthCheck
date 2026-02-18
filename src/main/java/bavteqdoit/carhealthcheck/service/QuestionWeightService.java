package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.model.Question;
import bavteqdoit.carhealthcheck.data.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionWeightService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void updateWeights(Map<String, String[]> params) {

        for (String param : params.keySet()) {

            if (!param.startsWith("weight_")) continue;

            Long questionId = parseId(param.substring(7));
            if (questionId == null) continue;

            String[] value = params.get(param);
            if (value == null || value.length == 0) continue;

            Integer newWeight = parseInt(value[0]);
            if (newWeight == null) continue;

            Question q = questionRepository.findById(questionId).orElse(null);
            if (q == null) continue;

            int safeWeight = Math.max(1, Math.min(999, newWeight));

            if (!safeWeightEquals(q.getWeight(), safeWeight)) {
                q.setWeight(safeWeight);
                questionRepository.save(q);
            }
        }
    }

    private Long parseId(String s) {
        try { return Long.parseLong(s); }
        catch (Exception e) { return null; }
    }

    private Integer parseInt(String s) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return null; }
    }

    private boolean safeWeightEquals(Integer dbWeight, int newWeight) {
        if (dbWeight == null) return false;
        return dbWeight == newWeight;
    }
}
