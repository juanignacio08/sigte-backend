package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "estudiante", schema = "personas")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escuela_id", nullable = false)
    private EscuelaEntity escuela;

    @Column(name = "codigo", length = 10)
    private String codigo;

    @Column(name = "numero_documento", length = 15)
    private String numeroDocumento;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "primer_apellido", nullable = false, length = 100)
    private String primerApellido;

    @Column(name = "segundo_apellido", nullable = false, length = 100)
    private String segundoApellido;

    @Column(name = "correo_institucional", nullable = false, length = 100)
    private String correoInstitucional;

    @Column(name = "genero_codigo", nullable = false, length = 6)
    private String generoCodigo;

    @Column(name = "telefono", length = 15)
    private String telefono;

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