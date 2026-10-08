package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tesis_autor", schema = "tramites")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TesisAutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tesis_id", nullable = false)
    private TesisEntity tesis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private EstudianteEntity estudiante;

    @Column(name = "fecha_asociacion", nullable = false)
    private OffsetDateTime fechaAsociacion;

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
