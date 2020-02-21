package ru.alfabank.alfamir.feed_new.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.HeaderRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.Header;

public class HeaderMapper implements Function<HeaderRaw, Header> {

    @Inject
    public HeaderMapper(){}

    @Override
    public Header apply(HeaderRaw headerRaw) throws Exception {
        String title = headerRaw.getTitle();
        String description = headerRaw.getDescription();
        int isPersonal = headerRaw.getIsPersonal();
        String imageUrl = headerRaw.getImageUrl();
        String placeholderImage = headerRaw.getPlaceholderImage();
        String authorImageUrl = headerRaw.getAuthorImageUrl();
        String pubTime = headerRaw.getPubTime();
        String feedType = headerRaw.getFeedType();
        String feedUrl = headerRaw.getFeedUrl();
        int isSubscribed = headerRaw.getIsSubscribed();
        int canPublish = headerRaw.getCanPublish();
        Header header = new Header.Builder()
                .title(title)
                .description(description)
                .isPersonal(isPersonal)
                .imageUrl(imageUrl)
                .placeholderImage(placeholderImage)
                .authorImageUrl(authorImageUrl)
                .pubTime(pubTime)
                .feedType(feedType)
                .feedUrl(feedUrl)
                .isSubscribed(isSubscribed)
                .canPublish(canPublish)
                .build();
        return header;
    }
}