package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.Objects;

@Getter
@Setter
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

    @ManyToMany
    @JoinTable(
            name = "model_engine_type",
            joinColumns = @JoinColumn(name = "model_type_id"),
            inverseJoinColumns = @JoinColumn(name = "engine_type_id")
    )
    private Set<EngineType> engineTypes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelType that)) return false;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
