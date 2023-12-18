package com.kingtest.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingtest.dto.requests.LoginRequestDto;
import com.kingtest.dto.requests.MedicoCreateDto;
import com.kingtest.dto.requests.RecepcionistaInputDto;
import com.kingtest.dto.requests.SignupRequestDto;
import com.kingtest.dto.responses.MedicoDataForTableDto;
import com.kingtest.dto.responses.MedicoSafeDto;
import com.kingtest.dto.responses.RecepcionistaSafeDto;
import com.kingtest.entities.Medico;
import com.kingtest.entities.Paciente;
import com.kingtest.entities.Recepcionista;
import com.kingtest.entities.User;

@Service
public class DataEncryptionService {
    @Autowired
    private StringEncryptionService stringEncryptionService;

    // Encription Methods
    public LoginRequestDto encryptLoginRequest(LoginRequestDto loginRequest){
        LoginRequestDto encryptedLoginRequest = new LoginRequestDto();

        encryptedLoginRequest.setUsername(stringEncryptionService.encrypt(loginRequest.getUsername()));
        encryptedLoginRequest.setPassword(stringEncryptionService.encrypt(loginRequest.getPassword()));

        return encryptedLoginRequest;
    }

    public SignupRequestDto convertMedicoToEncryptedSignupRequest(MedicoCreateDto medico) {
        SignupRequestDto signupRequest = medico.toSignupRequest();
        SignupRequestDto encryptSignupRequest = new SignupRequestDto();

        encryptSignupRequest.setEmail(stringEncryptionService.encrypt(signupRequest.getEmail()));
        encryptSignupRequest.setUsername(stringEncryptionService.encrypt(signupRequest.getUsername()));
        encryptSignupRequest.setPassword(stringEncryptionService.encrypt(signupRequest.getPassword()));
        encryptSignupRequest.setRole(stringEncryptionService.encrypt(signupRequest.getRole()));

        return encryptSignupRequest;
    }

    public SignupRequestDto convertRecepcionistaToEncryptedSignupRequest(RecepcionistaInputDto recepcionistaInputDto) {
        SignupRequestDto signupRequest = recepcionistaInputDto.toSignupRequest();
        SignupRequestDto encryptSignupRequest = new SignupRequestDto();

        encryptSignupRequest.setEmail(stringEncryptionService.encrypt(signupRequest.getEmail()));
        encryptSignupRequest.setUsername(stringEncryptionService.encrypt(signupRequest.getUsername()));
        encryptSignupRequest.setPassword(stringEncryptionService.encrypt(signupRequest.getPassword()));
        encryptSignupRequest.setRole(stringEncryptionService.encrypt(signupRequest.getRole()));

        return encryptSignupRequest;
    }


    public MedicoCreateDto convertMedicoToEncryptedMedicoDto(MedicoCreateDto nonEncodedMedico) {
        return MedicoCreateDto.builder()
                .fullName(nonEncodedMedico.getFullName())
                .cpf(stringEncryptionService.encrypt(nonEncodedMedico.getCpf()))
                .rg(stringEncryptionService.encrypt(nonEncodedMedico.getRg()))
                .birthday(stringEncryptionService.encrypt(nonEncodedMedico.getBirthday()))
                .address(stringEncryptionService.encrypt(nonEncodedMedico.getAddress()))
                .crm(stringEncryptionService.encrypt(nonEncodedMedico.getCrm()))
                .dateDiploma(stringEncryptionService.encrypt(nonEncodedMedico.getDateDiploma()))
                .diplomaOrganization(stringEncryptionService.encrypt(nonEncodedMedico.getDiplomaOrganization()))
                .email(stringEncryptionService.encrypt(nonEncodedMedico.getEmail()))
                .password(stringEncryptionService.encrypt(nonEncodedMedico.getPassword()))
                .specialization(stringEncryptionService.encrypt(nonEncodedMedico.getSpecialization()))
                .clinicId(nonEncodedMedico.getClinicId())
                .userId(nonEncodedMedico.getUserId())
                .build();
    }

    public RecepcionistaInputDto convertRecepcionistaInputToEncryptedRecepcionistaDto(RecepcionistaInputDto nonEncodedRecepcionista) {
        return RecepcionistaInputDto.builder()
                .fullName(nonEncodedRecepcionista.getFullName())
                .birthday(stringEncryptionService.encrypt(nonEncodedRecepcionista.getBirthday()))
                .address(stringEncryptionService.encrypt(nonEncodedRecepcionista.getAddress()))
                .email(stringEncryptionService.encrypt(nonEncodedRecepcionista.getEmail()))
                .password(stringEncryptionService.encrypt(nonEncodedRecepcionista.getPassword()))
                .clinicId(nonEncodedRecepcionista.getClinicId())
                .userId(nonEncodedRecepcionista.getUserId())
                .build();
    }


