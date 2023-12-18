package com.kingtest.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kingtest.crypto.DataEncryptionService;
import com.kingtest.dto.requests.LoginRequestDto;
import com.kingtest.dto.requests.SignupRequestDto;
import com.kingtest.entities.User;
import com.kingtest.exceptions.NotFoundException;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.exceptions.ValidationExceptionValues;
import com.kingtest.repositories.UserRepository;

@Service
public class UserService {
	private DataEncryptionService dataEncryptionService;
	private UserRepository userRepository;

	public UserService(DataEncryptionService dataEncryptionService,
			UserRepository userRepository) {
		this.dataEncryptionService = dataEncryptionService;
		this.userRepository = userRepository;
	}

	private void validateUserForRegistration(SignupRequestDto signUpRequest) throws ValidationException {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new ValidationException(ValidationExceptionValues.USER_EMAIL_ALREADY_TAKEN);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new ValidationException(ValidationExceptionValues.USER_EMAIL_ALREADY_TAKEN);
		}
	}

	public User registerUser(SignupRequestDto signUpRequest) throws ValidationException {
		validateUserForRegistration(signUpRequest);

		User user = new User(signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				signUpRequest.getPassword());

		String strRoles = signUpRequest.getRole();
		user.setRole(strRoles);

		userRepository.save(user);

		return user;
	}

	public User findByEmailAndPassword(LoginRequestDto loginRequest) throws NotFoundException {
		LoginRequestDto encryptedLoginRequest = dataEncryptionService.encryptLoginRequest(loginRequest);

		Optional<User> optionalUser = userRepository.findByUsernameAndPassword(
			encryptedLoginRequest.getUsername(),
			encryptedLoginRequest.getPassword());

		return optionalUser
			.map(user -> dataEncryptionService.decryptUser(user))
			.orElseThrow(() -> new NotFoundException("User not found"));
	}

}