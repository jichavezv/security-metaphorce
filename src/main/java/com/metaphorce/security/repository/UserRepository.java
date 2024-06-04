package com.metaphorce.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metaphorce.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public Optional<User> findByName(String name);
}
