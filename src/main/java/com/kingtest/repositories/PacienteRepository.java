package com.kingtest.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingtest.entities.Paciente;
import com.kingtest.repositories.pagingRepositories.PacientePagingRepository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, PacientePagingRepository{
    public List<Paciente> findByFullNameContains(String fullName);
    public boolean existsByFullName(String fullName);
    public boolean existsByFullNameAndIdIsNot(String fullName, Long id);
    public Optional<Paciente> findById(Long userId);
    public long countByFullNameContains(String fullName);
}
