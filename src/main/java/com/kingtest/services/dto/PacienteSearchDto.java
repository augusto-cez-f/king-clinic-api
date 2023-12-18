package com.kingtest.services.dto;

import java.util.List;

import com.kingtest.entities.Paciente;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PacienteSearchDto extends PageableSearchDto {
    List<Paciente> pacientes;

    public PacienteSearchDto(long countTotal, List<Paciente> medicos) {
        this.setCountTotal(countTotal);
        this.setPacientes(medicos);
    }
}
