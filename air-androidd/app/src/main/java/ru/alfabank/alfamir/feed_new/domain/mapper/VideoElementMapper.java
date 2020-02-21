package ru.alfabank.alfamir.feed_new.domain.mapper;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.VideoElementRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.VideoElement;

import javax.inject.Inject;

public class VideoElementMapper implements Function<VideoElementRaw, VideoElement> {

    @Inject
    public VideoElementMapper(){}

    @Override
    public VideoElement apply(VideoElementRaw messageRaw) {
        String archiveId = messageRaw.getArchiveId();
        String id = messageRaw.getId();
        String postUrl = messageRaw.getPostUrl();
        int postWidth = messageRaw.getPostWidth();
        int postHeight = messageRaw.getPostHeight();
        return new VideoElement.Builder()
                .id(id)
                .archiveId(archiveId)
                .postUrl(postUrl)
                .postWidth(postWidth)
                .postHeight(postHeight)
                .build();
    }

}