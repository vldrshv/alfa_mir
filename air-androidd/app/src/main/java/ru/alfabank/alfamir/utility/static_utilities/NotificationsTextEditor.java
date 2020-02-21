package ru.alfabank.alfamir.utility.static_utilities;

import android.text.Html;
import android.text.Spanned;

import java.util.List;

import ru.alfabank.alfamir.notification.presentation.dto.DisplayInfo;
import ru.alfabank.alfamir.utility.enums.FormatElement;
import ru.alfabank.alfamir.notification.data.dto.ModelNotification;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_1;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_2;
import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_3;
import static ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH;

/**
 * Created by U_m0wy5 on 16.01.2018.
 */

public class NotificationsTextEditor {
    public static Spanned getBirthdayNotification(ModelNotification.MessageParameters[] messageParameters, String notificationDate){
        String birthdate = "";
        String createdDate = "";
        String employee = "";
        if(messageParameters!=null&&messageParameters.length!=0){
            for (ModelNotification.MessageParameters msgparams : messageParameters){
                if (msgparams.getType().equals("birthdate")){
                    birthdate = msgparams.getContent();

                    String [] properDate = notificationDate.split(" ");
                    if(properDate.length==2){
                        createdDate = properDate[0];
                    }

                    if(birthdate.equals(createdDate)){
                        birthdate = "Сегодня";
                    } else {
//                        birthdate = DateConverter.getDayAndMonth(birthdate, DATE_PATTERN_1, DATE_PATTERN_2);
                        birthdate = DateConverter.formatDate(birthdate, DATE_PATTERN_1, DATE_PATTERN_2, TIME_ZONE_GREENWICH, FormatElement.BIRTHDAY);
                    }
                }
                if(msgparams.getType().equals("employee")){
                    employee = msgparams.getContent();
                }
            }

        }
        return Html.fromHtml(birthdate + " День Рождения празднует " + "<font color=#1875f0>" + employee + "</font>" );
    }

    public static Spanned getVacationNotification(ModelNotification.MessageParameters[] messageParameters){
        String vacationStart = "";
        String vacationEnd = "";
        String employee = "";
        if(messageParameters!=null&&messageParameters.length!=0){
            for (ModelNotification.MessageParameters msgparams : messageParameters){
                if (msgparams.getType().equals("vacationstart")){
                    vacationStart = msgparams.getContent();
                    vacationStart = DateConverter.formatDate(vacationStart, DATE_PATTERN_1, DATE_PATTERN_3, TIME_ZONE_GREENWICH, FormatElement.BIRTHDAY);
                }
                if (msgparams.getType().equals("vacationend")){
                    vacationEnd = msgparams.getContent();
                    vacationEnd = DateConverter.formatDate(vacationEnd, DATE_PATTERN_1, DATE_PATTERN_3, TIME_ZONE_GREENWICH, FormatElement.BIRTHDAY);
                }
                if(msgparams.getType().equals("employee")){
                    employee = msgparams.getContent();
                }
            }

        }
        return Html.fromHtml("<font color=#1875f0>" + employee + "</font>" +
                " будет отсутствовать с " + vacationStart + " по " +  vacationEnd);
    }

    public static Spanned getCommentNotificationText(ModelNotification.MessageParameters[] messageParameters){
        String commentText = "";
        if(messageParameters!=null&&messageParameters.length!=0){
            for (ModelNotification.MessageParameters msgparams : messageParameters){
                if (msgparams.getType().equals("postname")){
                    commentText = msgparams.getContent();
                }
            }
        }
        return Html.fromHtml("Комментарий к записи" + "<br />" +
                "<font color=#1875f0>" + commentText + "</font>" );
    }

