package com.project.oag.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.Customer;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.Role;
import com.project.oag.entity.User;
import com.project.oag.exceptions.UserAlreadyExistException;
import com.project.oag.repository.PasswordResetTokenRepository;
import com.project.oag.repository.RoleRepository;
import com.project.oag.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
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
    
    
    @Override
    public void registerNewUserAccount(User user) {
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email address: " + user.getEmail());
        }
        user.setEnabled(false);
       // String token = UUID.randomUUID().toString();
        //user.setToken(token);
        userRepository.save(user);
        sendConfirmationEmail(user.getEmail());
        userRepository.save(user);
    }
    
    /*
    @Override
    public void registerNewUserAccount(User user) {
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email address: " + user.getEmail());
        }
        user.setEnabled(false);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        if (user != null) {
            user.setToken(token);
            userRepository.save(user);
            sendConfirmationEmail(user.getEmail());
        }
    }*/

    /*
    @Override
    public User registerNewUserAccount(final UserDto accountDto) {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email address: " + accountDto.getEmail());
        }
        final User user = new User();
        user.setFirstname(accountDto.getFirstname());
        user.setLastname(accountDto.getLastname());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setPhone(accountDto.getPhone());
        user.setAddress(accountDto.getAddress());
        user.setSex(accountDto.getSex());
        user.setAge(accountDto.getAge());
        user.setUsername(accountDto.getUsername());
        user.setUsing2FA(accountDto.isUsing2FA());
        user.setEnabled(false);
        sendConfirmationEmail(user.getEmail());
        //user.setRoles(Arrays.asList(roleRepository.findByName("Admin")));
        return userRepository.save(user);
    }*/
    
    @Override
    public void uploadProfile(User user) {
        user.setPhotos(user.getPhotos());
        userRepository.save(user);
    }    

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


    @Override
    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME,
                user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }

    @Override
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

    @Override
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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public List<User> getUsersByRole(String name) {
        Role role = new Role(name);
        return userRepository.findByRolesContains(role);
    }
    
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setEnabled(true);
            user.setPhone(updatedUser.getPhone());
            user.setFirstname(updatedUser.getFirstname());
            user.setLastname(updatedUser.getLastname());
            user.setAddress(updatedUser.getAddress());
            user.setAge(updatedUser.getAge());
            return userRepository.save(user);
        }
        return null;
    }
    
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

	@Override
	public User getUser(String verificationToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendConfirmationEmail(String email) {
	    	User user =  userRepository.findByEmail(email);
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom(senderEmail);
	        message.setTo(email);
	        message.setSubject("Confirm your registration");
	        Random random = new Random();
	        String confirmationCode = String.format("%06d", random.nextInt(1000000));
	        user.setToken(confirmationCode);
	        userRepository.save(user);
	        
	        message.setText("Please enter the following confirmation code on our website to confirm your registration: " + confirmationCode);
	        mailSender.send(message);
	        
	    }
	    
	  	@Override
	    public void confirmRegistration(String email, String confirmationCode) {
	    User user = userRepository.findByEmail(email);
	    if(user == null){
	        throw new IllegalArgumentException("Invalid email");
	    }
	    else{
	        if (!user.getToken().equals(confirmationCode)) {
	            throw new IllegalArgumentException("Invalid confirmation code");
	        }
	        user.setEnabled(true);
	        user.setToken(null); // Remove the confirmation code after successful confirmation
	        userRepository.save(user);
	    }
	}

}
