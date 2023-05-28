package com.project.oag.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService  implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      return userRepository.findByEmail(email)
              .orElseThrow(() ->
                      new UsernameNotFoundException(
                              String.format(USER_NOT_FOUND_MSG, email)));
  }


}
