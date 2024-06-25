package com.project.oag.config;

import com.project.oag.app.entity.AccountLockoutRule;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.UserRole;
import com.project.oag.app.repository.AccountLockoutRepository;
import com.project.oag.app.repository.RoleRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.AppConstants;
import com.project.oag.config.cache.CacheManagerService;
import com.project.oag.config.security.ConfiguredUser;
import com.project.oag.config.security.LockoutRule;
import com.project.oag.config.security.RolePermissionConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StartupDataInitialization implements CommandLineRunner {
    public static final String ETH_PHONE_INITIALS = "+251 9";
    public static final Random RANDOM = new Random();
    public static final String ADMIN = "ADMIN";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionConfig rolePermissionConfig;
    private final CacheManagerService cacheManagerService;
    private final AccountLockoutRepository accountLockoutRepository;
    private final ModelMapper modelMapper;
    public StartupDataInitialization(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     RolePermissionConfig rolePermissionConfig, CacheManagerService cacheManagerService, AccountLockoutRepository accountLockoutRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.rolePermissionConfig = rolePermissionConfig;
        this.cacheManagerService = cacheManagerService;
        this.accountLockoutRepository = accountLockoutRepository;
        this.modelMapper = modelMapper;
    }
    public static String generateRandomPhoneNumber() {
        return ETH_PHONE_INITIALS.concat(String.valueOf(RANDOM.nextInt(11111111, 99999999)));
    }
    private static String getKey(String r) {
        return r.replace("ROLE_", "").toLowerCase();
    }
    private static boolean isAdmin(String roleName) {
        return roleName.contains(ADMIN);
    }

    /**
     * Create initial records from configured properties
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (!rolePermissionConfig.initial()) {
            log.info(AppConstants.LOG_PREFIX, "Skipped initial user and role configuration", "");
            return;
        }
        log.info(AppConstants.LOG_PREFIX, "Started initial user and role configuration ", ", Validating the configuration for init user and role");

        validateInitConfigurationValues(rolePermissionConfig);

        saveConfiguredRoles();

        saveConfiguredUsers();

        saveConfiguredLockoutPolicy();
    }
    private void saveConfiguredLockoutPolicy() {
        try {
            accountLockoutRepository.saveAll(
                    prepareRules(rolePermissionConfig.lockoutPolicy())
            );
            log.error(AppConstants.LOG_PREFIX, "Successfully saved configured lockout policy to database {}");
        } catch (Exception e) {
            log.error(AppConstants.LOG_PREFIX, "Error encountered while saving configured lockout policy to database {}", e.getMessage());
        }
    }
    private List<AccountLockoutRule> prepareRules(final HashMap<String, LockoutRule> lockoutPolicy) {
        val currentSavedLockoutRule = accountLockoutRepository.findAll()
                .parallelStream().map(AccountLockoutRule::getFailureCount)
                .collect(Collectors.toSet());
        return lockoutPolicy.values().parallelStream()
                .filter(l -> !currentSavedLockoutRule.contains(l.failureCount()))
                .map(lockoutRule -> {
                    val rule = new AccountLockoutRule();
                    rule.setBlockTime(lockoutRule.blockTime());
                    rule.setFailureCount(lockoutRule.failureCount());
                    return rule;
                })
                .toList();
    }

    private void saveConfiguredUsers() {
        try {
            userRepository.saveAll(
                    prepareUser(rolePermissionConfig.users())
            );
            cacheManagerService.evictAllCacheValues(AppConstants.CACHE_NAME_USER_INFO);
            log.info(AppConstants.LOG_PREFIX, "successfully inserted configured users to database. ");
        } catch (Exception e) {
            log.error(AppConstants.LOG_PREFIX, "Error encountered while saving configured users to database {}", e.getMessage());
        }
    }

    private void saveConfiguredRoles() {
        try {
            val currentSaveRoles = roleRepository.findAll()
                    .parallelStream().map(UserRole::getRoleName)
                    .collect(Collectors.toSet());
            val roles = prepareUserRole(rolePermissionConfig.roles(), currentSaveRoles);
            roleRepository.saveAll(roles);
            log.info(AppConstants.LOG_PREFIX, "Successfully inserted configured roles to database. ");
        } catch (Exception e) {
            log.error(AppConstants.LOG_PREFIX, "Error encountered while saving configured roles to database {}", e.getMessage());
        }
    }


    /**
     * Prepare users and assign roles from configured property
     *
     * @param users
     * @return
     */
    private List<User> prepareUser(Map<String, ConfiguredUser> users) {
        val savedRoles = roleRepository.findAll();
        val currentSavedUsers = userRepository.findAll()
                .parallelStream().map(User::getEmail)
                .collect(Collectors.toSet());
        return users.values()
                .parallelStream()
//                .filter(u -> !currentSavedUsers.contains(u.email()))
                .map(u -> {

                    User currentUser = userRepository.findByEmailIgnoreCase(u.email()).
                            orElse(null);
                    /**
                     * to handle already existing user, with updated information
                     */
                    if (ObjectUtils.isNotEmpty(currentUser)) {
                        currentUser.setFirstName(u.firstName());
                        currentUser.setLastName(u.lastName());
                        currentUser.setPassword(new BCryptPasswordEncoder()
                                .encode(u.password()));
                        currentUser.setUuid(UUID.randomUUID().toString());
                        currentUser.setPhone(generateRandomPhoneNumber());
                        currentUser.setUserRole(
                                savedRoles.parallelStream()
                                        .filter(role -> u.role().contains(role.getRoleName()))
                                        .findFirst()
                                        .orElse(null));
                        return currentUser;
                    }
                    /**
                     * To handle new user entries
                     */
                    val user = new User();
                    user.setEmail(u.email());
                    user.setFirstName(u.firstName());
                    user.setLastName(u.lastName());
                    user.setPassword(new BCryptPasswordEncoder()
                            .encode(u.password()));
                    user.setUuid(UUID.randomUUID().toString());
                    user.setPhone(generateRandomPhoneNumber());
                    user.setUserRole(
                            savedRoles.parallelStream()
                                    .filter(role -> u.role().contains(role.getRoleName()))
                                    .findFirst()
                                    .orElse(null));
                    return user;
                }).toList();
    }
    private void validateInitConfigurationValues(RolePermissionConfig rolePermissionConfig) {

        var userConfiguredRoles = rolePermissionConfig.users().values()
                .parallelStream()
//                .flatMap(Collection::stream)
                .map(ConfiguredUser::role)
                .collect(Collectors.toSet());

        if (!rolePermissionConfig.roles().containsAll(userConfiguredRoles)) {
            log.warn(AppConstants.LOG_PREFIX, "Found roles assigned to users, but not configured, users will not have those roles as they will be skip skipped! ",
                    userConfiguredRoles.removeAll(rolePermissionConfig.roles()));
        }

    }

    private List<UserRole> prepareUserRole(Set<String> roles, Set<String> currentSaveRoles) {
        val userFlags = rolePermissionConfig.userFlag();
        return roles.parallelStream()
//                .filter(r -> !currentSaveRoles.contains(r))
                .map(r -> {
                    var role = new UserRole();
                    UserRole userRole = roleRepository.findByRoleNameIgnoreCase(r).
                            orElse(null);
                    /**
                     * to handle already existing roles, with updated permission list
                     */
                    if (ObjectUtils.isNotEmpty(userRole)) {
                        userRole.setAdmin(isAdmin(r));
                        return userRole;
                    }
                    /**
                     * To handle new role entries
                     */
                    role.setRoleName(r);
                    role.setAdmin(isAdmin(r));
                    role.setIssuerUserId(null);
                    return role;
                }).toList();
    }
}
