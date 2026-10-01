package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "docente", schema = "personas")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escuela_id", nullable = false)
    private EscuelaEntity escuela;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_academico_id", nullable = false)
    private DepartamentoEntity departamentoAcademico;

    @Column(name = "tipo_documento_codigo", length = 6)
    private String tipoDocumentoCodigo;

    @Column(name = "numero_documento", length = 15)
    private String numeroDocumento;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(name = "primer_apellido", nullable = false, length = 100)
    private String primerApellido;

    @Column(name = "segundo_apellido", nullable = false, length = 100)
    private String segundoApellido;

    @Column(name = "correo_institucional", nullable = false, length = 100, unique = true)
    private String correoInstitucional;

    @Column(name = "genero_codigo", nullable = false, length = 6)
    private String generoCodigo;

    @Column(length = 15)
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