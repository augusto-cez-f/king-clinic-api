package com.kingtest.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kingtest.crypto.DataEncryptionService;
import com.kingtest.dto.requests.MedicoCreateDto;
import com.kingtest.dto.requests.MedicoInputDto;
import com.kingtest.dto.requests.SignupRequestDto;
import com.kingtest.dto.responses.MedicoDataForTableDto;
import com.kingtest.dto.responses.MedicoSafeDto;
import com.kingtest.entities.Medico;
import com.kingtest.entities.User;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.exceptions.ValidationExceptionValues;
import com.kingtest.repositories.MedicoRepository;
import com.kingtest.services.UserDataService.UserDataService;
import com.kingtest.services.dto.MedicoSearchDto;
import com.kingtest.util.ProjectConstants;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MedicosService implements UserDataService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private DataEncryptionService dataEncryptionService;

    @Autowired
    private UserService userServices;

    public MedicoSearchDto findByClinicId(long clinicId, int page) {
        try {
            Pageable pageable = PageRequest.of(page, ProjectConstants.DEFAULT_REGISTRIES_BY_PAGES);
            long medicosTotal = medicoRepository.countByClinicaId(clinicId);
            List<Medico> doctorsByClinic = medicoRepository.findByClinicaIdOrderByFullName(clinicId, pageable);
            List<MedicoDataForTableDto> medicosSafe = doctorsByClinic
                    .stream()
                    .map(medico -> dataEncryptionService.decryptMedicoDataForTable(medico))
                    .collect(Collectors.toList());

            MedicoSearchDto medicosSearch = new MedicoSearchDto(medicosTotal, medicosSafe);

            return medicosSearch;
        } catch (Exception e) {
            throw e;
        }
    }

    public MedicoSafeDto login(long userId) {
        Medico medico = medicoRepository.findByUserId(userId);
        MedicoSafeDto decryptedMedico = dataEncryptionService.decryptMedicoToMedicoSafeDto(medico);

        return decryptedMedico;
    }

    public MedicoSearchDto findByClinicAndName(long clinicId, String name, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            long medicosTotal = medicoRepository.countByClinicaIdAndFullNameContains(clinicId, name);
            List<Medico> doctorsByClinic = medicoRepository.findByClinicaIdAndFullNameContainsOrderByFullName(clinicId,
                    name, pageable);
            List<MedicoDataForTableDto> medicosSafe = doctorsByClinic
                    .stream()
                    .map(medico -> dataEncryptionService.decryptMedicoDataForTable(medico))
                    .collect(Collectors.toList());

            MedicoSearchDto medicosSearch = new MedicoSearchDto(medicosTotal, medicosSafe);

            return medicosSearch;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public MedicoSafeDto create(MedicoCreateDto medico) throws Exception {
        try {
            SignupRequestDto signupRequest = dataEncryptionService.convertMedicoToEncryptedSignupRequest(medico);
            User medUser = userServices.registerUser(signupRequest);

            medico.setUserId(medUser.getId());
            MedicoCreateDto encryptedMedico = dataEncryptionService.convertMedicoToEncryptedMedicoDto(medico);

            if (medicoRepository.existsByEmail(encryptedMedico.getEmail())) {
                throw new ValidationException(ValidationExceptionValues.DOCTOR_EMAIL_ALREADY_TAKEN);
            }

            Medico createdMedico = medicoRepository.save(encryptedMedico.toEntity());

            return dataEncryptionService.decryptMedicoToMedicoSafeDto(createdMedico);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public MedicoSafeDto update(String id, MedicoInputDto medico) throws Exception {
        try {
            Long longId = Long.parseLong(id);
            Optional<Medico> thisMedicOptional = medicoRepository.findById(longId);

            if (thisMedicOptional.isEmpty()) {
                throw new ValidationException(ValidationExceptionValues.MED_ID_NOT_FOUND);
            }

            Medico foundMedic = thisMedicOptional.get();
            Medico editData = medico.toEntity();

            editData.setId(longId);
            editData.setEmail(foundMedic.getEmail());
            editData.setClinicaId(foundMedic.getClinicaId());
            editData.setUserId(foundMedic.getUserId());

            Medico createdMedico = medicoRepository.save(editData);

            return createdMedico.toSafeDTO();
        } catch (Exception e) {
            throw e;
        }
    }

}
