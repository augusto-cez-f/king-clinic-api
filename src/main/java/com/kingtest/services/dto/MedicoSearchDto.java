package com.kingtest.services.dto;

import java.util.List;

import com.kingtest.dto.responses.MedicoDataForTableDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MedicoSearchDto extends PageableSearchDto {
    List<MedicoDataForTableDto> medicos;

    public MedicoSearchDto(long countTotal, List<MedicoDataForTableDto> medicos) {
        this.setCountTotal(countTotal);
        this.setMedicos(medicos);
    }
}
