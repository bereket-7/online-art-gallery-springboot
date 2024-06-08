package com.project.oag.config.security.captcha;

import com.project.oag.exceptions.ReCaptchaInvalidException;
import com.project.oag.exceptions.ReCaptchaUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.RecaptchaUtils.hasClientError;
import static com.project.oag.utils.RequestUtils.getIpAddressFromHeader;

@Service
@Slf4j
public class CaptchaService implements CaptchaServiceInterface {
    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
    protected final HttpServletRequest request;
    protected final ReCaptchaAttemptService reCaptchaAttemptService;
    protected final RestOperations restTemplate;
    private final CaptchaConfig config;


    public CaptchaService(CaptchaConfig config, HttpServletRequest request, ReCaptchaAttemptService reCaptchaAttemptService, RestTemplate restTemplate) {
        this.config = config;
        this.request = request;
        this.reCaptchaAttemptService = reCaptchaAttemptService;
        this.restTemplate = restTemplate;
    }

    public RecaptchaResponse processResponse(String response) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", config.secret());
        map.add("response", response);
        return makeRestCall(map).getBody();
    }

    private ResponseEntity<RecaptchaResponse> makeRestCall(MultiValueMap<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        return restTemplate.exchange(config.verifyUrl(),
                HttpMethod.POST,
                entity,
                RecaptchaResponse.class);
    }

    public RecaptchaResponse processResponse(String response, final String action) throws ReCaptchaInvalidException {
        final String ipAddress = getIpAddressFromHeader(request);
        securityCheck(response, ipAddress);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", config.secret());
        map.add("response", response);
        map.add("remoteip", ipAddress);

        ResponseEntity<RecaptchaResponse> googleResponse;
        try {
            googleResponse = makeRestCall(map);
            val responseBody = googleResponse.getBody();
            if (ObjectUtils.isEmpty(responseBody)) {
                throw new ReCaptchaInvalidException("Recaptcha response has no body.");
            }
            log.debug(LOG_PREFIX, "Google's response: {} ", googleResponse.getBody());
            if (!responseBody.success() || !responseBody.action().equals(action) || responseBody.score() < config.threshold()) {
                if (hasClientError(responseBody.errorCodes())) {
                    reCaptchaAttemptService.reCaptchaFailed(ipAddress);
                }
                throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
            }
        } catch (RestClientException rce) {
            log.debug(LOG_PREFIX, "Registration unavailable at this time.  Please try again later.", rce.getMessage());
            throw new ReCaptchaUnavailableException("Registration unavailable at this time. Please try again later.");
        }
        reCaptchaAttemptService.reCaptchaSucceeded(ipAddress);
        return googleResponse.getBody();
    }

    @Override
    public String getReCaptchaSite() {
        return config.site();
    }

    @Override
    public String getReCaptchaSecret() {
        return config.secret();
    }

    protected void securityCheck(final String response, String ipAddress) {
        log.debug("Attempting to validate response {}", response);

        if (reCaptchaAttemptService.isBlocked(ipAddress, config.allowedAttempts())) {
            log.error(LOG_PREFIX, "Client exceeded maximum number of failed attempts", ipAddress);
            throw new ReCaptchaInvalidException("Client exceeded maximum number of failed attempts");
        }

        if (!responseSanityCheck(response)) {
            throw new ReCaptchaInvalidException("Response contains invalid characters");
        }
    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
}
