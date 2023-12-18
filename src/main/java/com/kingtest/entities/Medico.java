package com.kingtest.entities;

import com.kingtest.dto.requests.MedicoCreateDto;
import com.kingtest.dto.requests.MedicoInputDto;
import com.kingtest.dto.responses.MedicoDataForTableDto;
import com.kingtest.dto.responses.MedicoSafeDto;

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

@Entity
@Table(name= "medicos")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="nome_completo")
    private String fullName;

    @Column(name="cpf")
    private String cpf;

    @Column(name="rg")
    private String rg;
    
    @Column(name="data_nascimento")
    private String birthday;

    @Column(name="endereco")
    private String address;

    @Column(name="crm")
    private String crm;

    @Column(name="diploma_data")
    private String dateDiploma;

    @Column(name="diploma_instituicao")
    private String diplomaOrganization;

    @Column(name="email")
    private String email;

    @Column(name="especializacao")
    private String specialization;

    @Column(name="clinica_id")
    private long clinicaId;

    @Column(name="user_id")
    private long userId;    

    public Medico(MedicoCreateDto medicoCreateDto){
        this.setFullName(medicoCreateDto.getFullName());
        this.setCpf(medicoCreateDto.getCpf());
        this.setRg(medicoCreateDto.getRg());
        this.setBirthday(medicoCreateDto.getBirthday());
        this.setAddress(medicoCreateDto.getAddress());
        this.setCrm(medicoCreateDto.getCrm());
        this.setDateDiploma(medicoCreateDto.getDateDiploma());
        this.setDiplomaOrganization(medicoCreateDto.getDiplomaOrganization());
        this.setEmail(medicoCreateDto.getEmail());        
        this.setClinicaId(medicoCreateDto.getClinicId());
        this.setUserId(medicoCreateDto.getUserId());
        this.setSpecialization(medicoCreateDto.getSpecialization());
    }

    public Medico(MedicoInputDto medicoInputDTO){
        this.setFullName(medicoInputDTO.getFullName());
        this.setCpf(medicoInputDTO.getCpf());
        this.setRg(medicoInputDTO.getRg());
        this.setBirthday(medicoInputDTO.getBirthday());
        this.setAddress(medicoInputDTO.getAddress());
        this.setCrm(medicoInputDTO.getCrm());
        this.setDateDiploma(medicoInputDTO.getDegreeDate());
        this.setDiplomaOrganization(medicoInputDTO.getDegreeInstitution());
        this.setClinicaId(medicoInputDTO.getClinicId());
        this.setSpecialization(medicoInputDTO.getSpecialization());
    }

    public Medico(long id) {
        this.setId(id);
    }

    public MedicoSafeDto toSafeDTO(){
        return new MedicoSafeDto(this);
    }

    public MedicoDataForTableDto toDataForTableDTO(){
        return new MedicoDataForTableDto(this);
    }
}
