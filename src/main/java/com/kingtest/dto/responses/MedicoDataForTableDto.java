package com.kingtest.dto.responses;

import com.kingtest.entities.Medico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDataForTableDto {
    private long id;
    private String fullName;
    private String cpf;
    private String crm;
    private long clinicId;
    private String specialization;
    private String birthday;
    private String rg;
    private String address;
    private String degreeDate;
    private String degreeInstitution;
    private String email;

    public MedicoDataForTableDto(Medico medico) {
        this.setId(medico.getId());
        this.setFullName(medico.getFullName());
        this.setCpf(medico.getCpf());
        this.setCrm(medico.getCrm());
        this.setClinicId(medico.getClinicaId());
        this.setSpecialization(medico.getSpecialization());
        this.setBirthday(medico.getBirthday());
        this.setRg(medico.getRg());
        this.setAddress(medico.getAddress());
        this.setDegreeDate(medico.getDateDiploma());
        this.setDegreeInstitution(medico.getDiplomaOrganization());
        this.setEmail(medico.getEmail());
    }
}
