package com.project.oag.app.service;

import com.project.oag.app.entity.ConfirmationToken;
import com.project.oag.app.repository.ConfirmationTokenRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.oag.utils.Utils.prepareResponse;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public ResponseEntity<GenericResponse> saveConfirmationToken(ConfirmationToken token) {
        try {
            val response = confirmationTokenRepository.save(token);
            return prepareResponse(HttpStatus.OK, "success", response);
        } catch (Exception e) {
            throw new GeneralException("Error saving confirmation token");
        }
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }


}
