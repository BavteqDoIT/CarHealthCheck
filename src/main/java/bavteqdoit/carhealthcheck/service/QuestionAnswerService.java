package bavteqdoit.carhealthcheck.service;

import bavteqdoit.carhealthcheck.data.*;
import bavteqdoit.carhealthcheck.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final CarRepository carRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    @Transactional
    public void saveCategoryResponses(Long carId,
                                      Map<String, String> allRequestParams,
                                      boolean isAdmin) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));

        for (String paramName : allRequestParams.keySet()) {

            if (!paramName.startsWith("questionId_")) continue;

            Long questionId = parseLong(allRequestParams.get(paramName));
            if (questionId == null) continue;

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));

            if (isAdmin) {
                updateWeightIfPresent(question, allRequestParams);
            }

            QuestionAnswer answer = buildAnswer(car, question, allRequestParams);

            questionAnswerRepository.save(answer);
        }
    }

    private void updateWeightIfPresent(Question question, Map<String, String> params) {
        String weightParam = params.get("weight_" + question.getId());
        Integer w = parseInt(weightParam);
        if (w == null) return;

        int safeWeight = Math.max(1, Math.min(999, w));
        question.setWeight(safeWeight);
        questionRepository.save(question);
    }

    private QuestionAnswer buildAnswer(Car car, Question question, Map<String, String> params) {
        QuestionAnswer answer = new QuestionAnswer();
        answer.setCar(car);
        answer.setQuestion(question);

        String optionParam = params.get("selectedOption_" + question.getId());
        Long optionId = parseLong(optionParam);
        if (optionId != null) {
            answer.setSelectedOption(
                    questionOptionRepository.findById(optionId).orElse(null)
            );
        }

        String textValue = params.get("answerValue_" + question.getId());
        if (textValue != null) {
            answer.setAnswerValue(textValue);
        }

        String numericValue = params.get("numericValue_" + question.getId());
        Float f = parseFloat(numericValue);
        if (f != null) {
            answer.setNumericValue(f);
        }

        return answer;
    }

    private Long parseLong(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Float parseFloat(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Float.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}