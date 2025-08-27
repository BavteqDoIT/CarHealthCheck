package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Question question;

    @Column(name = "question_option")
    private String value;
    private String labelKey;
}