    // Decription Methods
    public Paciente decryptPaciente(Paciente encrytedPaciente) {
        Paciente decodedPaciente = new Paciente();

        decodedPaciente.setId(encrytedPaciente.getId());
        decodedPaciente.setFullName(encrytedPaciente.getFullName());
        decodedPaciente.setAddress(stringEncryptionService.decrypt(encrytedPaciente.getAddress()));
        decodedPaciente.setBirthday(stringEncryptionService.decrypt(encrytedPaciente.getBirthday()));
        decodedPaciente.setGender(stringEncryptionService.decrypt(encrytedPaciente.getGender()));
        decodedPaciente.setPhone(stringEncryptionService.decrypt(encrytedPaciente.getPhone()));
        decodedPaciente.setReligion(stringEncryptionService.decrypt(encrytedPaciente.getReligion()));

        return decodedPaciente;
    }

    public Medico decryptMedico(Medico encryptedMedico) {
        Medico decryptedMedico = Medico.builder()
            .id(encryptedMedico.getId())
            .fullName(encryptedMedico.getFullName())
            .cpf(stringEncryptionService.decrypt(encryptedMedico.getCpf()))
            .rg(stringEncryptionService.decrypt(encryptedMedico.getRg()))
            .birthday(stringEncryptionService.decrypt(encryptedMedico.getBirthday()))
            .address(stringEncryptionService.decrypt(encryptedMedico.getAddress()))
            .crm(stringEncryptionService.decrypt(encryptedMedico.getCrm()))
            .dateDiploma(stringEncryptionService.decrypt(encryptedMedico.getDateDiploma()))
            .diplomaOrganization(stringEncryptionService.decrypt(encryptedMedico.getDiplomaOrganization()))
            .email(stringEncryptionService.decrypt(encryptedMedico.getEmail()))
            .specialization(stringEncryptionService.decrypt(encryptedMedico.getSpecialization()))
            .clinicaId(encryptedMedico.getClinicaId())
            .userId(encryptedMedico.getUserId())
            .build();

        return decryptedMedico;
    }

    public User decryptUser(User user){
		User decryptedUser = new User();

        decryptedUser.setId(user.getId());
        decryptedUser.setEmail(stringEncryptionService.decrypt(user.getEmail()));
        decryptedUser.setRole(stringEncryptionService.decrypt(user.getRole()));
        decryptedUser.setUsername(stringEncryptionService.decrypt(user.getUsername()));
        
        return decryptedUser;
	}

    public MedicoSafeDto decryptMedicoToMedicoSafeDto(Medico medico) {
        return MedicoSafeDto.builder()
                .id(medico.getId())
                .fullName(medico.getFullName())
                .birthday(stringEncryptionService.decrypt(medico.getBirthday()))
                .email(stringEncryptionService.decrypt(medico.getEmail()))
                .clinicId(medico.getClinicaId())
                .userId(medico.getUserId())
                .build();
    }

    public MedicoDataForTableDto decryptMedicoDataForTable(Medico unencryptedMedico) {
        return MedicoDataForTableDto.builder()
                .id(unencryptedMedico.getId())
                .fullName(unencryptedMedico.getFullName())
                .cpf(stringEncryptionService.decrypt(unencryptedMedico.getCpf()))
                .crm(stringEncryptionService.decrypt(unencryptedMedico.getCrm()))
                .clinicId(unencryptedMedico.getClinicaId())
                .specialization(stringEncryptionService.decrypt(unencryptedMedico.getSpecialization()))
                .birthday(stringEncryptionService.decrypt(unencryptedMedico.getBirthday()))
                .rg(stringEncryptionService.decrypt(unencryptedMedico.getRg()))
                .address(stringEncryptionService.decrypt(unencryptedMedico.getAddress()))
                .degreeDate(stringEncryptionService.decrypt(unencryptedMedico.getDateDiploma()))
                .degreeInstitution(stringEncryptionService.decrypt(unencryptedMedico.getDiplomaOrganization()))
                .email(stringEncryptionService.decrypt(unencryptedMedico.getEmail()))
                .build();
    }

    public RecepcionistaSafeDto decryptRecepcionistaToRecepcionistaSafeDto(Recepcionista createdRecepcionista) {
        return RecepcionistaSafeDto.builder()
            .userId(createdRecepcionista.getUserId())
            .fullName(createdRecepcionista.getFullName())
            .email(stringEncryptionService.decrypt(createdRecepcionista.getEmail()))
            .birthday(stringEncryptionService.decrypt(createdRecepcionista.getBirthday()))
            .clinicId(createdRecepcionista.getClinicaId())    
            .build();
    }

}
