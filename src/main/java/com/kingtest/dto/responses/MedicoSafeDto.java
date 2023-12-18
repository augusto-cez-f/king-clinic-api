package com.kingtest.dto.responses;

import com.kingtest.entities.Medico;
import com.kingtest.services.UserDataService.UserData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MedicoSafeDto extends UserData {
    private long id;    
    private String fullName;
    private String birthday;
    private String email;
    private long clinicId;

    public MedicoSafeDto(Medico medico) {
        this.setId(medico.getId());
        this.setFullName(medico.getFullName());
        this.setBirthday(medico.getBirthday());
        this.setEmail(medico.getEmail());
        this.setClinicId(medico.getClinicaId());
        this.setUserId(medico.getUserId());
    }
}
