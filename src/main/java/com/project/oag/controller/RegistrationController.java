package com.project.oag.controller;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.common.GenericResponse;
import com.project.oag.controller.dto.PasswordDto;
import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.User;
import com.project.oag.entity.VerificationToken;
import com.project.oag.exceptions.InvalidOldPasswordException;
import com.project.oag.registration.OnRegistrationCompleteEvent;
import com.project.oag.security.UserSecurityService;
import com.project.oag.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class RegistrationController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messages;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private Environment env;

    public RegistrationController() {
        super();
    }

    // Registration
    @PostMapping("/user/registration")
    public GenericResponse registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
        LOGGER.debug("Registering user account with information: {}", accountDto);

        final User registered = userService.registerNewUserAccount(accountDto);
        eventPublisher
                .publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return new GenericResponse("success");
    }

    // User activation - verification
    @GetMapping("/user/resendRegistrationToken")
    public GenericResponse resendRegistrationToken(final HttpServletRequest request,
            @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    // Reset password
    @PostMapping("/user/resetPassword")
    public GenericResponse resetPassword(final HttpServletRequest request,
            @RequestParam("email") final String userEmail) {
        final User user = userService.findUserByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
        }
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }

    // Save password
    @PostMapping("/user/savePassword")
    public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {

        final String result = userSecurityService.validatePasswordResetToken(passwordDto.getToken());

        if (result != null) {
            return new GenericResponse(messages.getMessage("auth.message." + result, null, locale));
        }

        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
        if (user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
        } else {
            return new GenericResponse(messages.getMessage("auth.message.invalid", null, locale));
        }
    }

    // Change user password
    @PostMapping("/user/updatePassword")
    public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
        final User user = userService.findUserByEmail(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

    // Change user 2 factor authentication
    @PostMapping("/user/update/2fa")
    public GenericResponse modifyUser2FA(@RequestParam("use2FA") final boolean use2FA)
            throws UnsupportedEncodingException {
        final User user = userService.updateUser2FA(use2FA);
        if (use2FA) {
            return new GenericResponse(userService.generateQRUrl(user));
        }
        return null;
    }

    /*
     * @GetMapping("/registrationConfirm")
     * public String confirmRegistration(final HttpServletRequest request, final
     * Model model,
     * 
     * @RequestParam("token") final String token) {
     * final Locale locale = request.getLocale();
     * 
     * final VerificationToken verificationToken =
     * userService.getVerificationToken(token);
     * if (verificationToken == null) {
     * final String message = messages.getMessage("auth.message.invalidToken", null,
     * locale);
     * model.addAttribute("message", message);
     * return "redirect:/badUser.html?lang=" + locale.getLanguage();
     * }
     * 
     * final User user = verificationToken.getUser();
     * final Calendar cal = Calendar.getInstance();
     * if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime())
     * <= 0) {
     * model.addAttribute("message", messages.getMessage("auth.message.expired",
     * null, locale));
     * model.addAttribute("expired", true);
     * model.addAttribute("token", token);
     * return "redirect:/badUser.html?lang=" + locale.getLanguage();
     * }
     * 
     * user.setEnabled(true);
     * userService.saveRegisteredUser(user);
     * model.addAttribute("message", messages.getMessage("message.accountVerified",
     * null, locale));
     * return "redirect:/login.html?lang=" + locale.getLanguage();
     * }
     * 
     * 
     * 
     * @PostMapping("/user/registration")
     * public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid final
     * UserDto userDto,
     * final HttpServletRequest request, final Errors errors) {
     * LOGGER.debug("Registering user account with information: {}", userDto);
     * 
     * try {
     * final User registered = userService.registerNewUserAccount(userDto);
     * 
     * final String appUrl = "http://" + request.getServerName() + ":" +
     * request.getServerPort()
     * + request.getContextPath();
     * eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
     * request.getLocale(), appUrl));
     * } catch (final UserAlreadyExistException uaeEx) {
     * ModelAndView mav = new ModelAndView("registration", "user", userDto);
     * String errMessage = messages.getMessage("message.regError", null,
     * request.getLocale());
     * mav.addObject("message", errMessage);
     * return mav;
     * } catch (final RuntimeException ex) {
     * LOGGER.warn("Unable to register user", ex);
     * return new ModelAndView("emailError", "user", userDto);
     * }
     * return new ModelAndView("successRegister", "user", userDto);
     * }
     * 
     * @GetMapping("/user/resendRegistrationToken")
     * public String resendRegistrationToken(final HttpServletRequest request, final
     * Model model,
     * 
     * @RequestParam("token") final String existingToken) {
     * final Locale locale = request.getLocale();
     * final VerificationToken newToken =
     * userService.generateNewVerificationToken(existingToken);
     * final User user = userService.getUser(newToken.getToken());
     * try {
     * final String appUrl = "http://" + request.getServerName() + ":" +
     * request.getServerPort()
     * + request.getContextPath();
     * final SimpleMailMessage email = constructResetVerificationTokenEmail(appUrl,
     * request.getLocale(), newToken,
     * user);
     * mailSender.send(email);
     * } catch (final MailAuthenticationException e) {
     * LOGGER.debug("MailAuthenticationException", e);
     * return "redirect:/emailError.html?lang=" + locale.getLanguage();
     * } catch (final Exception e) {
     * LOGGER.debug(e.getLocalizedMessage(), e);
     * model.addAttribute("message", e.getLocalizedMessage());
     * return "redirect:/login.html?lang=" + locale.getLanguage();
     * }
     * model.addAttribute("message", messages.getMessage("message.resendToken",
     * null, locale));
     * return "redirect:/login.html?lang=" + locale.getLanguage();
     * }
     * 
     * @PostMapping("/user/resetPassword")
     * public String resetPassword(final HttpServletRequest request, final Model
     * model,
     * 
     * @RequestParam("email") final String userEmail) {
     * final User user = userService.findUserByEmail(userEmail);
     * if (user == null) {
     * model.addAttribute("message", messages.getMessage("message.userNotFound",
     * null, request.getLocale()));
     * return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
     * }
     * 
     * final String token = UUID.randomUUID().toString();
     * userService.createPasswordResetTokenForUser(user, token);
     * try {
     * final String appUrl = "http://" + request.getServerName() + ":" +
     * request.getServerPort()
     * + request.getContextPath();
     * final SimpleMailMessage email = constructResetTokenEmail(appUrl,
     * request.getLocale(), token, user);
     * mailSender.send(email);
     * } catch (final MailAuthenticationException e) {
     * LOGGER.debug("MailAuthenticationException", e);
     * return "redirect:/emailError.html?lang=" + request.getLocale().getLanguage();
     * } catch (final Exception e) {
     * LOGGER.debug(e.getLocalizedMessage(), e);
     * model.addAttribute("message", e.getLocalizedMessage());
     * return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
     * }
     * model.addAttribute("message",
     * messages.getMessage("message.resetPasswordEmail", null,
     * request.getLocale()));
     * return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
     * }
     * 
     * @GetMapping("/user/changePassword")
     * public String changePassword(final HttpServletRequest request, final Model
     * model, @RequestParam("id") final long id,
     * 
     * @RequestParam("token") final String token) {
     * final Locale locale = request.getLocale();
     * 
     * final PasswordResetToken passToken =
     * userService.getPasswordResetToken(token);
     * if (passToken == null || passToken.getUser().getId() != id) {
     * final String message = messages.getMessage("auth.message.invalidToken", null,
     * locale);
     * model.addAttribute("message", message);
     * return "redirect:/login.html?lang=" + locale.getLanguage();
     * }
     * 
     * final Calendar cal = Calendar.getInstance();
     * if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
     * model.addAttribute("message", messages.getMessage("auth.message.expired",
     * null, locale));
     * return "redirect:/login.html?lang=" + locale.getLanguage();
     * }
     * 
     * final Authentication auth = new
     * UsernamePasswordAuthenticationToken(passToken.getUser(), null,
     * userDetailsService.loadUserByUsername(passToken.getUser().getEmail()).
     * getAuthorities());
     * SecurityContextHolder.getContext().setAuthentication(auth);
     * 
     * return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
     * }
     * 
     * @PostMapping("/user/savePassword")
     * 
     * @PreAuthorize("hasRole('READ_PRIVILEGE')")
     * public String savePassword(final HttpServletRequest request, final Model
     * model,
     * 
     * @RequestParam("password") final String password) {
     * final Locale locale = request.getLocale();
     * 
     * final User user = (User)
     * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     * userService.changeUserPassword(user, password);
     * model.addAttribute("message", messages.getMessage("message.resetPasswordSuc",
     * null, locale));
     * return "redirect:/login.html?lang=" + locale;
     * }
     */

    // ============== NON-API ============

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale,
            final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale,
            final String token, final User user) {
        final String url = contextPath + "/user/changePassword?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
