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
@Table(name = "comision", schema = "tramites")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComisionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesis_id", nullable = false)
    private TesisEntity tesis;

    @Column(name = "etapa_codigo", nullable = false, length = 6)
    private String etapaCodigo;

    @Column(name = "fecha_conformacion", nullable = false)
    private LocalDate fechaConformacion;

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
