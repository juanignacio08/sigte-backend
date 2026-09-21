package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "departamento_academico", schema = "maestros")
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

    @Column(name = "creado_por", nullable = false)
    private Long creadoPor;

    @Column(name = "fecha_creacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "actualizado_por", nullable = false)
    private Long actualizadoPor;

    @Column(name = "fecha_actualizacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean eliminado;

}
