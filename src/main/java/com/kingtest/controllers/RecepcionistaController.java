package com.kingtest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingtest.dto.requests.RecepcionistaInputDto;
import com.kingtest.dto.responses.RecepcionistaSafeDto;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.services.RecepcionistaService;

@RestController
@RequestMapping("/recepcionistas")
public class RecepcionistaController {
    @Autowired
    private RecepcionistaService recepcionistaService;
    
    @GetMapping("/clinica/{clinicId}")
    public ResponseEntity<List<RecepcionistaSafeDto>> findByClinicaId(@PathVariable(value = "clinicId") long clinicId) {
        List<RecepcionistaSafeDto> recepcionistaSafeDTOs = 
            recepcionistaService.findByClinicId(clinicId);

        if (recepcionistaSafeDTOs != null) {
            return ResponseEntity.ok().body(recepcionistaSafeDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> registerRecepcionista(@RequestBody RecepcionistaInputDto recepcionistaInputDto) {
        try {
            RecepcionistaSafeDto recepcionistaSafeDto = recepcionistaService.create(recepcionistaInputDto);
            return new ResponseEntity<RecepcionistaSafeDto>(recepcionistaSafeDto, HttpStatus.CREATED);
        } catch (ValidationException validationE) {
            validationE.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(validationE.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
