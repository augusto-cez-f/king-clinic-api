package com.kingtest.dto.requests;

import javax.validation.constraints.NotEmpty;

import com.kingtest.entities.Medico;
import com.kingtest.entities.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MedicoCreateDto {
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String cpf;
    @NotEmpty
    private String rg;
    @NotEmpty
    private String birthday;
    @NotEmpty
    private String address;
    @NotEmpty
    private String crm;
    // @NotEmpty
    private String dateDiploma;
    // @NotEmpty
    private String diplomaOrganization;
    // @NotEmpty
    private String email;
    // @NotEmpty
    private String password;
    // @NotEmpty
    private String specialization;
    // @NotEmpty
    private long clinicId;
    private long userId;   

    public Medico toEntity(){
        return new Medico(this);
    }

    public SignupRequestDto toSignupRequest(){
        return new SignupRequestDto(
            email, //TODO talvez alterar login para nome do usuário
            email,
            ERole.M.toString(),
            password
        );
    }
}
