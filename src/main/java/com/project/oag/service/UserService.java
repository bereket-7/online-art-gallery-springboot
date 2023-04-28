package com.project.oag.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.Customer;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.User;

public interface UserService {

	User getUser(String verificationToken);

	void saveRegisteredUser(User user);

	void deleteUser(User user);

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

	void uploadProfile(User user);

	void registerNewUserAccount(UserDto user);

	void registerNewUserAccount(User user);


}
