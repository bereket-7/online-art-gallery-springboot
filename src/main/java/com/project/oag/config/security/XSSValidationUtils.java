package com.project.oag.config.security;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@UtilityClass
@Slf4j
public class XSSValidationUtils {
    public final Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.\\/?\\s]*$", Pattern.CASE_INSENSITIVE);
    public static boolean isValidURL(String uri, List<String> skipWords) {
        AtomicBoolean flag = new AtomicBoolean(false);
        String[] urls = uri.split("\\/");

        Arrays.stream(urls).filter(e -> !ObjectUtils.isEmpty(e)).forEach(url -> {
            System.out.println("value:" + url);
            if (skipWords.stream().anyMatch(p -> url.toLowerCase().contains(p.toLowerCase()))) {
                log.error("bad char found!!!!!");
                flag.set(true);
            }
            Matcher matcher = pattern.matcher(url);
            if (!matcher.matches()) {
                log.error("Invalid char found!!!!!");
                flag.set(true);
            } else {
                log.error("valid char found: {}", url);
            }
        });
        return !flag.get();
    }


}