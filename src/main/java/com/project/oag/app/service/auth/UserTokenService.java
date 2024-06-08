package com.project.oag.app.service.auth;

import com.project.oag.app.dto.auth.JWTToken;
import com.project.oag.app.dto.auth.RolePermissionDto;
import com.project.oag.app.dto.auth.*;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.UserToken;
import com.project.oag.app.repository.TokenRepository;
import com.project.oag.app.service.UserInfoUserDetailsService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

import static com.project.oag.common.AppConstants.BEARER;
import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static org.apache.commons.lang3.StringUtils.SPACE;

@Service
@Slf4j
public class UserTokenService {
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final TokenRepository tokenRepository;
    private final UserInfoUserDetailsService userDetailsService;

    public UserTokenService(JwtService jwtService, ModelMapper modelMapper, TokenRepository tokenRepository, UserInfoUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
    }

    public ResponseEntity<GenericResponse> generateUserToken(final UserDto userDto) {
        log.info(LOG_PREFIX, "Saving token to database:  ", "");
        try {
            revokeExistingTokens(userDto);
            val jwtToken = new JWTToken(jwtService.generateToken(userDto.getEmail()));
            val userToken = UserToken.builder()
                    .user(modelMapper.map(userDto, User.class))
                    .tokenType(TokenType.BEARER)
                    .expired(false)
                    .revoked(false)
                    .token(jwtToken.token())
                    .build();

            tokenRepository.save(userToken);
            log.info(LOG_PREFIX, "Successfully saved token to database ", "");
            return ResponseEntity.ok(new GenericResponse(HttpStatus.OK.value(), "",
                    UserInfoDto.builder()
                            .uuid(userDto.getUuid())
                            .token(jwtToken.token())
                            .username(userDto.getEmail())
//                            .companyId(userDto.getUserRole().getIssuerCompanyId())
                            .permissions(userDto.getUserRole().getRolePermissions()
                                    .stream()
                                    .map(RolePermissionDto::getPermissionName)
                                    .toList())
                            .fullName(userDto.getFirstName().concat(SPACE).concat(userDto.getLastName()))
                            .avatarUrl(userDto.getImage())
                            .build()));
        } catch (Exception e) {
            log.info(LOG_PREFIX, "Failed generateUserToken ", e);
            return ResponseEntity.status(HttpStatus.OK.value()).body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
                    , "Failed generateUserToken , " + e.getLocalizedMessage(), Collections.emptyList()));
        }
    }

    protected void validateRequestToken(HttpServletRequest request, String authHeader) {
        String username = null;
        String token = null;
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            token = authHeader.substring(BEARER.length());
            try {
                username = jwtService.extractUsername(token);
            } catch (SignatureException signatureException) {
                log.warn(LOG_PREFIX, signatureException.getMessage(), "");
                //todo: log jwt auth failure to database for analysis
                throw signatureException;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            final boolean isPersistedTokenStillValid =
                    tokenRepository
                            .findByToken(token)
                            .map(t -> (!t.isExpired() && !t.isRevoked()))
                            .orElse(false);
            if (jwtService.validateToken(token, userDetails) && isPersistedTokenStillValid) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }


    protected void revokeExistingTokens(UserDto userDto) {
        log.info(LOG_PREFIX, "Started revoking tokens for user", userDto.getEmail());
        try {
            var previousToken = tokenRepository.findValidTokens(userDto.getId());
            if (CollectionUtils.isEmpty(previousToken)) {
                return;
            }
            previousToken.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });

            tokenRepository.saveAll(previousToken);
            log.info(LOG_PREFIX, "Successfully revoked token ", "");
        } catch (Exception e) {
            log.info(LOG_PREFIX, "Failed while revoking tokens ", e.getMessage());
            throw new GeneralException("Failed while revoking tokens " + e.getMessage());
        }
    }
}
