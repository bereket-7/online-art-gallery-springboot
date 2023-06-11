//package com.project.oag.security.service;
//
//import com.project.oag.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//@Configuration
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        try {
//            return userRepository.findByEmail(email)
//                    .orElseThrow(() -> new Exception("user Not found "));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}