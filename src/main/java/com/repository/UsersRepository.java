package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{

	public Users findByemail(String email);
	
	long count();
}
