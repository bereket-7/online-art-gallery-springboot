package com.project.oag.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.User;
import com.project.oag.entity.VerificationToken;

public interface UserService {
	User registerNewUserAccount(UserDto accountDto);

	User getUser(String verificationToken);

	void saveRegisteredUser(User user);

	void deleteUser(User user);

	void createVerificationTokenForUser(User user, String token);

	VerificationToken getVerificationToken(String VerificationToken);

	VerificationToken generateNewVerificationToken(String token);

	void createPasswordResetTokenForUser(User user, String token);

	User findUserByEmail(String email);

	PasswordResetToken getPasswordResetToken(String token);

	Optional<User> getUserByPasswordResetToken(String token);

	Optional<User> getUserByID(long id);

	void changeUserPassword(User user, String password);

	boolean checkIfValidOldPassword(User user, String password);

	String validateVerificationToken(String token);

	String generateQRUrl(User user) throws UnsupportedEncodingException;

	User updateUser2FA(boolean use2FA);

	List<String> getUsersFromSessionRegistry();

	public List<User> getAllUsers();

}
