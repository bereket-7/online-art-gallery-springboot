package com.project.oag.app.controller;

import com.project.oag.app.dto.GenericResponsePageable;
import com.project.oag.app.dto.UserSearchRequestDto;
import com.project.oag.app.dto.VerifyOtpRequestDTO;
import com.project.oag.app.dto.auth.RegisterUserRequestDto;
import com.project.oag.app.service.auth.PasswordService;
import com.project.oag.app.service.auth.RoleManagementService;
import com.project.oag.app.service.auth.UserService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.oag.common.AppConstants.*;
import static com.project.oag.utils.RequestUtils.getPageable;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final PasswordService passwordService;
    private final RoleManagementService roleManagementService;

    public AdminController(UserService userService,
                           PasswordService passwordService,
                           RoleManagementService roleManagementService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.roleManagementService = roleManagementService;
    }
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> addUser(HttpServletRequest request, @Valid @RequestBody RegisterUserRequestDto user) {
        return userService.registerUser(request, user);
    }

    @PostMapping("/register/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> userVerify(HttpServletRequest request, @RequestBody VerifyOtpRequestDTO verifyOtpRequestDTO) {
        return userService.verifyRegisterOtp(request, verifyOtpRequestDTO);
    }

    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_USERS')")
    public ResponseEntity<GenericResponsePageable> fetchAllUsers(HttpServletRequest request,
                                                                 @RequestParam(name = "uuid", required = false) String uuid,
                                                                 @RequestParam(name = "firstName", required = false) String firstName,
                                                                 @RequestParam(name = "lastName", required = false) String lastName,
                                                                 @RequestParam(name = "email", required = false) String email,
                                                                 @RequestParam(name = "phone", required = false) String phone,
                                                                 @RequestParam(name = "fromDate", required = false) LocalDateTime fromDate,
                                                                 @RequestParam(name = "toDate", required = false) LocalDateTime toDate,
                                                                 @RequestParam(value = "sortType", defaultValue = LAST_UPDATE_DATE_DESC, required = false) List<String> sortType,
                                                                 @RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Pageable pageable = getPageable(sortType, pageNumber, pageSize);
        val requestDto = new UserSearchRequestDto(uuid, firstName, lastName, email, phone, fromDate, toDate);
        return userService.fetchUsersAll(request, requestDto, pageable);
    }

}
