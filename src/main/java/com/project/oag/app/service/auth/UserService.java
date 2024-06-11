package com.project.oag.app.service.auth;

import com.project.oag.app.dto.*;
import com.project.oag.app.dto.auth.*;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.RoleRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.OtpService;
import com.project.oag.common.GenericResponse;
import com.project.oag.common.service.NotificationService;
import com.project.oag.common.service.UserHelperService;
import com.project.oag.config.properties.EmailConfig;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UnexpectedRoleException;
import com.project.oag.exceptions.UserAuthorizationException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.project.oag.app.validation.RequestValidation.validateRegisterUserRequest;
import static com.project.oag.common.AppConstants.*;
import static com.project.oag.utils.NotificationUtils.generateBodyForEmail;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;
import static java.util.Collections.emptyList;

@Service
@Slf4j
public class UserService {
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String FLAG = "FLAG";
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NotificationService emailService;
    private final EmailConfig emailConfig;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final UserTokenService tokenService;
    private final UserHelperService userHelperService;

    public UserService(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, ModelMapper modelMapper, NotificationService emailService, EmailConfig emailConfig, OtpService otpService, AuthenticationManager authenticationManager, UserTokenService tokenService, UserHelperService userHelperService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.emailConfig = emailConfig;
        this.otpService = otpService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userHelperService = userHelperService;
    }
    private static String getFullName(UserDto user) {
        return user.getFirstName().concat(SPACE).concat(user.getLastName());
    }

