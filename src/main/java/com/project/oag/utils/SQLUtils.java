package com.project.oag.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Locale;

public class SQLUtils {
    public static String getLikeString(String string) {
        if (ObjectUtils.isNotEmpty(string))
            return '%' + string.toLowerCase(Locale.getDefault()) + '%';
        return null;
    }
}
