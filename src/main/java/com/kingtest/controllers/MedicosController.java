package com.kingtest.controllers;

import java.util.List;

import javax.validation.Valid;

// import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingtest.dto.requests.MedicoCreateDto;
import com.kingtest.dto.requests.MedicoInputDto;
import com.kingtest.dto.responses.MedicoDataForTableDto;
import com.kingtest.dto.responses.MedicoSafeDto;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.services.MedicosService;
import com.kingtest.services.dto.MedicoSearchDto;
import com.kingtest.util.ProjectConstants;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/medicos")
public class MedicosController {
    @Autowired
    private MedicosService medicoService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid MedicoCreateDto medico) {
        try {
            MedicoSafeDto medicoSafeDTO = medicoService.create(medico);
            return new ResponseEntity<MedicoSafeDto>(medicoSafeDTO, HttpStatus.CREATED);
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

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") String id, @RequestBody MedicoInputDto medico) {
        try {
            MedicoSafeDto medicoSafeDTO = medicoService.update(id, medico);

            return new ResponseEntity<MedicoSafeDto>(medicoSafeDTO, HttpStatus.CREATED);

        } catch (ValidationException validationE) {
            return ResponseEntity.badRequest().body(validationE.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Do better error management here
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/clinica/{clinicId}")
    public ResponseEntity<List<MedicoDataForTableDto>> findByClinicaId(
            @PathVariable(value = "clinicId") long clinicId,
            @RequestParam("page") int page) throws Exception {

        MedicoSearchDto medicoSearch = medicoService.findByClinicId(clinicId, page);

        if (medicoSearch.getMedicos() != null) {
            return ResponseEntity
                    .ok()
                    .header(ProjectConstants.HDR_TOTAL_REGISTRIES,
                            Long.toString(medicoSearch.getCountTotal()))
                    // .header("Access-Control-Allow-Headers",
                    // ProjectConstants.HDR_TOTAL_REGISTRIES)
                    .body(medicoSearch.getMedicos());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/clinica/{clinicId}/nome/{nome}")
    public ResponseEntity<List<MedicoDataForTableDto>> findByName(
            @PathVariable(value = "nome") String name,
            @PathVariable(value = "clinicId") long clinicId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) throws Exception {

        MedicoSearchDto medicoSearch = medicoService.findByClinicAndName(clinicId, name, page, size);

        if (medicoSearch.getMedicos() != null) {
            return ResponseEntity
                    .ok()
                    .header(ProjectConstants.HDR_TOTAL_REGISTRIES,
                            Long.toString(medicoSearch.getCountTotal()))
                    // .header("Access-Control-Allow-Headers",
                    // ProjectConstants.HDR_TOTAL_REGISTRIES)
                    .body(medicoSearch.getMedicos());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
