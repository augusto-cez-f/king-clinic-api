package com.kingtest.dto.requests;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequestDto {
	@NotBlank
  	private String username;
	@NotBlank
	private String password;
}
