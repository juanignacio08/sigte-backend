package unsch.fcsa.sigte.application.port.out.maestro.dtos;

import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;

import java.util.List;

public record DepartamentoDetalle(
        Long id,
        Long facultadId,
        String abreviatura,
        String denominacion,
        Integer estado,
        List<EscuelaProfesional> escuelas
) {}
