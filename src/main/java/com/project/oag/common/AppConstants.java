package com.project.oag.common;

public class AppConstants {
    public static final String EXCEPTION_ON_SENDING_EMAIL = "Exception while sending email.";
    public static final String TEXT_HTML = "text/html";
    public static final String NAME_PATTERN = "^[A-Za-z]+(?:\\s[A-Za-z]+)?$";
    public static final String PHONE_PATTERN = "^(\\+\\d{1,4})?[-.\\s]?\\(?\\d{1,6}\\)?[-.\\s]?\\d{1,10}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String LOG_PREFIX = "Core application {} {}";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String DEFAULT_PAGE_NUMBER = "0";
}
