package com.metaphorce.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.metaphorce.security.mapper.UserMapper;
import com.metaphorce.security.model.User;
import com.metaphorce.security.repository.UserRepository;

/**
 * Service for operations on Authentication
 * @author Juan Chavez
 * @since May/28/2024
 */
@Service
public class AuthenticationService implements UserDetailsService {
	@Autowired
    private UserRepository repository;
        
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> userDetails = repository.findByName(username);
		
		return userDetails.map(UserMapper.INSTANCE::entityToDto)
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
	}
	
	/**
	 * Register a User for Authentication and Authorization
	 * @param userInput Data of new User
	 * @return New User registered
	 * @author Juan Chavez
	 * @since May/28/2024
	 */
	public User signUp(User userInput) {
		userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));
		return repository.save(userInput);
	}
}
