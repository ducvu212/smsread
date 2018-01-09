package com.example.root.smsread.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public final class DateTimeHelper {
    public static final String EMPTY_DATE = "0000-00-00";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DAY_TIME = "EEEE h:mm aa";
    public static final String FORMAT_FB_BIRTHDAY = "MM/dd/yyyy";
    public static final String FORMAT_FILE = "yyyyMMdd_HHmmss";
    public static final String FORMAT_FULL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_LONG_DATE = "MMMM d, yyyy";
    public static final String FORMAT_MATCHED_TIME = "hh:mm aa, MM/dd/yyyy";
    public static final String FORMAT_MESSAGE_SAME_DAY = "h:mm aa";
    public static final String FORMAT_MESSAGE_SAME_MONTH = "EEEE d MMM";
    public static final String FORMAT_MESSAGE_SAME_WEEK = "EEEE";
    public static final String FORMAT_MESSAGE_SAME_YEAR = "d MMM";
    public static final String FORMAT_MESSAGE_TIME = "EEE, MM/dd/yyyy, hh:mm aa";
    public static final String FORMAT_SHORT_DATE = "MMM yyyy";
    public static final String FORMAT_SHORT_TIME = "HH:mm";
    public static final String FORMAT_TIME = "HH:mm:ss";

    public static Calendar clearTimeCalendar(Calendar calendar) {
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar;
    }

    public static Calendar clearSecondCalendar(Calendar calendar) {
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar;
    }

    public static int currentYear() {
        return currentValue(1);
    }

    public static int currentMonth() {
        return currentValue(2);
    }

    public static int currentDay() {
        return currentValue(5);
    }

    public static int currentValue(int field) {
        return Calendar.getInstance().get(field);
    }

    public static int getAge(Calendar calendar) {
        return Calendar.getInstance().get(1) - calendar.get(1);
    }

    public static Calendar createCalendar(int year, int month, int dayOfMonth) {
        return updateCalendar(createCalendar(), year, month, dayOfMonth);
    }

    public static Calendar updateCalendar(Calendar calendar, int year, int month, int dayOfMonth) {
        calendar.set(1, year);
        calendar.set(2, month);
        calendar.set(5, dayOfMonth);
        return calendar;
    }

    public static String toString(Calendar calendar, String format) {
        return getSimpleDateFormat(format).format(calendar.getTime());
    }

    public static String toString(Calendar calendar) {
        return toString(calendar, FORMAT_DATE);
    }

    private static SimpleDateFormat getSimpleDateFormat(String language, String format) {
        return new SimpleDateFormat(format, new Locale(language));
    }

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format, Locale.getDefault());
    }

    public static Calendar parseCalendar(String calendar, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        Calendar cal = createCalendar();
        try {
            cal.setTime(sdf.parse(calendar));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String convert(String calendar, String fromFormat, String toFormat) {
        if (calendar.equals(EMPTY_DATE)) {
            return null;
        }
        return toString(parseCalendar(calendar, fromFormat), toFormat);
    }

    public static int getHour(String timeText) {
        return parseCalendar(timeText, FORMAT_TIME).get(11);
    }

    public static int getMinute(String timeText) {
        return parseCalendar(timeText, FORMAT_TIME).get(12);
    }

    public static String parseTime(int hour, int minute) {
        Calendar calendar = createCalendar();
        calendar.set(11, hour);
        calendar.set(12, minute);
        return toString(calendar, FORMAT_TIME);
    }

    public static long daysBetween(Calendar beforeDate, Calendar afterDate) {
        Calendar date1 = (Calendar) beforeDate.clone();
        Calendar date2 = (Calendar) afterDate.clone();
        long daysBetween = 0;
        while (date1.before(date2)) {
            date1.add(5, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static Calendar createCalendar() {
        return Calendar.getInstance();
    }

    public static String currentCalendar(String format) {
        return toString(createCalendar(), format);
    }

    public static String currentCalendarNoSecond(String format) {
        return toString(clearSecondCalendar(createCalendar()), format);
    }

    public static void increment(Calendar currentDate) {
        currentDate.add(5, 1);
    }

    public static boolean isLastDayOfMonth(Calendar calendar) {
        return calendar.get(5) == calendar.getActualMaximum(5);
    }

    public static boolean isSameDate(Calendar calendar1, Calendar calendar2) {
        return clearTimeCalendar((Calendar) calendar1.clone()).equals(clearTimeCalendar((Calendar) calendar2.clone()));
    }

    public static boolean isSameWeek(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(3) == calendar2.get(3) && calendar1.get(1) == calendar2.get(1);
    }

    public static boolean isSameMonth(Calendar calendar1, Calendar calendar2) {
        Calendar c1 = (Calendar) calendar1.clone();
        Calendar c2 = (Calendar) calendar2.clone();
        return c1.get(2) == c2.get(2) && c1.get(1) == c2.get(1);
    }

    public static boolean isSameYear(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(1) == calendar2.get(1);
    }

    public static Calendar formatDate(String timeInMillis) {
        try {
            long time = Long.parseLong(timeInMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(String timeInMillis, String format) {
        try {
            long time = Long.parseLong(timeInMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return toString(calendar, format);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatDate(long timeInMillis, String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
            return toString(calendar, format);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }
}
