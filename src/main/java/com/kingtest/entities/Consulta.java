package com.kingtest.entities;

import javax.persistence.JoinColumn;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.kingtest.dto.requests.ConsultaInputDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table(name= "consultas")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="data")
    private String date;

    @Column(name="hora")
    private String hour;

    @Column(name="endereco")
    private String address;

    @Column(name="valor")
    private double price;

    @Column(name="valor_medico")
    private double priceDoctor;

    @Column(name="pago")
    private boolean paid;

    @Column(name="realizada")
    private boolean fulfilled;

    @Column(name="motivo")
    private String motive;

    @Column(name="metodo_pagamento")
    private String paymentMethod;

    @Column(name="inicial_id")
    private Long inicialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", insertable = false, updatable = false)
	@Fetch(FetchMode.JOIN)
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", insertable = false, updatable = false)
	@Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private Paciente paciente;

    public Consulta(ConsultaInputDto consultaInputDTO) {
        if( consultaInputDTO.getId() != null ) {
            this.setId(consultaInputDTO.getId());
        }
        
        this.setAddress(consultaInputDTO.getAddress());
        this.setDate(consultaInputDTO.getDate());
        this.setHour(consultaInputDTO.getHour());
        this.setMedico(new Medico(consultaInputDTO.getMedicoId()));
        this.setPaciente(new Paciente(consultaInputDTO.getPacienteId()));        
        this.setInicialId(consultaInputDTO.getInitialId());
        this.setMotive(consultaInputDTO.getMotive());
        this.setPaid(consultaInputDTO.isPaid());
        this.setPrice(consultaInputDTO.getPrice());
        this.setPaymentMethod(consultaInputDTO.getPaymentMethod());
        this.setPriceDoctor(consultaInputDTO.getPriceDoctor());
    }
}
