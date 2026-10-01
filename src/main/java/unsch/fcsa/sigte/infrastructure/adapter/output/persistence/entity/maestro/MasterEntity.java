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
@Table(name = "maestro", schema = "maestros")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_tabla", nullable = false, length = 3)
    private String tableCode;

    @Column(name = "codigo_item", nullable = false, length = 3)
    private String itemCode;

    @Column(name = "codigo", nullable = false, length = 6, unique = true)
    private String code;

    @Column(name = "denominacion", nullable = false, length = 200)
    private String denomination;

    @Column(name = "es_sistema", nullable = false)
    private Boolean isSystem;

    @Column(name = "estado", nullable = false)
    private Integer state;

    @CreatedBy
    @Column(name = "creado_por", nullable = false, updatable = false)
    private String creadoPor;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedBy
    @Column(name = "actualizado_por", nullable = false)
    private String actualizadoPor;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "eliminado", nullable = false)
    private Boolean isDeleted;

}
