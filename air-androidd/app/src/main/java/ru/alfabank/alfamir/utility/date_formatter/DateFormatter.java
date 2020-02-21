package ru.alfabank.alfamir.utility.date_formatter;

public interface DateFormatter {

    String formatDate(String date, String unformattedPattern, String formattedPattern, String timeZone);

    String formatDate(long date, String unformattedPattern, String formattedPattern);

    String formatDate(long date, String formattedPattern);

    long formatDate(String date, String unformattedPattern, String timeZone);

    String getCurrentUtcTime(String formattedPattern);

    long getCurrentUtcTime();

    String getCurrentLocalTime(String formattedPattern);

    String getTimeLeftUntil(long startTimeLong);

}
