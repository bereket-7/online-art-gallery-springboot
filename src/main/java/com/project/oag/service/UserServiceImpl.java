package com.project.oag.service;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maxmind.geoip2.DatabaseReader;
import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.NewLocationToken;
import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.User;
import com.project.oag.entity.UserLocation;
import com.project.oag.entity.VerificationToken;
import com.project.oag.exceptions.UserAlreadyExistException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.repository.NewLocationTokenRepository;
import com.project.oag.repository.PasswordResetTokenRepository;
import com.project.oag.repository.RoleRepository;
import com.project.oag.repository.UserLocationRepository;
import com.project.oag.repository.UserRepository;
import com.project.oag.repository.VerificationTokenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    @Qualifier("GeoIPCountry")
    private DatabaseReader databaseReader;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private NewLocationTokenRepository newLocationTokenRepository;

    @Autowired
    private Environment env;

	@Autowired
	private JavaMailSender mailSender;
	
    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "SpringRegistration";
	


    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public User registerNewUser(UserDto userDto) throws UserAlreadyExistException {
        Optional<User> existingUser = Optional.of(findByEmail(userDto.getEmail()));
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User with email " + userDto.getEmail() + " already exists");
        }
        
        	User user = new User(userDto.getFirstname(),userDto.getLastname(),userDto.getPhone(),
					userDto.getAddress(),userDto.getEmail(),userDto.getSex(),userDto.getAge(),
					userDto.getUsername(),passwordEncoder.encode(userDto.getPassword()),userDto.getRole());
					return userRepository.save(user);

        //sendVerificationEmail(user);
        //return user;
    }
    
    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }
    
    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }
    @Override
    public void deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        userRepository.delete(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }


    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }
    
    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEmailVerified(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }
    
    @Override
    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }

    @Override
    public User updateUser2FA(boolean use2FA) {
        final Authentication curAuth = SecurityContextHolder.getContext()
            .getAuthentication();
        User currentUser = (User) curAuth.getPrincipal();
        currentUser.setUsing2FA(use2FA);
        currentUser = userRepository.save(currentUser);
        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
        SecurityContextHolder.getContext()
            .setAuthentication(auth);
        return currentUser;
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
        return Optional.ofNullable(passwordTokenRepository.findByToken(token) .getUser());
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
    public NewLocationToken isNewLoginLocation(String username, String ip) {

        if(!isGeoIpLibEnabled()) {
            return null;
        }

        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            final String country = databaseReader.country(ipAddress)
                .getCountry()
                .getName();
            System.out.println(country + "====****");
            final User user = userRepository.findByEmail(username);
            final UserLocation loc = userLocationRepository.findByCountryAndUser(country, user);
            if ((loc == null) || !loc.isEnabled()) {
                return createNewLocationToken(country, user);
            }
        } catch (final Exception e) {
            return null;
        }
        return null;
    }
    
    @Override
    public String isValidNewLocationToken(String token) {
        final NewLocationToken locToken = newLocationTokenRepository.findByToken(token);
        if (locToken == null) {
            return null;
        }
        UserLocation userLoc = locToken.getUserLocation();
        userLoc.setEnabled(true);
        userLoc = userLocationRepository.save(userLoc);
        newLocationTokenRepository.delete(locToken);
        return userLoc.getCountry();
    }

    @Override
    public void addUserLocation(User user, String ip) {

        if(!isGeoIpLibEnabled()) {
            return;
        }

        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            final String country = databaseReader.country(ipAddress)
                .getCountry()
                .getName();
            UserLocation loc = new UserLocation(country, user);
            loc.setEnabled(true);
            userLocationRepository.save(loc);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isGeoIpLibEnabled() {
        return Boolean.parseBoolean(env.getProperty("geo.ip.lib.enabled"));
    }

    private NewLocationToken createNewLocationToken(String country, User user) {
        UserLocation loc = new UserLocation(country, user);
        loc = userLocationRepository.save(loc);

        final NewLocationToken token = new NewLocationToken(UUID.randomUUID()
            .toString(), loc);
        return newLocationTokenRepository.save(token);
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
                    return o.toString()
            ;
                }
            }).collect(Collectors.toList());
    }
    
    
	@Override
	public User updateUser(Long id, UserDto userDto) throws UserNotFoundException {
		  User findUser = userRepository.findByUsername(userDto.getUsername());;
	       if (findUser == null) {
	           throw new UserNotFoundException("User with username " + userDto.getUsername() + " already exists.");
	       }
	       findUser.setAddress(userDto.getAddress());
	       findUser.setFirstname(userDto.getFirstname());
	       findUser.setLastname(userDto.getLastname());
	       findUser.setUsername(userDto.getUsername());
	       findUser.setPhone(userDto.getPhone());
	       return userRepository.save(findUser);
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
 /*
    private void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        user.setVerificationCode(token);
        userRepository.save(user);

        String subject = "Verify your email address";
        String confirmationUrl = "/verify-email?email=" + user.getEmail() + "&token=" + token;
        String message = "To confirm your account, please click here: " + confirmationUrl;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(senderEmail);
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

	@Override
    public void verifyEmail(String email, String token) throws Throwable {
      User user = userRepository.findByEmail(email);
      	if(user == null) {
               throw new UserNotFoundException("User with email " + email + " not found");
             }
        if(!user.getVerificationCode().equals(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        user.setVerificationCode(null);
        user.setEmailVerified(true);
        userRepository.save(user);
    }*/
	
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    /*@Transactional
    public User findByUsername(String username) {
        return userRepository.findByEmail(username);
    }*/
    @Transactional
    public boolean userEmailExists(String email){
        return userRepository.findByEmail(email) != null;
    }
    @Transactional
    public boolean userUsernameExists(String username){
    	return userRepository.findByUsername(username) != null;
    }
	@Override
	public User findByUsername(String username) {
		return userRepository.findByEmail(username);
	}
    /**
	@Override
	public User update(UserDto userDto)throws UserNotFoundException {
	     User findUser = userRepository.findByUsername(userDto.getUsername());
	       if (findUser == null) {
	           throw new UserNotFoundException("User with username " + userDto.getUsername() + " already exists.");
	       }
	       findUser.setAddress(userDto.getAddress());
	       findUser.setFirstname(userDto.getFirstname());
	       findUser.setLastname(userDto.getLastname());
	       findUser.setUsername(userDto.getUsername());
	       findUser.setPhone(userDto.getPhone());
	       return userRepository.save(findUser);
	}*/
	
	  public void updateResetPasswordToken(String token, String email) throws UserNotFoundException  {
	        User user = userRepository.findByEmail(email);
	        if (user != null) {
	        	user.setResetPasswordToken(token);
	        	userRepository.save(user);
	        } else {
	            throw new UserNotFoundException("Could not find any user with the email " + email);
	        }
	    }
	   
	  public User getByResetPasswordToken(String token) {
	        return userRepository.findByResetPasswordToken(token);
	    }
	     
	  public void changePassword(User user, String newPassword) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(newPassword);
	        user.setPassword(encodedPassword);
	        user.setResetPasswordToken(null);
	        userRepository.save(user);
	    }

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	} 

	@Override
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public void deleteUser(Long id) {
		 userRepository.deleteById(id);	
	}

	@Override
	public void verifyEmail(String email, String token) throws UserNotFoundException, InvalidTokenException, Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		// TODO Auto-generated method stub
		
	}

}
