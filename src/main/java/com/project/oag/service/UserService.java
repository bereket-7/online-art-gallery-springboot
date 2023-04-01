package com.project.oag.service;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.NewLocationToken;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.User;
import com.project.oag.entity.VerificationToken;
import com.project.oag.exceptions.UserAlreadyExistException;
import com.project.oag.exceptions.UserNotFoundException;
public interface UserService {
	//User update(UserDto userDto) throws UserNotFoundException;
	void changePassword(User user, String password);
	void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
	User getByResetPasswordToken(String token);
	void verifyEmail(String email, String token) throws UserNotFoundException, InvalidTokenException, Throwable;
	User registerNewUser(UserDto userDto) throws UserAlreadyExistException;
	User findByUsername(String username);
	List<User> getAllUsers();
	Optional<User> getUserById(Long id);
	void deleteUser(Long id);
	User updateUser(Long id, UserDto userDto) throws  UserNotFoundException;
	User getUser(String verificationToken);
	void saveRegisteredUser(User user);
	VerificationToken getVerificationToken(String VerificationToken);
	void createVerificationTokenForUser(User user, String token);
	VerificationToken generateNewVerificationToken(String existingVerificationToken);
	String validateVerificationToken(String token);
	//Object getUsersFromSessionRegistry();
	NewLocationToken isNewLoginLocation(String username, String ip);
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


    String isValidNewLocationToken(String token);

    void addUserLocation(User user, String ip);
}

