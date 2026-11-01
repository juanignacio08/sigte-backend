package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_tesis", schema = "tramites")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoTesisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesis_id", nullable = false)
    private TesisEntity tesis;

    @Column(name = "expediente_tesis_id")
    private Long expedienteTesisId;

    @Column(name = "tipo_documento_codigo", nullable = false, length = 6)
    private String tipoDocumentoCodigo;

    @Column(name = "numero_correlativo")
    private Integer numeroCorrelativo;

    @Column(name = "anio_correlativo")
    private Integer anioCorrelativo;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "archivo_word_url", columnDefinition = "TEXT")
    private String archivoWordUrl;

    @Column(name = "archivo_pdf_url", columnDefinition = "TEXT")
    private String archivoPdfUrl;

    @Column(name = "tamanio_bytes_word")
    private Long tamanioBytesWord;

    @Column(name = "tamanio_bytes_pdf")
    private Long tamanioBytesPdf;

    @Column(name = "resultado_codigo", length = 6)
    private String resultadoCodigo;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "estado_codigo", nullable = false, length = 6)
    private String estadoCodigo;

    @Column(nullable = false)
    private Integer estado;

    @CreatedBy
    @Column(name = "creado_por", nullable = false, updatable = false, length = 100)
    private String creadoPor;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedBy
    @Column(name = "actualizado_por", nullable = false, length = 100)
    private String actualizadoPor;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false)
    private Boolean eliminado;
}
