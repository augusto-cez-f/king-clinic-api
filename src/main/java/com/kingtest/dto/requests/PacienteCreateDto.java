package com.kingtest.dto.requests;

import com.kingtest.entities.Paciente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PacienteCreateDto {
    private Long id;
    private String fullName;
    private String birthday;
    private String address;
    private String gender;
    private String phone;
    private String religion;

    public Paciente toEntity(){
        return new Paciente(this);
    }
}
