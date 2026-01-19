package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(
        name = "model_variant",
        uniqueConstraints = @UniqueConstraint(
                name = "ux_mv_unique",
                columnNames = {"model_type_id", "engine_type_id", "body_type_id", "year_from", "year_to"}
        )
)
public class ModelVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "model_type_id", nullable = false)
    private ModelType modelType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "engine_type_id", nullable = false)
    private EngineType engineType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "body_type_id", nullable = false)
    private BodyType bodyType;

    @Column(name = "year_from")
    private Integer yearFrom;

    @Column(name = "year_to")
    private Integer yearTo;

    @Column(name = "generation_code")
    private String generationCode;

    private Boolean facelift;

    @Column(name = "basic_score")
    @Min(0) @Max(100)
    private Integer basicScore;

    @Column(name = "source_url", columnDefinition = "TEXT")
    private String sourceUrl;

    @Column(name = "source_ratings_count")
    private Integer sourceRatingsCount;

    @Column(name = "source_avg_rating")
    private Double sourceAvgRating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelVariant that)) return false;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
