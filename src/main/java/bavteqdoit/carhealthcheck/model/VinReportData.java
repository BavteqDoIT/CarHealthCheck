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

    private String plateNumber;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    @Enumerated(EnumType.STRING)
    private OcStatus ocStatus;

    @Enumerated(EnumType.STRING)
    private TechnicalInspectionStatus technicalInspectionStatus;

    private java.time.LocalDate ocValidUntil;

    private Integer lastOdometerKm;

    @Enumerated(EnumType.STRING)
    private CarRisk theft;

    @Enumerated(EnumType.STRING)
    private CarRisk scrapped;

    @Enumerated(EnumType.STRING)
    private CarRisk accident;

    @Enumerated(EnumType.STRING)
    private CarRisk damaged;

    @Enumerated(EnumType.STRING)
    private CarRisk odometerMismatch;

    @Enumerated(EnumType.STRING)
    private CarRisk notRoadworthy;

    @Enumerated(EnumType.STRING)
    private CarRisk taxi;

    @Enumerated(EnumType.STRING)
    private CarRisk totalLoss;

    @Enumerated(EnumType.STRING)
    private CarRisk vinChecksumError;

    @Enumerated(EnumType.STRING)
    private CarRisk serviceActions;

    private String vinFromReport;
    private String brandFromReport;
    private String modelFromReport;
}
