package com.kingtest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kingtest.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndPassword(String username, String password);
	Optional<User> findByEmailAndPassword(String email, String password);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}