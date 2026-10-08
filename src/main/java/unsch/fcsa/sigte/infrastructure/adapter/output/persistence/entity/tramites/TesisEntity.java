package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tesis", schema = "tramites")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TesisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escuela_id", nullable = false)
    private EscuelaEntity escuela;

    @Column(name = "titulo", nullable = false, columnDefinition = "TEXT")
    private String titulo;

    @Column(name = "titulo_dictaminado", columnDefinition = "TEXT")
    private String tituloDictaminado;

    @Column(name = "estado_codigo", nullable = false, length = 6)
    private String estadoCodigo;

    @Column(name = "fecha_registro", nullable = false)
    private OffsetDateTime fechaRegistro;

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

    @OneToMany(mappedBy = "tesis", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TesisAutorEntity> autores = new ArrayList<>();

    @OneToMany(mappedBy = "tesis", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExpedienteTesisEntity> expedientes = new ArrayList<>();

}