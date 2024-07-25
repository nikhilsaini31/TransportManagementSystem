package com.TransportManagementSystem.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;import com.TransportManagementSystem.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String username);
	
}