    private static void appendUserInfoToRequestContext(HttpServletRequest request, UserDto user, String fullName) {
        request.setAttribute(USER_ID, user.getId());
        request.setAttribute(FULL_NAME, fullName);
        request.setAttribute(IS_ADMIN, user.getUserRole().isAdmin());
        request.setAttribute(UUID, user.getUuid());
        request.setAttribute(REQUEST_UNIQUE_ID, java.util.UUID.randomUUID());
    }
    @Transactional
    public ResponseEntity<GenericResponse> registerUser(HttpServletRequest request, RegisterUserRequestDto registerUserRequestDto) {
        validateRegisterUserRequest(registerUserRequestDto);
        log.info(LOG_PREFIX, "Started signup process for email:  ", registerUserRequestDto.getEmail());
        if (userRepository.existsByEmailIgnoreCase(registerUserRequestDto.getEmail())) {
            log.info(LOG_PREFIX, "Username/email already taken:  ", registerUserRequestDto.getEmail());
            return prepareResponse(HttpStatus.CONFLICT, "Username/Email already taken", emptyList());
        }
        registerUserRequestDto.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));
        val userModel = modelMapper.map(registerUserRequestDto, User.class);

        log.info(LOG_PREFIX, "Fetching current user information to set manager information", "");
        return saveUserAndSendOtpVerification(registerUserRequestDto.getChannel(), userModel);
    }

    @Transactional
    public ResponseEntity<GenericResponse> customerSignup(HttpServletRequest request, SignupRequestDto registerUserRequestDto) {
        log.info(LOG_PREFIX, "Started customer signup process for email:  ", registerUserRequestDto.getEmail());
        if (userRepository.existsByEmailIgnoreCase(registerUserRequestDto.getEmail())) {
            log.info(LOG_PREFIX, "Username/email already taken:  ", registerUserRequestDto.getEmail());
            return prepareResponse(HttpStatus.CONFLICT, "Username/Email already taken", emptyList());
        }
        registerUserRequestDto.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));
        val userModel = modelMapper.map(registerUserRequestDto, User.class);
        val customerRole = roleRepository.findByRoleNameIgnoreCase(ROLE_CUSTOMER).orElseThrow(() -> new ResourceNotFoundException("Unable to find customer role for customer signup process"));
        userModel.setUserRole(customerRole);
        return saveUserAndSendOtpVerification(registerUserRequestDto.getChannel(), userModel);
    }
    public ResponseEntity<GenericResponse> authenticateUserCredentials(HttpServletRequest request, HttpServletResponse response, final AuthRequestDto authRequestDto, final UserType userType) throws ServletException, IOException {
        if (!userRepository.existsByUsernameAndIsAdmin(authRequestDto.username(), isAdminLoginProcess(userType))) {
            getUserByUsername(authRequestDto.username());
            log.info("Login failed, Failed to Confirm user role as {}, aborting login process", userType.name());
            throw new UnexpectedRoleException("Login failed, Failed to Confirm user as {}, aborting login process", userType.name());
        }
        try {
            boolean authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.username(), authRequestDto.password())).isAuthenticated();
            if (!authResult) {
                throw new RuntimeException("Unable to login! Please check username and password.");
            }

            return loginVerification(request, authRequestDto);
        } catch (AuthenticationException e) {
            log.info(LOG_PREFIX, "Login failed for user ", authRequestDto.username());
            return prepareResponse(HttpStatus.UNAUTHORIZED, "Incorrect username or password! ", emptyList());
        }
    }

    private boolean isAdminLoginProcess(UserType userType) {
        return switch (userType) {
            case ADMIN -> true;
            case CUSTOMER -> false;
        };
    }
    private ResponseEntity<GenericResponse> saveUserAndSendOtpVerification(NotificationChannel channel, User userModel) {
        try {
            val savedUser = userRepository.save(userModel);
            val fullName = savedUser.getFirstName().concat(SPACE).concat(savedUser.getLastName());
            val response = sendOtp(OtpRequestDto.builder().userId(savedUser.getId()).name(fullName).otpType(OtpCodeTypeDto.ALL).notificationType(NotificationType.SIGNUP_OTP).address(savedUser.getEmail()).notificationChannel(channel).build());
            log.info(LOG_PREFIX, "Successfully registered user ", savedUser.getEmail());
            return response;
        } catch (Exception e) {
            log.info(LOG_PREFIX, "Failed while registering user ", e.getMessage());
            return prepareResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed while registering role " + e.getLocalizedMessage(), emptyList());
        }
    }

    public ResponseEntity<GenericResponse> loginVerification(HttpServletRequest request, final AuthRequestDto authRequestDto) {
        log.info(LOG_PREFIX, "Login verification process for username", authRequestDto.username());
        val user = getUserByUsername(authRequestDto.username());
        isUserVerified(authRequestDto.username(), user);
        val fullName = getFullName(user);
        appendUserInfoToRequestContext(request, user, fullName);
        if (!user.isEnable2FA()) {
            return tokenService.generateUserToken(getUserByUsername(authRequestDto.username()));
        }
        if (otpService.hasActiveOtp(user.getId())) {
            return prepareResponse(HttpStatus.CONFLICT, "Already have active Otp", null);
        }
        return sendOtp(OtpRequestDto.builder().userId(user.getId()).name(fullName).otpType(OtpCodeTypeDto.ALL).notificationType(NotificationType.LOGIN_OTP).address(authRequestDto.username()).notificationChannel(authRequestDto.channel()).build());
    }
    private ResponseEntity<GenericResponse> sendOtp(final OtpRequestDto requestDto) {
        boolean isOtpSent = otpService.sendOtpNotification(requestDto);
        if (isOtpSent) {
            log.info(LOG_PREFIX, "Sent OTP to user address for username", requestDto.getAddress());
            return prepareResponse(HttpStatus.OK, OTP_SENT, emptyList());
        } else {
            log.info(LOG_PREFIX, "Failed to Send OTP for username", requestDto.getAddress());
            return prepareResponse(HttpStatus.INTERNAL_SERVER_ERROR, FAILED_TO_SEND, emptyList());
        }
    }
    private void isUserVerified(final String username, final UserDto user) {
        if (user == null) {
            log.info(LOG_PREFIX, "Login verification process failed, user not found for username ", username);
            throw new UserAuthorizationException(USER_NOT_FOUND);
        }
        if (!user.isVerified()) {
            log.info(LOG_PREFIX, "Account not verified ", username);
            throw new RequestRejectedException("Account not verified");
        }
    }
    public ResponseEntity<GenericResponse> verifyLoginOtp(final VerifyOtpRequestDTO verifyOtpRequestDTO) {
        if (verifyOtpRequestDTO.isResendOtp()) {
            val user = getUserByUsername(verifyOtpRequestDTO.getUsername());
            isUserVerified(verifyOtpRequestDTO.getUsername(), user);
            if (otpService.canResendOtp(user.getId())) {
                return prepareResponse(HttpStatus.TOO_EARLY, "Not allowed to make new request now, " + "Please wait for some time and try again Already have active Otp", emptyList());
            }
            return resendVerifyOtp(verifyOtpRequestDTO, NotificationType.LOGIN_OTP, user);
        }
        log.info(LOG_PREFIX, "Started verifying OTP for username ", verifyOtpRequestDTO.getUsername());
        val otpVerifyResponse = otpService.verifyOtpCode(verifyOtpRequestDTO, NotificationType.LOGIN_OTP);
        if (!otpVerifyResponse.getStatusCode().is2xxSuccessful()) {
            log.info(LOG_PREFIX, "Failed to verify the OTP for username ", verifyOtpRequestDTO.getUsername());
            return otpVerifyResponse;
        }
        //todo: changes here to add user info
        return tokenService.generateUserToken(getUserByUsername(verifyOtpRequestDTO.getUsername()));
    }
    public ResponseEntity<GenericResponse> verifySignupOtp(HttpServletRequest request, final VerifyOtpRequestDTO verifyOtpRequestDTO) {
        val accountToActivate = getUserByUsername(verifyOtpRequestDTO.getUsername());
        return verifyUserAccount(verifyOtpRequestDTO, accountToActivate);
    }
    public ResponseEntity<GenericResponse> verifyRegisterOtp(HttpServletRequest request, final VerifyOtpRequestDTO verifyOtpRequestDTO) {
        val accountToActivate = getUserByUsername(verifyOtpRequestDTO.getUsername());
        getUserByUsername(getLoggedInUserName(request));
        return verifyUserAccount(verifyOtpRequestDTO, accountToActivate);
    }
    private ResponseEntity<GenericResponse> verifyUserAccount(VerifyOtpRequestDTO verifyOtpRequestDTO, UserDto accountToActivate) {
        if (verifyOtpRequestDTO.isResendOtp()) {
            if (otpService.canResendOtp(accountToActivate.getId())) {
                return prepareResponse(HttpStatus.TOO_EARLY, """
                        Already have active Otp, Not allowed to make new request now,
                        "Please wait for some time and try again""", emptyList());
            }
            return resendVerifyOtp(verifyOtpRequestDTO, NotificationType.SIGNUP_OTP, accountToActivate);
        }
        log.info(LOG_PREFIX, "Started verifying OTP for username ", verifyOtpRequestDTO.getUsername());
        val verifyResponse = otpService.verifyOtpCode(verifyOtpRequestDTO, NotificationType.SIGNUP_OTP);
        if (!verifyResponse.getStatusCode().is2xxSuccessful()) {
            log.info(LOG_PREFIX, "Failed to verify the OTP for username ", verifyOtpRequestDTO.getUsername());
            return verifyResponse;
        }
        log.info(LOG_PREFIX, "Updating user to verified", verifyOtpRequestDTO.getUsername());
        userRepository.updateVerifiedByEmailIgnoreCase(true, verifyOtpRequestDTO.getUsername());
        log.info(LOG_PREFIX, "Successfully updated user to verified", verifyOtpRequestDTO.getUsername());
        emailService.sendNotification(NotificationDto.builder().sendTo(verifyOtpRequestDTO.getUsername()).subject(emailConfig.accountVerifiedSubject()).body(generateBodyForEmail(NotificationInfoDto.builder().notificationType(NotificationType.ACCOUNT_CREATED).build(), emailConfig)).build());
        return prepareResponse(HttpStatus.OK, "You have successfully activated user account!", emptyList());
    }
    private ResponseEntity<GenericResponse> resendVerifyOtp(VerifyOtpRequestDTO verifyOtpRequestDTO, NotificationType notificationType, UserDto user) {
        log.info(LOG_PREFIX, "Resend verifying OTP for username ", verifyOtpRequestDTO.getUsername());
        return sendOtp(OtpRequestDto.builder().userId(user.getId()).name(getFullName(user)).otpType(OtpCodeTypeDto.ALL).notificationType(notificationType).address(verifyOtpRequestDTO.getUsername()).notificationChannel(ObjectUtils.isNotEmpty(verifyOtpRequestDTO.getMedium()) ? verifyOtpRequestDTO.getMedium() : NotificationChannel.EMAIL).build());
    }

//    public ResponseEntity<GenericResponsePageable> fetchUsersAll(HttpServletRequest request, UserSearchRequestDto requestDto, Pageable pageable) {
//        try {
//            val spec = prepareFetchAllUsersSpec(requestDto);
//            val result = userRepository.findAll(spec, pageable);
//            return prepareResponseWithPageable(HttpStatus.OK, "Fetched users", prepareResultContent(result), preparePageInfo(result));
//        } catch (Exception e) {
//            log.error(LOG_PREFIX, "Failed while users  ", e);
//            throw new GeneralException("Failed while fetch " + e.getLocalizedMessage());
//        }
//    }


    private UserDto getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email).map((element) -> modelMapper.map(element, UserDto.class)).orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }

}
