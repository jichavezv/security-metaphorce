package com.metaphorce.security.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.metaphorce.security.dto.UserAuthDTO;
import com.metaphorce.security.model.User;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	@Mapping(source = "authorities", target = "roles", qualifiedByName = "authoritiesToRoles")
	User dtoToEntity(UserAuthDTO dto);
	
	@Named("authoritiesToRoles")
	public static String authoritiesToRoles(List<GrantedAuthority> authorities) {
		return authorities.stream()
				.map(e -> String.valueOf(e))
				.collect(Collectors.joining(","));
	}
		
	@Mapping(source = "roles", target = "authorities", qualifiedByName = "rolesToAuthorities")
	UserAuthDTO entityToDto(User entity);
	
	@Named("rolesToAuthorities")
	public static List<GrantedAuthority> rolesToAuthorities(String roles) {
		return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
	}
}
