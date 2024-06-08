package com.project.oag.config.security.captcha;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "success",
        "score",
        "action",
        "challenge_ts",
        "hostname",
        "error-codes"
})
public record RecaptchaResponse(
        @JsonProperty("success")
        boolean success,
        @JsonProperty("challenge_ts")
        String challengeTs,
        @JsonProperty("hostname")
        String hostname,
        @JsonProperty("error-codes")
        ErrorCode[] errorCodes,
        @JsonProperty("score")
        float score,
        @JsonProperty("action")
        String action
) {
}