package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ModelType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_type")
    private String modelName;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
