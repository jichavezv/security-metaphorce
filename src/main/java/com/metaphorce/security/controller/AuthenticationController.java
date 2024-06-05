package com.metaphorce.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metaphorce.security.dto.LoginResponseDTO;
import com.metaphorce.security.dto.LoginUserDTO;
import com.metaphorce.security.dto.UserAuthDTO;
import com.metaphorce.security.mapper.UserMapper;
import com.metaphorce.security.model.User;
import com.metaphorce.security.service.AuthenticationService;
import com.metaphorce.security.service.JwtService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Endpoints for Authentication on API
 * @author Juan Chavez
 * @since May/29/2024
 */
@RequestMapping("/api/v1/auth")
@RestController
@Slf4j
public class AuthenticationController {
	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserMapper mapper;

	/**
	 * Endpoint for test 
	 * @return Welcome Message
	 * @author Juan Chavez
	 * @since May/29/2024
	 */
	@GetMapping("/welcome")
	public String welcome() {
		return "This endpoint is not secure yet";
	}

	/**
	 * Endpoint with Post method to add user for Authentication on API
	 * @param registerUserDto Resgister data
	 * @return User Data register
	 * @author Juan Chavez
	 * @since May/29/2024
	 */
	@PostMapping("/signup")
	public ResponseEntity<UserAuthDTO> register(@Valid @RequestBody UserAuthDTO registerUserDto) {
		log.info("User to register: " + registerUserDto);
		ResponseEntity<UserAuthDTO> response = null;
		User registeredUser = authenticationService.signUp(mapper.dtoToEntity(registerUserDto));
		
		if(registeredUser != null) {
			response = ResponseEntity.ok(mapper.entityToDto(registeredUser));
			log.info("User registered: " + registeredUser);
		} else {
			response = ResponseEntity.badRequest().build();
			log.info("Error to sign up User: " + registeredUser);
		}

		return response;
	}

	/**
	 * Endpoint to login a User 
	 * @param loginUserDto Login data
	 * @return Token for Authorization
	 * @author Juan Chavez
	 * @since May/29/2024 
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> authenticate(@Valid @RequestBody LoginUserDTO loginUserDto) {
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getName(), loginUserDto.getPassword()));
		if (authentication.isAuthenticated()) {
			String token = jwtService.generateToken(loginUserDto.getName());
			long expiration = jwtService.extractExpiration(token).getTime();
			log.debug("Authenticated: " + token + " - " + expiration);
			return ResponseEntity.ok(LoginResponseDTO.builder()
					.token(token)
					.expiration(expiration)
					.build());
		} else {
			log.info("User not authenticated: " + loginUserDto);
			throw new UsernameNotFoundException("Invalid user request!");		}
	}


}
