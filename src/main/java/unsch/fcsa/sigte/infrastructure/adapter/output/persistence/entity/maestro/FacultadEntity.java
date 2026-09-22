package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "facultad", schema = "maestros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacultadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, unique = true)
    private String abreviatura;

    @Column(nullable = false, length = 200)
    private String denominacion;

    @Column(nullable = false)
    private Integer estado;

    @Column(nullable = false)
    private Boolean eliminado;
}
