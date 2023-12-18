package com.kingtest.dto.responses;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingtest.entities.Consulta;


@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>{
    public List<Consulta> findByDateStartsWith(String dateStart);
    public List<Consulta> findByDateOrderByHour(String date);
    public List<Consulta> findByDateStartsWithAndMedicoIdOrderByHour(String date, Long medicoId);
    public List<Consulta> findByDateStartsWithAndPaymentMethodOrderByHour(String date, String paymentMethod);
    public List<Consulta> findByDateStartsWithAndMedicoIdAndPaymentMethodOrderByHour(String date, Long medicoId, String paymentMethod);
}
