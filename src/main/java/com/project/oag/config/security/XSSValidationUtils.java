package com.project.oag.config.security;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
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

    public static boolean isValidRequestParam(String param, List<String> skipWords) {
        AtomicBoolean flag = new AtomicBoolean(false);
        String[] paramList = param.split("&");

        Arrays.stream(paramList).filter(e -> !StringUtils.isEmpty(e)).forEach(url -> {
            System.out.println("value:" + url);
            if (skipWords.stream().anyMatch(url::equalsIgnoreCase)) {
                log.error("Bad char found!!!!!");
                flag.set(true);
            }
            Matcher matcher = pattern.matcher(url);
            if (!matcher.matches()) {
                log.error("Invalid char found!!!!!");
                flag.set(true);
            } else {
                log.error("Valid char found: {}", url);
            }
        });
        return !flag.get();
    }

    public static boolean isValidURLPattern(String uri, List<String> skipWords) {
        AtomicBoolean flag = new AtomicBoolean(false);
        String[] urls = uri.split("\\/");

        try {
            Arrays.stream(urls).filter(e -> !StringUtils.isEmpty(e)).forEach(url -> {
                Map<String, Object> mapping = null;
                try {
                    log.info("urls:{}", urls);
                    mapping = jsonToMap(new JSONObject(url));
                } catch (JSONException e) {
                    log.error("JSONException ", e);
                    throw new RuntimeException(e);
                }
                log.error("Map; {}", mapping);
                mapping.forEach((key, value) -> {
                    //   System.out.println("key  "+key+"  value:"+value);
                    if (skipWords.stream().anyMatch(String.valueOf(value)::equalsIgnoreCase)) {
                        log.error("Bad char found!!!!!");
                        flag.set(true);
                    }
                    Matcher matcher = pattern.matcher(String.valueOf(value));
                    if (!matcher.matches()) {
                        log.error("{}  : Invalid char found!!!!!", key);
                        flag.set(true);
                    } else {
                        log.info("Valid char found: {}", value);
                    }
                });

            });
        } catch (Exception ex) {
            flag.set(true);
        }
        return !flag.get();
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json, retMap);
        }
        return retMap;
    }
    public static Map<String, Object> toMap(JSONObject object, Map<String, Object> map) throws JSONException {

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray jsonArray) {
                value = toList(key, jsonArray, map);
            } else if (value instanceof JSONObject jsonObject) {
                value = toMap(jsonObject, map);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    public static List<Object> toList(String key, JSONArray array, Map<String, Object> map) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray jsonArray) {
                value = toList(key, jsonArray, map);
            } else if (value instanceof JSONObject jsonObject) {
                value = toMap(jsonObject, map);
            } else {
                String mapValue = String.valueOf(value);
                if (map.containsKey(key)) {
                    mapValue += "," + map.get(key);
                }
                map.put(key, mapValue);
            }
            list.add(value);
        }
        return list;
    }


}