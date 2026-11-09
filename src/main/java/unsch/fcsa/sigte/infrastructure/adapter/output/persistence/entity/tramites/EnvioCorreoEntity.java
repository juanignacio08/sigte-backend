package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "envio_correo", schema = "tramites")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EnvioCorreoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_Id", nullable = false)
    private DocumentoTesisEntity documento;

    @Column(nullable = false, length = 300)
    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String cuerpo;

    // JSONB en PostgreSQL → se guarda como String con @JdbcTypeCode
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String destinatarios;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String adjuntos;

    @Column(name = "fecha_envio", nullable = false)
    private OffsetDateTime fechaEnvio;

    @Column(name = "error_mensaje", columnDefinition = "TEXT")
    private String errorMensaje;

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