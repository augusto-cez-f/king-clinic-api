package com.kingtest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.kingtest.dto.requests.ConsultaInputDto;
import com.kingtest.entities.Consulta;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.services.ConsultaService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    @Autowired
    private ConsultaService consultaService;

    @GetMapping("year/{year}/month/{month}")
    public ResponseEntity<List<Consulta>> findByMonthAndYear(
        @PathVariable(value = "year") String year,
        @PathVariable(value = "month") String month) {
        List<Consulta> consultas = consultaService.findByMonthAndYear(month, year);

        if(consultas != null) {
            return ResponseEntity.ok().body(consultas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("date/{date}")
    public ResponseEntity<List<Consulta>> findByDate(
        @PathVariable(value = "date") String date) {
        List<Consulta> consultas = consultaService.findByDate(date);
        
        if(consultas != null) {
            return ResponseEntity
                .ok()
                .body(consultas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("relatorio/{date}")
    public ResponseEntity<List<Consulta>> findReportByDateAndFilters(
        @PathVariable(value = "date") String date,
        @RequestParam(required = false) Long doctorId,
        @RequestParam(required = false) String paymentMethod) {
        List<Consulta> consultas = consultaService.findReportByDateAndFilters(date, doctorId, paymentMethod);
        
        if(consultas != null) {
            return ResponseEntity.ok().body(consultas);
        } 
        
        return ResponseEntity.notFound().build();        
    }

    @PostMapping
    public ResponseEntity<Consulta> create(
        @RequestBody ConsultaInputDto consulta) {
        consultaService.create(consulta);
        
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> update(
        @RequestBody ConsultaInputDto consulta,
        @PathVariable(value = "id") String id) throws Exception {
            
        consultaService.update(consulta, id);
        
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Object> payForConsultation(
        @PathVariable(value = "id") String id) throws Exception {
            
        try {
            consultaService.payConsultation(id);
            
            return ResponseEntity.noContent().build();
        } catch (ValidationException validationE) {
            return ResponseEntity.badRequest().body(validationE.getMessage());
        }
    }

    @PutMapping("/{id}/realizada")
    public ResponseEntity<Object> confirmThatConsultationOccurred(
        @PathVariable(value = "id") String id) throws Exception {
            
        try {
            consultaService.payConsultation(id);
            
            return ResponseEntity.noContent().build();
        } catch (ValidationException validationE) {
            return ResponseEntity.badRequest().body(validationE.getMessage());
        }
    }
}
