package com.metaphorce.security.mapper;

import org.mapstruct.Mapper;

import com.metaphorce.security.dto.UserAuthDTO;
import com.metaphorce.security.model.User;

@Mapper
public interface UserMapper {
	User dtoToEntity(UserAuthDTO source);
	UserAuthDTO entityToDto(User source);
}
