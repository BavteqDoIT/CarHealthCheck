package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "vin_mileage_entry",
        uniqueConstraints = @UniqueConstraint(name="uk_car_date_mileage", columnNames = {"car_id","reading_date","mileage_km"}))
public class VinMileageEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "reading_date", nullable = false)
    private LocalDate readingDate;

    @Column(name = "mileage_km", nullable = false)
    private Integer mileageKm;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private VinMileageSource source = VinMileageSource.UNKNOWN;

    @Column(name = "event_title", length = 500)
    private String eventTitle;
}
