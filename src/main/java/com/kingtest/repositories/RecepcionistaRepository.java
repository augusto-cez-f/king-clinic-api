package com.kingtest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingtest.entities.Recepcionista;

@Repository
public interface RecepcionistaRepository  extends JpaRepository<Recepcionista, Long>{
    public List<Recepcionista> findByClinicaId(long clinicId);
    public Recepcionista findByUserId(Long userId);
    public boolean existsByEmail(String email);
    
}
