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
@Table(name = "departamento_academico", schema = "maestros")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facultad_id", nullable = false)
    private Long facultadId;

    @Column(nullable = false, length = 10, unique = true)
    private String abreviatura;

    @Column(nullable = false, length = 200)
    private String denominacion;

    @Column(nullable = false)
    private Integer estado;

    @CreatedBy
    @Column(name = "creado_por", nullable = false, updatable = false)
    private Long creadoPor;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedBy
    @Column(name = "actualizado_por", nullable = false)
    private Long actualizadoPor;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false)
    private Boolean eliminado;

}
