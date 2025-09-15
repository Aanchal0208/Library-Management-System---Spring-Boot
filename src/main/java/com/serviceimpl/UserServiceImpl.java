package com.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.entity.Users;
import com.repository.UsersRepository;
import com.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Users saveusers(Users user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return usersRepository.save(user);
	}

	@Override
	public long totalregisteres() {
		return usersRepository.count();
	}

	@Override
	public Users findByEmail(String email) {
		return usersRepository.findByemail(email);
	}

}
