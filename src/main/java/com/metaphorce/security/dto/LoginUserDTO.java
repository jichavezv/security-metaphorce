package com.metaphorce.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class for Login
 * @author Juan Chavez
 * @since May/28/2024
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoginUserDTO {
	@NotNull
	@NotBlank
	private String name;
	
	@NotNull
	@NotBlank
	private String password;
}
