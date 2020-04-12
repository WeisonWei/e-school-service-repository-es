package com.es.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


public class DateUtils {

    public static Date today() {
        return new Date();
    }

    public static String todayStr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(today());
    }

    public static String thatDayStr(long daysToSubtract) {
        return LocalDate.now().minusDays(daysToSubtract).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String todayFullStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today());
    }

    public static String todayWeekDayStr() {
        return LocalDate.now().getDayOfWeek().toString();
    }

    public static int todayWeekDayInt() {
        return LocalDate.now().getDayOfWeek().getValue();
    }

    public static String dateStrWithPoint(String date) {
        return date.replaceAll("-", ".").substring(5, date.length());
    }

    public static String formattedDate(Date date) {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : todayStr();
    }

    public static String formattedDate(long times) {
        return formattedDate("yyyy-MM-dd", times);
    }

    public static String formattedDate(String format) {
        return formattedDate(format, System.currentTimeMillis());
    }

    public static String formattedDate(String format, long times) {
        return new SimpleDateFormat(format).format(times);
    }

    public static String formattedDateTime(long times) {
        return formattedDate("yyyy-MM-dd HH:mm:ss", times);
    }

    public static String formattedDateTimeEx(long times) {
        return formattedDate("yyyy-MM-dd HH:mm:ss.SSS", times);
    }

    public static Long unixtime(String format, String formatDate) {
        long time = (new SimpleDateFormat(format)).parse(formatDate, new ParsePosition(0)).getTime();
        return time;
    }

    public static Long unixtime(String formatDate) throws ParseException {
        long time = DateFormat.getInstance().parse(formatDate).getTime();
        return time;
    }

    public static long currentDateTime() {
        return System.currentTimeMillis();
    }

    public static long currentDateTime(Duration duration) {
        return duration.toMillis();
    }

    public static Date longToDate(long millSec) {
        Date date = new Date(millSec);
        return date;
    }

    public static String getDateMonthDay(long times) {
        return formattedDate("MM月dd日", times);
    }

    public static Long getTodayMill() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        long milli = today.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return milli;
    }

    public static Long getMonthFirstDateMill() {
        LocalDateTime firstDay = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
                .withHour(00).withMinute(00).withSecond(00);
        long milli = firstDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return milli;
    }

    public static Long getMonthLastDateMill() {
        LocalDateTime firstDay = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59);
        long milli = firstDay.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return milli;
    }


}
