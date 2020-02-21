package ru.alfabank.alfamir.feed_new.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.PostRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.Author;
import ru.alfabank.alfamir.feed_new.presentation.dto.Post;
import ru.alfabank.alfamir.feed_new.presentation.dto.PostInteractionElement;
import ru.alfabank.alfamir.feed_new.presentation.dto.PostParameters;

public class PostMapper implements Function<PostRaw, Post> {

    private AuthorMapper mAuthorMapper;
    private PostInteractionElementMapper mPostInteractionElementMapper;
    private PostParametersMapper mPostParametersMapper;

    @Inject
    public PostMapper(AuthorMapper authorMapper,
                      PostInteractionElementMapper postInteractionElementMapper,
                      PostParametersMapper postParametersMapper){
        mAuthorMapper = authorMapper;
        mPostInteractionElementMapper = postInteractionElementMapper;
        mPostParametersMapper = postParametersMapper;
    }

    @Override
    public Post apply(PostRaw postRaw) {
        String id = postRaw.getId();
        String title = postRaw.getTitle();
        String pubTime = postRaw.getPubTime();
        String updateTime = postRaw.getUpdateTime();
        int likeCount = postRaw.getLikeCount();
        int currentUserLike = postRaw.getCurrentUserLike();
        int commentCount = postRaw.getCommentCount();
        String htmlContent = postRaw.getHtmlContent();
        String imageUrl = postRaw.getImageUrl();
        Author author = mAuthorMapper.apply(postRaw.getAuthor());
        String feedUrl =postRaw.getFeedUrl();
        String threadId = postRaw.getThreadId();
        String feedType = postRaw.getFeedType();
        int commentEnabled = postRaw.getCommentEnabled();
        String heading = postRaw.getHeading();
        int deleteEnabled = postRaw.getDeleteEnabled();
        int isFavorite = postRaw.getIsFavorite();
        int isSubscribed = postRaw.getIsSubscribed();
        PostInteractionElement interactionElement = mPostInteractionElementMapper.apply(postRaw.getInteractionElement());
        String contentPreviewText = postRaw.getContentPreviewText();
        PostParameters postParameters = mPostParametersMapper.apply(postRaw.getPostParameters());
        Post post = new Post.Builder()
                .id(id)
                .title(title)
                .pubTime(pubTime)
                .updateTime(updateTime)
                .likeCount(likeCount)
                .currentUserLike(currentUserLike)
                .commentCount(commentCount)
                .htmlContent(htmlContent)
                .imageUrl(imageUrl)
                .author(author)
                .feedUrl(feedUrl)
                .threadId(threadId)
                .feedType(feedType)
                .commentEnabled(commentEnabled)
                .heading(heading)
                .deleteEnabled(deleteEnabled)
                .isFavorite(isFavorite)
                .isSubscribed(isSubscribed)
                .interactionElement(interactionElement)
                .contentPreviewText(contentPreviewText)
                .postParameters(postParameters)
                .build();
        return post;
    }

}