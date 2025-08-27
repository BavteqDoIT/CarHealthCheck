package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class QuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private QuestionOption selectedOption;

    @ManyToOne(optional = false)
    private Car car;

    private String answerValue;
    private Float numericValue;
}
