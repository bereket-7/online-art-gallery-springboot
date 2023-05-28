package com.project.oag.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.common.FileUploadUtil;
import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.Role;
import com.project.oag.entity.User;
import com.project.oag.exceptions.UserAlreadyExistException;
import com.project.oag.repository.PasswordResetTokenRepository;
import com.project.oag.repository.RoleRepository;
import com.project.oag.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl {
	
    private String path = "src/main/resources/static/img/user-images/";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Value("${email.throttle.delay}")
    private long emailThrottleDelay;
    
     @Value("${spring.mail.username}")
     private String senderEmail;

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "OnlineArtGallery";

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }
    
  
    public User authenticateUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(username, password);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            return null;
        }
    }
//    
//    @Override
//    public void registerUser(UserDto userDto) {
//        User user = new User();
//        user.setFirstname(userDto.getFirstname());
//        user.setLastname(userDto.getLastname());
//        user.setEmail(userDto.getEmail());
//        user.setPhone(userDto.getPhone());
//        user.setAddress(userDto.getAddress());
//        user.setSex(userDto.getSex());
//        user.setAge(userDto.getAge());
//        user.setUsername(userDto.getUsername());
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//        List<Role> roles = new ArrayList<>();
//        for (Role.RoleType roleType : userDto.getRoles()) {
//            Role role = roleRepository.findByName(roleType);
//            if (role == null) {
//                throw new EntityNotFoundException("Role not found with name: " + roleType);
//            }
//            roles.add(role);
//        }
//        user.setRoles(roles);
//        if (emailExists(user.getEmail())) {
//            throw new UserAlreadyExistException(
//                "There is an account with that email address: " + user.getEmail());
//        }
//        userRepository.save(user);
//    }
    

  
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }


    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }


    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }


    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

//    @Override
//    public void changeUserPassword(final User user, final String password) {
//        user.setPassword(passwordEncoder.encode(password));
//        userRepository.save(user);
//    }

    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME,
                user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }

    public User updateUser2FA(boolean use2FA) {
        final Authentication curAuth = SecurityContextHolder.getContext()
                .getAuthentication();
        User currentUser = (User) curAuth.getPrincipal();
        currentUser.setUsing2FA(use2FA);
        currentUser = userRepository.save(currentUser);
        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(),
                curAuth.getAuthorities());
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
        return currentUser;
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }


    public List<String> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals()
                .stream()
                .filter((u) -> !sessionRegistry.getAllSessions(u, false)
                        .isEmpty())
                .map(o -> {
                    if (o instanceof User) {
                        return ((User) o).getEmail();
                    } else {
                        return o.toString();
                    }
                }).collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
//    @Override
//    public User updateUser(Long id, User updatedUser) {
//        User user = userRepository.findById(id).orElse(null);
//        if (user != null) {
//            user.setUsername(updatedUser.getUsername());
//            user.setEmail(updatedUser.getEmail());
//            user.setEnabled(true);
//            user.setPhone(updatedUser.getPhone());
//            user.setFirstname(updatedUser.getFirstname());
//            user.setLastname(updatedUser.getLastname());
//            user.setAddress(updatedUser.getAddress());
//            user.setAge(updatedUser.getAge());
//            return userRepository.save(user);
//        }
//        return null;
//    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    
    /*   Email confiramation    */
//	  @Override
//	    public void sendConfirmationEmail(String email) {
//	        User user = userRepository.findByEmail(email);
//	        if (user == null) {
//	            throw new IllegalArgumentException("Invalid email");
//	        }
//	        SimpleMailMessage message = new SimpleMailMessage();
//	        message.setFrom(senderEmail);
//	        message.setTo(email);
//	        message.setSubject("Confirm your registration");
//	        Random random = new Random();
//	        String confirmationCode = String.format("%06d", random.nextInt(1000000));
//	        user.setToken(confirmationCode);
//	        userRepository.save(user);
//	        message.setText("Please enter the following confirmation code on our website to confirm your registration: " + confirmationCode);
//	        mailSender.send(message);
//	    }
    private String generateConfirmationCode() {
        int codeLength = 6; 
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            code.append(randomChar);
        }
        return code.toString();
    } 
    

	  public void sendConfirmationEmail(String email) {
	      User user = userRepository.findByEmail(email);
	      if (user == null) {
	          throw new IllegalArgumentException("Invalid email");
	      }
	      LocalDateTime expirationTime = LocalDateTime.now().plusHours(24); // Set expiration time to 24 hours from now
	      user.setExpirationTime(expirationTime);

	      String confirmationCode = generateConfirmationCode();
	      user.setToken(confirmationCode);
	      userRepository.save(user);

	      SimpleMailMessage message = new SimpleMailMessage();
	      message.setFrom(senderEmail);
	      message.setTo(email);
	      message.setSubject("Confirm your registration");
	      message.setText("Please enter the following confirmation code on our website to confirm your registration: " + confirmationCode);
	      mailSender.send(message);
	  }

	  	
//	  	@Override
//	    public void confirmRegistration(String email, String confirmationCode) {
//	        User user = userRepository.findByEmail(email);
//	        if (user == null) {
//	            throw new IllegalArgumentException("Invalid email");
//	        } else {
//	            if (!user.getToken().equals(confirmationCode)) {
//	                throw new IllegalArgumentException("Invalid confirmation code");
//	            }
//	            user.setEnabled(true);
//	            user.setToken(null);
//	            userRepository.save(user);
//	        }
//	    }
	  	
	  	public void confirmRegistration(String email, String confirmationCode) {
	  	    User user = userRepository.findByEmail(email);
	  	    if (user == null) {
	  	        throw new IllegalArgumentException("Invalid email");
	  	    } else {
	  	        if (!user.getToken().equals(confirmationCode)) {
	  	            throw new IllegalArgumentException("Invalid confirmation code");
	  	        }
	  	        if (user.getExpirationTime().isBefore(LocalDateTime.now())) {
	  	            throw new IllegalArgumentException("Confirmation code has expired");
	  	        }
	  	        if (user.isEnabled()) {
	  	            throw new IllegalArgumentException("User is already enabled");
	  	        }
	  	        user.setEnabled(true);
	  	        user.setToken(null);
	  	        userRepository.save(user);
	  	    }
	  	}


}
