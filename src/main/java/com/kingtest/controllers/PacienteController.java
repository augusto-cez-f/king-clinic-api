package com.kingtest.controllers;
import java.util.List;

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

import com.kingtest.dto.requests.PacienteCreateDto;
import com.kingtest.dto.responses.errors.ErrorMessageResponse;
import com.kingtest.entities.Paciente;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.services.PacienteService;
import com.kingtest.services.dto.PacienteSearchDto;
import com.kingtest.util.ProjectConstants;

import jakarta.transaction.Transactional;

@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders = ProjectConstants.HDR_TOTAL_REGISTRIES)
@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    PacienteService pacienteService;

    @PostMapping
    @Transactional(rollbackOn = { Exception.class })
    public ResponseEntity<?> create(@RequestBody PacienteCreateDto paciente) {
        try {
            
            Paciente pacienteCreated = pacienteService.create(paciente);

            return new ResponseEntity<Paciente>(pacienteCreated, HttpStatus.CREATED);

        } catch (ValidationException validationE) {
            return ResponseEntity
                    .badRequest()
                    .body( new ErrorMessageResponse(validationE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Do better error management here
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<Paciente>> findAll(
            @RequestParam("page") int page) throws Exception {

        PacienteSearchDto pacientesSearch = pacienteService.findAll(page);

        if (pacientesSearch.getPacientes() != null) {
            return ResponseEntity
                    .ok()
                    .header(ProjectConstants.HDR_TOTAL_REGISTRIES, Long.toString(pacientesSearch.getCountTotal()))
                    .header("Access-Control-Allow-Headers", ProjectConstants.HDR_TOTAL_REGISTRIES)
                    .body(pacientesSearch.getPacientes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<List<Paciente>> findByName(
            @PathVariable(value = "nome") String name,
            @RequestParam("page") int page) throws Exception {

        PacienteSearchDto pacientesSearch = pacienteService.findByPartialName(name, page);

        if (pacientesSearch.getPacientes() != null) {
            return ResponseEntity
                    .ok()
                    .header(ProjectConstants.HDR_TOTAL_REGISTRIES, Long.toString(pacientesSearch.getCountTotal()))
                    .body(pacientesSearch.getPacientes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> editPaciente(
            @PathVariable(value = "id") String id,
            @RequestBody PacienteCreateDto paciente) throws Exception {

        try {
            paciente.setId(Long.valueOf(id));

            Paciente pacienteEditado = pacienteService.edit(paciente);

            return ResponseEntity
                    .ok()
                    .body(pacienteEditado);
        } catch (ValidationException validationE) {
            return ResponseEntity
                    .badRequest()
                    .body( new ErrorMessageResponse(validationE.getMessage()));       
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }
}
