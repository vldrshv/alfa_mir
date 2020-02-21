package ru.alfabank.alfamir.feed_new.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.PostParametersRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.PostParameters;

public class PostParametersMapper implements Function<PostParametersRaw, PostParameters> {

    @Inject
    public PostParametersMapper(){}

    @Override
    public PostParameters apply(PostParametersRaw postParametersRaw) {
        int likeEnabled = postParametersRaw.getLikeEnabled();
        int commentEnabled = postParametersRaw.getCommentEnabled();
        int menuEnabled = postParametersRaw.getMenuEnabled();
        int headingVisible = postParametersRaw.getHeadingVisible();
        int titleVisible = postParametersRaw.getTitleVisible();
        PostParameters postParameters = new PostParameters.Builder()
                .likeEnabled(likeEnabled)
                .commentEnabled(commentEnabled)
                .menuEnabled(menuEnabled)
                .headingVisible(headingVisible)
                .titleVisible(titleVisible)
                .build();
        return postParameters;
    }

}