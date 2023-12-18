package com.kingtest.entities;

import com.kingtest.dto.requests.PacienteCreateDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "pacientes")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "nome_completo")
    private String fullName;

    @Column(name = "data_nascimento")
    private String birthday;

    @Column(name = "endereco")
    private String address;

    @Column(name = "genero")
    private String gender;

    @Column(name = "telefone")
    private String phone;

    @Column(name = "religiao")
    private String religion;

    public Paciente (long id) {
        this.setId(id);
    }

    public Paciente(PacienteCreateDto createDTO) {
        if(createDTO.getId() != null) {
            this.setId(createDTO.getId());
        }
        
        this.setFullName(createDTO.getFullName());
        this.setBirthday(createDTO.getBirthday());
        this.setAddress(createDTO.getAddress());
        this.setGender(createDTO.getGender());
        this.setPhone(createDTO.getPhone());
        this.setReligion(createDTO.getReligion());
    }
}
