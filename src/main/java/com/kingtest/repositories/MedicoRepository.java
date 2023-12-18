package com.kingtest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingtest.entities.Medico;
import com.kingtest.repositories.pagingRepositories.MedicoPagingRepository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>, MedicoPagingRepository{
    public long countByClinicaId(long clinicId);
    public long countByClinicaIdAndFullNameContains(long clinicId, String fullName);
    public Medico findByUserId(Long userId);
    public boolean existsByEmail(String email);
}
