package com.kingtest.dto.requests;

import com.kingtest.entities.Medico;

import lombok.Data;


@Data
public class MedicoInputDto {
    private String fullName;
    private String cpf;
    private String rg;
    private String birthday;
    private String address;
    private String crm;
    private String degreeDate;
    private String degreeInstitution;
    private String specialization;
    private long clinicId;

    public Medico toEntity(){
        return new Medico(this);
    }
}
