package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "escuela", schema = "maestros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EscuelaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_academico_id", nullable = false)
    private DepartamentoEntity departamento;

    @Column(nullable = false, unique = true)
    private Integer codigo;

    @Column(nullable = false, length = 10, unique = true)
    private String abreviatura;

    @Column(nullable = false, length = 200)
    private String denominacion;

    @Column(name = "plantilla_memorando", nullable = false, length = 200)
    private String plantillaMemorando;

    @Column(name = "plantilla_resolucion", nullable = false, length = 200)
    private String plantillaResolucion;

    @Column(nullable = false)
    private Integer estado;

    @Column(nullable = false)
    private Boolean eliminado;
}
