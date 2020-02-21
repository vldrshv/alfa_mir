package ru.alfabank.alfamir.utility.static_utilities;

import android.text.format.DateUtils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ru.alfabank.alfamir.utility.enums.FormatElement;

import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_MOSCOW;


/**
 * Created by U_M0WY5 on 29.08.2017.
 */

public class DateConverter {

    private static Locale russian;
    private static DateFormatSymbols dfs;

    static {
        russian = new Locale("ru");
        String[] newMonths = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        dfs = DateFormatSymbols.getInstance(russian);
        dfs.setMonths(newMonths);
    }

    public static String formatDate(String date, String unformattedPattern, String formattedPattern, int timeZone, FormatElement element) {
        if (date != null && !date.equals("")) {
            switch (element) {
                case NOTIFICATION:
                    if (DateUtils.isToday(convertToMillis(date, unformattedPattern, timeZone))) {
                        return "Сегодня";
                    } else if (DateUtils.isToday(convertToMillis(date, unformattedPattern, timeZone) + TimeUnit.DAYS.toMillis(1))) {
                        return "Вчера";
                    } else {
                        return convertToLocalDate(convertToMillis(date, unformattedPattern, timeZone), formattedPattern);
                    }
                case POST:
                case COMMENT:
                    long lDate = convertToMillis(date, unformattedPattern, timeZone);
                    long lCurrent = new Date().getTime();
                    long hourDifference = (TimeUnit.MILLISECONDS.toHours(lCurrent - lDate));
                    if (hourDifference < 24) {
                        return getDifference(hourDifference, lCurrent, lDate);
                    } else {
                        return convertToLocalDate(convertToMillis(date, unformattedPattern, timeZone), formattedPattern);
                    }
                case TV_SHOW_SCHEDULE_START:
                case TV_SHOW_CURRENT_START_DATE:
                case TV_SHOW_CURRENT_START_AND_END_TIME:
                    return convertToLocalDate(convertToMillis(date, unformattedPattern, timeZone), formattedPattern);
                case BIRTHDAY:
                    return convertToUtcDate(convertToMillis(date, unformattedPattern, timeZone), formattedPattern);
                case MESSAGE:
                    return convertToLocalDate(convertToMillis(date, unformattedPattern, timeZone), formattedPattern);
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    private static String convertToLocalDate(long timeInMilliseconds, String formattedPattern) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat(formattedPattern);
        sdf.setDateFormatSymbols(dfs);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }

    public static String convertToUtcDate(long timeInMilliseconds, String formattedPattern) {
        Date date = new Date(timeInMilliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat(formattedPattern);
        sdf.setDateFormatSymbols(dfs);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }

    public static String getCurrentTimeWithIntTimeZone(int timeZone, String formattedPattern) {
        long lCurrent = new Date().getTime();
        long targetTimeZoneTime = lCurrent + 1000 * ((timeZone + 3) * 60 * 60);
        return DateConverter.convertToUtcDate(targetTimeZoneTime, formattedPattern);
    }

    public static Long convertToMillis(String date, String unformattedPattern, int timeZone) {
        try {
            switch (timeZone) {
                case TIME_ZONE_GREENWICH: {
                    SimpleDateFormat sdf = new SimpleDateFormat(unformattedPattern, russian);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date mDate = sdf.parse(date);
                    return mDate.getTime();
                }
                case TIME_ZONE_MOSCOW: {
                    SimpleDateFormat sdf = new SimpleDateFormat(unformattedPattern);
                    sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                    Date mDate = sdf.parse(date);
                    long test0 = mDate.getTime();
                    return mDate.getTime();
                }
            }
        } catch (Exception e) {
        }
        return new Long(0);
    }

    private static String getDifference(long hourDifference, long lCurrent, long lDate) {
        if (hourDifference < 0) {
            return "сегодня";
        } else if (hourDifference == 0) {
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(lCurrent - lDate);
            if (minutes < 0) {
                return "сегодня";
            } else if (minutes == 0) {
                return "только что";
            } else if (minutes == 1) {
                return "минуту назад";
            } else if (String.valueOf(minutes).endsWith("1")) {
                return minutes + " минуту назад";
            } else if (String.valueOf(minutes).endsWith("2") || String.valueOf(minutes).endsWith("3") ||
                    String.valueOf(minutes).endsWith("3") || String.valueOf(minutes).endsWith("4")) {
                return minutes + " минуты назад";
            } else {
                return minutes + " минут назад";
            }
        } else if (hourDifference == 1 || hourDifference == 21) {
            return hourDifference + " час назад";
        } else if (hourDifference == 2 || hourDifference == 3 || hourDifference == 4 || hourDifference == 22 || hourDifference == 23) {
            return hourDifference + " часа назад";
        } else return hourDifference + " часов назад";
    }

    public static int getCurrentProgress(String startsIn, String endsIn, String unformattedPattern, int timeZone) {
        long currentLong = new Date().getTime();
        long lStartsIn = convertToMillis(startsIn, unformattedPattern, timeZone);
        long lEndsIn = convertToMillis(endsIn, unformattedPattern, timeZone);
        long oneTick = (lEndsIn - lStartsIn) / 10000;
        long difference = (currentLong - lStartsIn) / oneTick;
        return (int) difference;
    }

    public static long getTimeTillFinish(String ends, String unformattedPattern, int timeZone) {
        long endsLong = convertToMillis(ends, unformattedPattern, timeZone);
        long currentLong = new Date().getTime();
        long differenceTillFinish = (endsLong - currentLong);
        return differenceTillFinish;
    }

    public static long getProgressStateInMiliSec(String starts, String ends, String unformattedPattern, int timeZone) {
        long startsLong = convertToMillis(starts, unformattedPattern, timeZone);
        long endsLong = convertToMillis(ends, unformattedPattern, timeZone);
        long oneTick = (endsLong - startsLong) / 10000;
        return oneTick;
    }

    public static String getStartsIn(String startTime, String unformattedPattern, int timeZone) {
        long startTimeLong = convertToMillis(startTime, unformattedPattern, timeZone);
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
}
