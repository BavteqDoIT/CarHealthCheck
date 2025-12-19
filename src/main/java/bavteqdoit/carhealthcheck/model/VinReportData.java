package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class VinReportData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    private Integer productionYearFromReport;

    private LocalDate firstRegistrationFromReport;

    private Long sourceReportFileId;
}
