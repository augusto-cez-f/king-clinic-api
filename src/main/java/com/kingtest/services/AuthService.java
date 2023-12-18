package com.kingtest.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingtest.crypto.JwtService;
import com.kingtest.dto.requests.LoginRequestDto;
import com.kingtest.dto.responses.jwtResponses.JwtResponse;
import com.kingtest.entities.User;
import com.kingtest.exceptions.NotFoundException;
import com.kingtest.services.UserDataService.UserData;
import com.kingtest.services.UserDataService.UserDataService;
import com.kingtest.services.UserDataService.UserDataServiceFactory;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	private UserDataServiceFactory userDataServiceFactory;
	private JwtService jwtService;
    private UserService userService;

    public ResponseEntity<?> login(LoginRequestDto loginRequest) throws Exception {
		try {
			User user = userService.findByEmailAndPassword(loginRequest);
			UserData userData = findUserData(user);
			JwtResponse jwtResponse = createJwtResponse(user, userData);

			return ResponseEntity.ok().body(jwtResponse);

		} catch (NotFoundException notFoundException) {
			return ResponseEntity
					.status(404)
					.body(notFoundException.getMessage());
		}
	}

	private UserData findUserData(User user){
		UserDataService userDataService = userDataServiceFactory.getUserDataService(user);
		UserData userData = userDataService.login(user.getId());
		return userData;
	}

	private JwtResponse createJwtResponse(User user, UserData userData){
		String jwt = jwtService.makeBodyJwt(user);

		JwtResponse response = JwtResponse.builder()
			.token(jwt)
			.type(JwtResponse.BEARER_TYPE)
			.id(user.getId())
			.username(user.getUsername())			
			.email(user.getEmail())
			.roles(List.of(user.getRole()))
			.userData(userData)
		.build();

		return response;
	}
}
