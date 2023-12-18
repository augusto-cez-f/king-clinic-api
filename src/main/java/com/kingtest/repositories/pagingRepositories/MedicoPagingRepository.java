package com.kingtest.repositories.pagingRepositories;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kingtest.entities.Medico;


@Repository
public interface MedicoPagingRepository extends PagingAndSortingRepository<Medico, Long> {
    public List<Medico> findByClinicaIdOrderByFullName(long clinicId, Pageable pageable);
    public List<Medico> findByClinicaIdAndFullNameContainsOrderByFullName(long clinicId, String name, Pageable pageable);
}