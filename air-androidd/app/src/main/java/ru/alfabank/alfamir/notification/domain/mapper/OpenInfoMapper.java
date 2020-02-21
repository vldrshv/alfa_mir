package ru.alfabank.alfamir.notification.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.notification.data.dto.OpenInfoRaw;
import ru.alfabank.alfamir.notification.presentation.dto.OpenInfo;

public class OpenInfoMapper implements Function<OpenInfoRaw, OpenInfo> {

    @Inject
    OpenInfoMapper(){ }

    @Override
    public OpenInfo apply(OpenInfoRaw openInfoRaw) throws Exception {
        String commentId = openInfoRaw.getCommentId();
        String feedType = openInfoRaw.getFeedType();
        String feedUrl = openInfoRaw.getFeedUrl();
        int isComment = openInfoRaw.getIsComment();
        String postId = openInfoRaw.getPostId();
        String userLogin = openInfoRaw.getUserLogin();
        return new OpenInfo.Builder()
                .commentId(commentId)
                .notificationType(feedType)
                .feedUrl(feedUrl)
                .isComment(isComment)
                .postId(postId)
                .userLogin(userLogin)
                .build();
    }
}