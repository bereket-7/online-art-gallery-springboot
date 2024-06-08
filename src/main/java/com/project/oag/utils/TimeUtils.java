package com.project.oag.utils;

import com.project.oag.common.AppConstants;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {
    public static final long ONE_MINUTE_MILLIS = 1000 * 60;
    public static final int MINUS_ONE = -1;

    private TimeUtils() {
    }

    public static Timestamp getStartOfTime(ZoneId defaultZoneId) {
        return formatLocalDateTimeToDbFormat(LocalDateTime.ofInstant(LocalDate.of(AppConstants.START_YEAR, 1, 1)
                .atStartOfDay(defaultZoneId).toInstant(), defaultZoneId));
    }

    public static Timestamp getEndOfTime(ZoneId defaultZoneId) {
        return formatLocalDateTimeToDbFormat(LocalDateTime.ofInstant(LocalDate.of(AppConstants.END_YEAR, 1, 1)
                .atStartOfDay(defaultZoneId).toInstant(), defaultZoneId));
    }

    public static Date getTimeAfterGivenMinutes(long minutes) {
        return new Date(System.currentTimeMillis() + (ONE_MINUTE_MILLIS * minutes));
    }

    public static Timestamp getTimestampBeforeGivenMinutes(long minutes) {
        return getTimestampAfterGivenMinutes(MINUS_ONE * minutes);
    }

    public static Timestamp getTimestampAfterGivenMinutes(long minutes) {
        return Timestamp.from(new Date(System.currentTimeMillis() + Duration.ofMinutes(minutes).toMillis()).toInstant());
    }

    public static Timestamp getTimestampAfterGivenSeconds(long seconds) {
        return getTimestampAfterGivenMinutes(Duration.ofSeconds(seconds).toMinutes());
    }

    public static Date getTimeAfterGivenMinutesGivenDate(long minutes, Date date) {
        return new Date(date.toInstant().toEpochMilli() + Duration.ofMinutes(minutes).toMillis());
    }

    public static Timestamp getTimestampAfterGivenMinutesGivenTimestamp(long minutes, Timestamp timestamp) {
        return Timestamp.from(new Date(timestamp.toInstant().toEpochMilli() + Duration.ofMinutes(minutes).toMillis()).toInstant());
    }

    public static Timestamp getTimestampNow() {
        return Timestamp.from(Instant.now());
    }

    public static Date getDateNow() {
        return Date.from(Instant.now());
    }

    public static long getTimestampDiffInSec(Timestamp from, Timestamp to) {
        return Duration
                .between(from.toLocalDateTime(), to.toLocalDateTime())
                .getSeconds();
    }

    public static long getTimestampDiffInMinutes(Timestamp from, Timestamp to) {
        return Duration.ofSeconds(Duration
                .between(from.toLocalDateTime(), to.toLocalDateTime())
                .getSeconds()).toMinutes();
    }

    public static Timestamp getTimeWithNano(LocalDateTime startDate) {
        return Timestamp.valueOf(startDate.withNano(0));
    }

    public static Timestamp formatLocalDateTimeToDbFormat(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return Timestamp.valueOf(localDateTime.format(formatter));
    }

    public static Timestamp getToDate(LocalDateTime toDate) {
        return toDate != null ? Timestamp.valueOf(toDate)
                : TimeUtils.getEndOfTime(ZoneId.systemDefault());
    }

    public static Timestamp getFromDate(LocalDateTime fromDate) {
        return fromDate != null ? Timestamp.valueOf(fromDate)
                : TimeUtils.getStartOfTime(ZoneId.systemDefault());
    }
}

