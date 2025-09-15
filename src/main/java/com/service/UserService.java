package com.service;

import org.springframework.stereotype.Service;

import com.entity.Users;

@Service
public interface UserService {
public Users saveusers(Users user);

public long totalregisteres();

public Users findByEmail(String email);
}
