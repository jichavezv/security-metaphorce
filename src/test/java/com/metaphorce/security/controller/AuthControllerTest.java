package com.metaphorce.security.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.metaphorce.security.dto.LoginResponseDTO;
import com.metaphorce.security.dto.LoginUserDTO;
import com.metaphorce.security.dto.UserAuthDTO;
import com.metaphorce.security.model.User;
import com.metaphorce.security.service.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class AuthControllerTest {
	@Autowired
	private AuthenticationController controller;
	
	@Autowired
	private AuthenticationService service;
		
	@BeforeEach
	public void setUp() {
		service.signUp(User.builder()
				.name("UserOne")
				.email("user1@web.com")
				.password("123456")
				.roles("admin,oper")
				.build());
	}
	
	@Test
	public void testWelcome() {
		String response = controller.welcome();
		
		assertNotNull(response);
		assertEquals(response, "This endpoint is not secure yet");
	}
	
	@Test
	public void testSignUp() {
		UserAuthDTO newUser = UserAuthDTO.builder()
				.name("NewUser")
				.email("new-user@web.com")
				.password("123456")
				.roles(Arrays.asList("admin,oper".split(",")))
				.build();
		
		ResponseEntity<UserAuthDTO> userSignedUp = controller.register(newUser);
		UserAuthDTO dto = userSignedUp.getBody();
		
		assertNotNull(dto);
		assertEquals(dto.getName(), newUser.getName());
	}
	
	@Test
	public void testLogin() {
		ResponseEntity<LoginResponseDTO> response = controller.authenticate(LoginUserDTO.builder()
				.name("UserOne")
				.password("123456")
				.build());
		
		LoginResponseDTO loggedUser = response.getBody();
		log.info("Logged User: " + loggedUser);
		
		assertNotNull(loggedUser);
		assertNotNull(loggedUser.getToken());
		assertNotNull(loggedUser.getExpiration());
	}
}
