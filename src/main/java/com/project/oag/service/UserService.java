package com.project.oag.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.oag.entity.User;
import com.project.oag.registration.token.ConfirmationToken;
import com.project.oag.registration.token.ConfirmationTokenService;
import com.project.oag.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService  implements UserDetailsService{
     private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	@Autowired
    private ConfirmationTokenService confirmationTokenService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
  }



    public String signUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return userRepository.enableUser(email);
    }


}
