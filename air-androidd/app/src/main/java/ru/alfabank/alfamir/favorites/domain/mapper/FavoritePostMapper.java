package ru.alfabank.alfamir.favorites.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.favorites.data.dto.FavoritePostRaw;
import ru.alfabank.alfamir.favorites.presentation.dto.FavoritePost;

public class FavoritePostMapper implements Function<FavoritePostRaw, FavoritePost> {

    @Inject
    public FavoritePostMapper(){ }

    @Override
    public FavoritePost apply(FavoritePostRaw favoritePostRaw) throws Exception {
        String feedType = favoritePostRaw.getFeedType();
        String feedUrl = favoritePostRaw.getFeedUrl();
        String imageUrl = favoritePostRaw.getImageUrl();
        String postId = favoritePostRaw.getPostId();
        String pubDate = favoritePostRaw.getPubDate();
        String title = favoritePostRaw.getTitle();
        FavoritePost favoritePost = new FavoritePost.Builder()
                .feedType(feedType)
                .feedUrl(feedUrl)
                .imageUrl(imageUrl)
                .postId(postId)
                .pubDate(pubDate)
                .title(title)
                .build();
        return favoritePost;
    }
}