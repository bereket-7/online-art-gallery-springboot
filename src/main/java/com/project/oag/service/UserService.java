package com.project.oag.service;

import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.Role;
import com.project.oag.entity.User;
import com.project.oag.registration.token.ConfirmationToken;
import com.project.oag.registration.token.ConfirmationTokenService;
import com.project.oag.repository.PasswordResetTokenRepository;
import com.project.oag.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService  implements UserDetailsService{
     private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    @Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
    private ConfirmationTokenService confirmationTokenService;

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "OnlineArtGallery";
    public String signUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException("email already registered");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public void uploadProfilePhoto(String loggedInEmail, MultipartFile file) {
        Optional<User> optionalUser = userRepository.findByEmail(loggedInEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                user.setImage(file.getBytes());
                userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload profile photo");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public byte[] getProfilePhoto(String loggedInEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(loggedInEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getImage();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
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

    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME,
                user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public Long getTotalCustomerUsers() {
        return userRepository.countTotalUsersByRole(Role.CUSTOMER);
    }

    public Long getTotalArtistUsers() {
        return userRepository.countTotalUsersByRole(Role.ARTIST);
    }

    public Long getTotalManagerUsers() {
        return userRepository.countTotalUsersByRole(Role.MANAGER);
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

}
