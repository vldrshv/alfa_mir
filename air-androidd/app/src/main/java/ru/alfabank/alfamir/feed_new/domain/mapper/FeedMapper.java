package ru.alfabank.alfamir.feed_new.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.FeedRaw;
import ru.alfabank.alfamir.feed_new.data.dto.PostRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.Feed;
import ru.alfabank.alfamir.feed_new.presentation.dto.Header;
import ru.alfabank.alfamir.feed_new.presentation.dto.Post;

public class FeedMapper implements Function<FeedRaw, Feed> {

    private HeaderMapper mHeaderMapper;
    private PostMapper mPostMapper;

    @Inject
    public FeedMapper(HeaderMapper headerMapper, PostMapper postMapper){
        mHeaderMapper = headerMapper;
        mPostMapper = postMapper;
    }

    @Override
    public Feed apply(FeedRaw feedRaw) throws Exception {
        Header header = mHeaderMapper.apply(feedRaw.getHeader());
        List<Post> postList = mapPost(feedRaw.getPosts());
        Feed feed = new Feed.Builder()
                .header(header)
                .posts(postList)
                .build();
        return feed;
    }

    private List<Post> mapPost(List<PostRaw> postRawList){
        List<Post> postList = new ArrayList<>();
        for (PostRaw postRaw : postRawList){
            postList.add(mPostMapper.apply(postRaw));
        }
        return postList;
    }

}