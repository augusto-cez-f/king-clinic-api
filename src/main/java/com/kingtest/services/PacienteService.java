package com.kingtest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kingtest.crypto.DataEncryptionService;
import com.kingtest.crypto.StringEncryptionService;
import com.kingtest.dto.requests.PacienteCreateDto;
import com.kingtest.entities.Paciente;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.exceptions.ValidationExceptionValues;
import com.kingtest.repositories.PacienteRepository;
import com.kingtest.services.dto.PacienteSearchDto;
import com.kingtest.util.ProjectConstants;

@Service
public class PacienteService {
    private PacienteRepository pacienteRepository;
    private StringEncryptionService stringEncryptionService;
    private DataEncryptionService dataEncryptionService;

    public PacienteService(PacienteRepository pacienteRepository, 
                            StringEncryptionService stringEncryptionService,
                            DataEncryptionService dataEncryptionService) {
        this.pacienteRepository = pacienteRepository;
        this.stringEncryptionService = stringEncryptionService;
        this.dataEncryptionService = dataEncryptionService;
    }
    
    public Paciente create(PacienteCreateDto paciente) throws Exception{
        try{
            PacienteCreateDto encodedPacienteDto = encodePacienteData(paciente);
            validatePacienteForCreating(encodedPacienteDto);
            Paciente createdPaciente = pacienteRepository.save(encodedPacienteDto.toEntity());
            
            return dataEncryptionService.decryptPaciente(createdPaciente);
        } catch (Exception e) {
            throw e;
        }        
    }

    private void validatePacienteForCreating(PacienteCreateDto paciente) throws Exception {
        boolean isPacienteInvalid = pacienteRepository.existsByFullName(paciente.getFullName());

        if(isPacienteInvalid) {
            throw new ValidationException(ValidationExceptionValues.PAT_FULLNAME_ALREADY_TAKEN);
        }
    }

    public Paciente edit(PacienteCreateDto paciente) throws Exception{
        try{
            PacienteCreateDto encodedPacienteDto = encodePacienteData(paciente);
            validatePacienteForEditing(encodedPacienteDto);
            Paciente createdPaciente = pacienteRepository.save(encodedPacienteDto.toEntity());
            
            return dataEncryptionService.decryptPaciente(createdPaciente);
        } catch (Exception e) {
            throw e;
        }        
    }

    private void validatePacienteForEditing(PacienteCreateDto paciente) throws Exception{
        boolean isPacienteInvalid = pacienteRepository.existsByFullNameAndIdIsNot(paciente.getFullName(), paciente.getId());
        
        if (isPacienteInvalid) {
            throw new ValidationException(ValidationExceptionValues.PAT_FULLNAME_ALREADY_TAKEN);
        }
    }

    public PacienteSearchDto findAll(int page) throws Exception{
        try{
            Pageable pageable = PageRequest.of(page, ProjectConstants.DEFAULT_REGISTRIES_BY_PAGES);
            
            long pacientesTotal = pacienteRepository.count();
            Page<Paciente> pacientes = pacienteRepository.findAllByOrderByFullName(pageable);
            List<Paciente> encodedPacientes = pacientes.toList();
            List<Paciente> decodedPacientes = 
                    encodedPacientes.stream()
                    .map(paciente -> dataEncryptionService.decryptPaciente(paciente))
                    .collect(Collectors.toList());
            
            PacienteSearchDto pacientesSearch = new PacienteSearchDto(pacientesTotal, 
                decodedPacientes);

            return pacientesSearch;
        } catch (Exception e) {
            throw e;
        }        
    }

    public PacienteSearchDto findByPartialName(String partialName, int page) throws Exception{
        try{

            Pageable pageable = PageRequest.of(page, ProjectConstants.DEFAULT_REGISTRIES_BY_PAGES);
            
            long pacientesTotal = pacienteRepository.countByFullNameContains(partialName);
            List<Paciente> pacientes = pacienteRepository.findByFullNameContainsOrderByFullName(partialName, pageable);
            List<Paciente> decodedPacientes = 
                    pacientes.stream()
                    .map(paciente -> dataEncryptionService.decryptPaciente(paciente))
                    .collect(Collectors.toList());
            
            PacienteSearchDto pacientesSearch = new PacienteSearchDto(pacientesTotal, 
                decodedPacientes);

            return pacientesSearch;
        } catch (Exception e) {
            throw e;
        }        
    }

    private PacienteCreateDto encodePacienteData(PacienteCreateDto createDto){
        PacienteCreateDto encriptedPaciente = new PacienteCreateDto();

        encriptedPaciente.setId(createDto.getId());
        encriptedPaciente.setFullName(createDto.getFullName());
        encriptedPaciente.setBirthday(stringEncryptionService.encrypt(createDto.getBirthday()));
        encriptedPaciente.setAddress(stringEncryptionService.encrypt(createDto.getAddress()));
        encriptedPaciente.setGender(stringEncryptionService.encrypt(createDto.getGender()));
        encriptedPaciente.setPhone(stringEncryptionService.encrypt(createDto.getPhone()));
        encriptedPaciente.setReligion(stringEncryptionService.encrypt(createDto.getReligion()));

        return encriptedPaciente;
    }
}
