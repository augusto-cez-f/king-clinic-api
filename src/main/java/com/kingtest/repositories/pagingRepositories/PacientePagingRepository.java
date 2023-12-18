package com.kingtest.repositories.pagingRepositories;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kingtest.entities.Paciente;

@Repository
public interface PacientePagingRepository extends PagingAndSortingRepository<Paciente, Long> {
    public Page<Paciente> findAllByOrderByFullName(Pageable pageable);
    public List<Paciente> findByFullNameContainsOrderByFullName(String fullName, Pageable pageable);
}