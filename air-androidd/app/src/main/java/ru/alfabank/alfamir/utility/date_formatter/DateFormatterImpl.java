package ru.alfabank.alfamir.utility.date_formatter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

public class DateFormatterImpl implements DateFormatter {

    private static Locale russian;
    private static DateFormatSymbols dfs;

    @Inject
    public DateFormatterImpl(){
        russian = new Locale("ru");
        String[] newMonths = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        dfs = DateFormatSymbols.getInstance(russian);
        dfs.setMonths(newMonths);
    }

    @Override
    public String formatDate(String date, String unformattedPattern, String formattedPattern, String timeZone){
        long milliS = getMilliSTime(date, unformattedPattern, timeZone);
        String time = getStringTime(milliS, formattedPattern);
        return time;
    }

    @Override
    public String formatDate(long date, String unformattedPattern, String formattedPattern){
        String time = getStringTime(date, formattedPattern);
        return time;
    }

    @Override
    public String formatDate(long date, String formattedPattern) {
        String time = getStringTime(date, formattedPattern);
        return time;
    }

    @Override
    public long formatDate(String date, String unformattedPattern, String timeZone) {
        long milliS = getMilliSTime(date, unformattedPattern, timeZone);
        return milliS;
    }

    @Override
    public String getCurrentUtcTime(String formattedPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(formattedPattern, russian);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        String time = sdf.format(date);
        return time;
    }

    @Override
    public long getCurrentUtcTime() {
        Date date = new Date();
        return date.getTime();
    }

    @Override
    public String getCurrentLocalTime(String formattedPattern) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(formattedPattern);
        sdf.setDateFormatSymbols(dfs);
        sdf.setTimeZone(tz);
        String time = sdf.format(date);
        return time;
    }

    @Override
    public String getTimeLeftUntil(long startTimeLong) {
        long dif = startTimeLong - new Date().getTime();
        int hours = (int) ((dif / (1000 * 60 * 60)) % 24);
        int days = (int) (dif / (1000 * 60 * 60 * 24));
        int minutes = (int) ((dif / (1000 * 60)) % 60);
        if (days > 0) {
            if (days == 1 || days == 21 || days == 31) {
                return "Через " + days + " день";
            } else if (days == 2 || days == 3 || days == 4 || days == 22 || days == 23 || days == 24) {
                return "Через " + days + " дня";
            } else {
                return "Через " + days + " дней";
            }
        } else if (hours > 0) {
            if (hours == 1 || hours == 21) {
                return "Через " + hours + " час";
            } else if (hours == 2 || hours == 3 || hours == 4 || hours == 22 || hours == 23) {
                return "Через " + hours + " часа";
            } else return "Через " + hours + " часов";
        } else if (dif < 0) {
            return "";
        } else {
            if (minutes == 1) {
                return "Через минуту";
            } else if (minutes == 11 || minutes == 12 || minutes == 13 || minutes == 14) {
                return "Через " + minutes + " минут";
            } else if (String.valueOf(minutes).endsWith("1")) {
                return "Через " + minutes + " минуту";
            } else if (String.valueOf(minutes).endsWith("2") || String.valueOf(minutes).endsWith("3") ||
                    String.valueOf(minutes).endsWith("3") || String.valueOf(minutes).endsWith("4")) {
                return "Через " + minutes + " минуты";
            } else {
                return "Через " + minutes + " минут";
            }
        }
    }

    private Long getMilliSTime(String date, String unformattedPattern, String timeZone){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(unformattedPattern, russian);
            sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date mDate = sdf.parse(date);
            return mDate.getTime();
        } catch (Exception e){
            return 0L;
        }
    }

    private String getStringTime(long milliS, String formattedPattern){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Date date = new Date(milliS);
        SimpleDateFormat sdf = new SimpleDateFormat(formattedPattern);
        sdf.setDateFormatSymbols(dfs);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }
}
