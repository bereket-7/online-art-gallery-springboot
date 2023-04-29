package com.project.oag.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.User;

public interface UserService {

	void createPasswordResetTokenForUser(User user, String token);

	User findUserByEmail(String email);

	PasswordResetToken getPasswordResetToken(String token);

	Optional<User> getUserByPasswordResetToken(String token);

	Optional<User> getUserByID(long id);

	void changeUserPassword(User user, String password);

	boolean checkIfValidOldPassword(User user, String password);

	String generateQRUrl(User user) throws UnsupportedEncodingException;

	User updateUser2FA(boolean use2FA);

	List<String> getUsersFromSessionRegistry();

	public List<User> getAllUsers();

	User addUser(User user);

	void deleteUser(Long id);

	User updateUser(Long id, User updatedUser);

	User getUserById(Long id);

	List<User> getUsersByRole(String string);

	void confirmRegistration(String email, String confirmationCode);
	
	void sendConfirmationEmail(String email);

	void registerUser(UserDto userDto);

	void uploadProfilePhoto(Long userId, MultipartFile image) throws IOException;

	User authenticateUser(String username, String password);


}
