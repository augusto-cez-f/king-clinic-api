package com.kingtest.dto.requests;

import com.kingtest.entities.Consulta;

import lombok.Data;

@Data
public class ConsultaInputDto {
    private Long id;
    private String date;
    private String hour;
    private String address;
    private Double price;
    private Double priceDoctor;
    private String motive;
    private String paymentMethod;
    private boolean isPaid;
    private Long initialId;
    private Long medicoId;
    private Long pacienteId;

    public Consulta toEntity(){
        return new Consulta(this);
    }
}
