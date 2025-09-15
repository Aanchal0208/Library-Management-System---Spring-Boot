package com.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.entity.Users;
import com.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = usersRepository.findByemail(username);
		if(user == null) {
			throw new UsernameNotFoundException("User Not found");
		}
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
			    user.getPassword(),
			    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
			);
	}

}

//spring security takes the role in the listing form even if the role is single to authorized
