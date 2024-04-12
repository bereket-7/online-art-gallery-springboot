package com.project.oag.utils;

import com.project.oag.exceptions.UserNotFoundException;
import io.jsonwebtoken.lang.Assert;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RequestUtils {
    public static final String SORT_BY_REGEX = "\\.";
    public static final String ASC = "asc";
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    private static HttpServletRequest getHttpServletRequest() {
        val requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    @SuppressWarnings("ConstantConditions")
    public static String fetchUserIpAddress(HttpServletRequest request) {

        if (request == null) {
            request = getHttpServletRequest();
            if (request == null) return null;
        }

        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3, "Illegal IP: " + ip);
        return ip;
    }

    public static String getIPFromRequest(HttpServletRequest request) {
        if (request == null) {
            request = getHttpServletRequest();
            if (request == null) return null;
        }
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (isValidIp(ipList)) {
                return ipList.split(",")[0];
            }
        }
        return getRemoteAddress(request);
    }

    public static String getLoggedInUserName(HttpServletRequest request) {
        String username;
        try {
            username = request.getUserPrincipal().getName();
        } catch (Exception e) {
            throw new UserNotFoundException("Unable to get username from request");
        }
        return username;
    }

    private static boolean isValidIp(String ip) {
        return StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    public static String getRemoteAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public static String getIpAddressFromHeader(HttpServletRequest request) {
        if (request == null) {
            request = getHttpServletRequest();
            if (request == null) return null;
        }
        for (String ipHeader : IP_HEADER_CANDIDATES) {
            String headerValue = Collections.list(request.getHeaders(ipHeader)).stream()
                    .filter(RequestUtils::isValidIp)
                    .findFirst()
                    .orElse(null);

            if (headerValue != null) {
                return headerValue;
            }
        }
        return getRemoteAddress(request);
    }

    public static Pageable getPageable(List<String> sortType, int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize, Sort.by(prepareSortExpression(sortType)));
    }

    public static Sort.Order[] prepareSortExpression(List<String> sortType) {
        return sortType.stream()
                .map(s -> {
                    String[] split = s.split(SORT_BY_REGEX);
                    String fieldName = split[0];
                    String direction = split.length > 1 ? split[1] : ASC;
                    return new Sort.Order(Sort.Direction.fromString(direction), fieldName);
                })
                .toArray(Sort.Order[]::new);
    }
}
