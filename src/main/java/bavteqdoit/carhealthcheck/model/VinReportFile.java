package bavteqdoit.carhealthcheck.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class VinReportFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_bytes", nullable = false, columnDefinition = "bytea")
    private byte[] pdfBytes;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VinReportStatus status;

    private LocalDateTime uploadedAt;

    private LocalDateTime parsedAt;

    @Column(length = 800)
    private String parseError;
}