    public static Spanned getBirthdayNotification(List<DisplayInfo> displayInfos, String notificationDate){
        String birthdate = "";
        String createdDate = "";
        String employee = "";
        if(displayInfos!=null&&displayInfos.size()!=0){
            for (DisplayInfo msgparams : displayInfos){
                if (msgparams.getType().equals("birthdate")){
                    birthdate = msgparams.getContent();

                    String [] properDate = notificationDate.split(" ");
                    if(properDate.length==2){
                        createdDate = properDate[0];
                    }

                    if(birthdate.equals(createdDate)){
                        birthdate = "Сегодня";
                    } else {
//                        birthdate = DateConverter.getDayAndMonth(birthdate, DATE_PATTERN_1, DATE_PATTERN_2);
                        birthdate = DateConverter.formatDate(birthdate, DATE_PATTERN_1, DATE_PATTERN_2, TIME_ZONE_GREENWICH, FormatElement.BIRTHDAY);
                    }
                }
                if(msgparams.getType().equals("employee")){
                    employee = msgparams.getContent();
                }
            }

        }
        return Html.fromHtml(birthdate + " День Рождения празднует " + "<font color=#1875f0>" + employee + "</font>" );
    }

    public static Spanned getVacationNotification(List<DisplayInfo> displayInfos){
        String vacationStart = "";
        String vacationEnd = "";
        String employee = "";
        if(displayInfos!=null&&displayInfos.size()!=0){
            for (DisplayInfo msgparams : displayInfos){
                if (msgparams.getType().equals("vacationstart")){
                    vacationStart = msgparams.getContent();
                    vacationStart = DateConverter.formatDate(vacationStart, DATE_PATTERN_1, DATE_PATTERN_3, TIME_ZONE_GREENWICH, FormatElement.BIRTHDAY);
                }
                if (msgparams.getType().equals("vacationend")){
                    vacationEnd = msgparams.getContent();
                    vacationEnd = DateConverter.formatDate(vacationEnd, DATE_PATTERN_1, DATE_PATTERN_3, TIME_ZONE_GREENWICH, FormatElement.BIRTHDAY);
                }
                if(msgparams.getType().equals("employee")){
                    employee = msgparams.getContent();
                }
            }

        }
        return Html.fromHtml("<font color=#1875f0>" + employee + "</font>" +
                " будет отсутствовать с " + vacationStart + " по " +  vacationEnd);
    }

    public static Spanned getSantaNotification(List<DisplayInfo> displayInfos){
        String employee = "";
        if(displayInfos!=null&&displayInfos.size()!=0){
            for (DisplayInfo msgparams : displayInfos){
                if(msgparams.getType().equals("employee")){
                    employee = msgparams.getContent();
                }
            }

        }
        return Html.fromHtml("Твой тайный Санта<br>"+ "<font color=#1875f0>" + employee + "</font>");
    }

    public static Spanned getCommentNotificationText(List<DisplayInfo> displayInfos){
        String commentText = "";
        if(displayInfos!=null&&displayInfos.size()!=0){
            for (DisplayInfo msgparams : displayInfos){
                if (msgparams.getType().equals("postname")){
                    commentText = msgparams.getContent();
                }
            }
        }
        return Html.fromHtml("Комментарий к записи" + "<br />" +
                "<font color=#1875f0>" + commentText + "</font>" );
    }

    public static String getDepartment(List<DisplayInfo> displayInfos){
        if(displayInfos!=null&&displayInfos.size()!=0){
            for (DisplayInfo msgparams : displayInfos){
                if (msgparams.getType().equals("sitename")){
                    return msgparams.getContent();
                }
            }
        }
        return null;
    }

    public static String getPostTitle(List<DisplayInfo> displayInfos){
        if(displayInfos!=null&&displayInfos.size()!=0){
            for (DisplayInfo msgparams : displayInfos){
                if (msgparams.getType().equals("postname")){
                    return msgparams.getContent();
                }
            }
        }
        return null;
    }

    public static String getBirthday(List<DisplayInfo> displayInfos){
        if(displayInfos!=null&&displayInfos.size()!=0){
            for (DisplayInfo msgparams : displayInfos){
                if (msgparams.getType().equals("birthdate")){
                    return msgparams.getContent();
                }
            }
        }
        return null;
    }

}
