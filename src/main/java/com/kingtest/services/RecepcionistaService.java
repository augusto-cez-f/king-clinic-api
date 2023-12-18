package com.kingtest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kingtest.crypto.DataEncryptionService;
import com.kingtest.dto.requests.RecepcionistaInputDto;
import com.kingtest.dto.requests.SignupRequestDto;
import com.kingtest.dto.responses.RecepcionistaSafeDto;
import com.kingtest.entities.Recepcionista;
import com.kingtest.entities.User;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.exceptions.ValidationExceptionValues;
import com.kingtest.repositories.RecepcionistaRepository;
import com.kingtest.services.UserDataService.UserData;
import com.kingtest.services.UserDataService.UserDataService;

@Service
public class RecepcionistaService implements UserDataService {
    @Autowired
    private RecepcionistaRepository recepcionistaRepository;
    @Autowired
    private DataEncryptionService dataEncryptionService;
    @Autowired
    private UserService userService;

    public List<RecepcionistaSafeDto> findByClinicId(long clinicId){
        try{
            List<Recepcionista> recepcionistByClinic = 
                recepcionistaRepository.findByClinicaId(clinicId);

            return recepcionistByClinic
                .stream()
                .map(recepcionista -> 
                    dataEncryptionService.decryptRecepcionistaToRecepcionistaSafeDto(recepcionista))
                .collect(Collectors.toList());

        } catch (Exception e) {
            throw e;
        }        
    }

    @Override
    public UserData login(long userId) {
        try{
            Recepcionista foundRecepcionista = recepcionistaRepository.findByUserId(userId);
            return foundRecepcionista.toSafeDTO();
        } catch (Exception e) {
            throw e;
        }   
    }

    @Transactional(rollbackFor = Exception.class)
    public RecepcionistaSafeDto create(RecepcionistaInputDto recepcionista) throws Exception {
        try {
            SignupRequestDto signupRequest = dataEncryptionService.convertRecepcionistaToEncryptedSignupRequest(recepcionista);
            User user = userService.registerUser(signupRequest);

            recepcionista.setUserId(user.getId());
            RecepcionistaInputDto encryptedRecepcionista = dataEncryptionService
                .convertRecepcionistaInputToEncryptedRecepcionistaDto(recepcionista);

            if (recepcionistaRepository.existsByEmail(encryptedRecepcionista.getEmail())) {
                throw new ValidationException(ValidationExceptionValues.DOCTOR_EMAIL_ALREADY_TAKEN);
            }

            Recepcionista createdRecepcionista = recepcionistaRepository.save(encryptedRecepcionista.toEntity());

            return dataEncryptionService.decryptRecepcionistaToRecepcionistaSafeDto(createdRecepcionista);
        } catch (Exception e) {
            throw e;
        }
    }
}
