package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "plantilla_documento", schema = "maestros")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantillaDocumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "escuela_id", nullable = false)
    private Long escuelaId;

    @Column(name = "tipo_documento_codigo", nullable = false, length = 6)
    private String tipoDocumentoCodigo;

    @Column(name = "nombre_documento", nullable = false)
    private String nombreDocumento;

    @Column(name = "archivo_url", nullable = false, columnDefinition = "TEXT")
    private String archivoUrl;

    @Column(name = "plazo_dias")
    private Integer plazoDias;

    @Column(name = "plazo_meses")
    private Integer plazoMeses;

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