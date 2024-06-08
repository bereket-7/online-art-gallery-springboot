package com.project.oag.common;

public class AppConstants {
    public static final String EXCEPTION_ON_SENDING_EMAIL = "Exception while sending email.";
    public static final String TEXT_HTML = "text/html";
    public static final String NAME_PATTERN = "^[A-Za-z]+(?:\\s[A-Za-z]+)?$";
    public static final String PHONE_PATTERN = "^(\\+\\d{1,4})?[-.\\s]?\\(?\\d{1,6}\\)?[-.\\s]?\\d{1,10}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public static final String LOG_PREFIX = "Core application {} {}";
    public static final String SPACE = " ";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String CACHE_KEY_EMAIL = "#email";
    public static final String FULL_NAME = "fullName";
    public static final String UUID = "UUID";
    public static final String REQUEST_UNIQUE_ID = "requestUniqueId";
    public static final long CACHE_EXPIRY_TIME = 300000; //5 minutes
    public static final String USER_NOT_FOUND = "User not found! ";
    public static final String OTP_SENT = "Otp Message Sent to email.";
    public static final String FAILED_TO_SEND = "Failed to send OTP, please try again.";
    public static final String CACHE_NAME_USER_INFO = "user_info";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String REGISTER_ACTION = "register";
    public static final String BEARER = "Bearer ";
    public static final String IS_ADMIN = "isAdmin";

    public static final String USER_ID = "userId";
    public static final String URL_PATTERN = "^(https?|ftp):\\/\\/(www\\.)?([a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+)(:[0-9]+)?(\\/[^\\s]*)?$";
    public static final String URL_PATH_PATTERN = "^\\/[^\\s?#]+(?:\\?[^\\s#]*)?(?:#[^\\s]*)?$";
    public static final String EXCEPTION_ON_PREPARING_SMTP_PROPS = "Exception while preparing SMTP properties";
    public static final String EXCEPTION_ON_FORMATTING_EMAIL = "Exception while formatting email message";
    public static final int START_YEAR = 2021;
    public static final int END_YEAR = 3099;

}
