package bavteqdoit.carhealthcheck.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.Objects;

@Getter
@Setter
@Entity
public class EngineType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "engine_type")
    private String engineType;

    @JsonIgnore
    @ManyToMany(mappedBy = "engineTypes")
    private Set<ModelType> modelTypes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EngineType that)) return false;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
