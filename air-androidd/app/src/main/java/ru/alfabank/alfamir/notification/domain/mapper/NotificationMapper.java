package ru.alfabank.alfamir.notification.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.notification.data.dto.AuthorRaw;
import ru.alfabank.alfamir.notification.data.dto.DisplayInfoRaw;
import ru.alfabank.alfamir.notification.data.dto.NotificationRaw;
import ru.alfabank.alfamir.notification.data.dto.OpenInfoRaw;
import ru.alfabank.alfamir.notification.presentation.dto.Author;
import ru.alfabank.alfamir.notification.presentation.dto.DisplayInfo;
import ru.alfabank.alfamir.notification.presentation.dto.Notification;
import ru.alfabank.alfamir.notification.presentation.dto.OpenInfo;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatter;

import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_10;
import static ru.alfabank.alfamir.Constants.DateFormatter.DATE_PATTERN_7;
import static ru.alfabank.alfamir.Constants.DateFormatter.TIME_ZONE_GREENWICH;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BIRTHDAY;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BLOG;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BLOG_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_COMMUNITY;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_COMMUNITY_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_MEDIA;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_MEDIA_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_NEWS;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_NEWS_COMMENT;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_SECRET_SANTA;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_SURVEY;
import static ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_VACATION;

public class NotificationMapper implements Function<NotificationRaw, Notification> {

    private AuthorMapper mAuthorMapper;
    private OpenInfoMapper mOpenInfoMapper;
    private DisplayInfoMapper mDisplayInfoMapper;
    private DateFormatter mDateFormatter;
    @Inject
    NotificationMapper(AuthorMapper authorMapper,
                       OpenInfoMapper openInfoMapper,
                       DisplayInfoMapper displayInfoMapper,
                       DateFormatter dateFormatter){
        mAuthorMapper = authorMapper;
        mOpenInfoMapper = openInfoMapper;
        mDisplayInfoMapper = displayInfoMapper;
        mDateFormatter = dateFormatter;
    }

    @Override
    public Notification apply(NotificationRaw notificationRaw) throws Exception {
        AuthorRaw authorRaw = notificationRaw.getAuthor();
        String dateUnformatted = notificationRaw.getDate();
        DisplayInfoRaw[] displayInfoRaws = notificationRaw.getDisplayInfo();
        long id = notificationRaw.getId();
        String imageUrl = notificationRaw.getImageUrl();
        OpenInfoRaw openInfoRaw = notificationRaw.getOpenInfo();
        String text = notificationRaw.getText();

        String date = mDateFormatter.formatDate(dateUnformatted, DATE_PATTERN_10,
                DATE_PATTERN_7, TIME_ZONE_GREENWICH);
        Author author = mAuthorMapper.apply(authorRaw);
        List<DisplayInfo> displayInfoList = new ArrayList<>();
        for (int i = 0; i < displayInfoRaws.length; i++){
            DisplayInfo displayInfo = mDisplayInfoMapper.apply(displayInfoRaws[i]);
            displayInfoList.add(displayInfo);
        }
        OpenInfo openInfo = mOpenInfoMapper.apply(openInfoRaw);
        int notificationType = getNotificationType(openInfo);
        return new Notification.Builder()
                .author(author)
                .notificationType(notificationType)
                .date(dateUnformatted)
                .displayInfo(displayInfoList)
                .id(id)
                .imageUrl(imageUrl)
                .openInfo(openInfo)
                .text(text)
                .build();

    }

    private int getNotificationType(OpenInfo openInfo){
        String notificationType = openInfo.getNotificationType();
        switch (notificationType){
            case "birthday": return NOTIFICATION_TYPE_BIRTHDAY;
            case "santa": return NOTIFICATION_TYPE_SECRET_SANTA;
            case "vacation": return NOTIFICATION_TYPE_VACATION;
            case "survey": return NOTIFICATION_TYPE_SURVEY;
            case "news": {
                if(openInfo.getIsComment()==0){
                    return NOTIFICATION_TYPE_NEWS;
                } else {
                    return NOTIFICATION_TYPE_NEWS_COMMENT;
                }
            }
            case "community": {
                if(openInfo.getIsComment()==0){
                    return NOTIFICATION_TYPE_COMMUNITY;
                } else {
                    return NOTIFICATION_TYPE_COMMUNITY_COMMENT;
                }
            }
            case "blog": {
                if(openInfo.getIsComment()==0){
                    if(openInfo.getFeedUrl().equals("http://alfa/Info/photostream")){
                        return NOTIFICATION_TYPE_MEDIA;
                    } else {
                        return NOTIFICATION_TYPE_BLOG;
                    }
                } else {
                    if(openInfo.getFeedUrl().equals("http://alfa/Info/photostream")){
                        return NOTIFICATION_TYPE_MEDIA_COMMENT;
                    } else {
                        return NOTIFICATION_TYPE_BLOG_COMMENT;
                    }
                }
            }
        }
        return -1;
    }

}