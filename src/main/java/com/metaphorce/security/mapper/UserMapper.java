package com.metaphorce.security.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.metaphorce.security.dto.UserAuthDTO;
import com.metaphorce.security.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
	
	@Mapping(target = "roles", qualifiedByName = "convertToAuthorities")
	UserAuthDTO entityToDto(User entity);
	
	@Mapping(ignore = true, target = "id")
	@Mapping(target = "roles", qualifiedByName = "convertToRoles")
	User dtoToEntity(UserAuthDTO dto);
	
	@Named("convertToAuthorities")
	default List<String> convertToAuthorities(String roles) {
		return Arrays.asList(roles.split(","));
	}
	
	@Named("convertToRoles")
	default String convertToRoles(List<String> authorities) {
		return authorities.stream()
				.map(e -> String.valueOf(e))
				.collect(Collectors.joining(","));
	}
}
