package com.kingtest.dto.requests;

import javax.validation.constraints.NotEmpty;

import com.kingtest.entities.Recepcionista;
import com.kingtest.entities.enums.ERole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecepcionistaInputDto {
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String birthday;
    @NotEmpty
    private String address;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private Long clinicId;
    private Long userId;   

    public Recepcionista toEntity(){
        return Recepcionista.builder()
            .fullName(fullName)
            .birthday(birthday)
            .email(email)
            .clinicaId(clinicId)
            .userId(userId)            
            .build();
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
