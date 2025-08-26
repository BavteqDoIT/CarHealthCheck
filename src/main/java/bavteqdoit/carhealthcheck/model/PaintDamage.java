package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PaintDamage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paint_id")
    private PaintCheck paintCheck;

    private String bodyPart;
    private String damageType;
    private String size;
}
