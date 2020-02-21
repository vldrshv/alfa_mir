package ru.alfabank.alfamir.search.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.search.data.dto.PageRaw;
import ru.alfabank.alfamir.search.presentation.dto.Page;

public class PageMapper implements Function<PageRaw, Page> {

    @Inject
    PageMapper(){ }

    @Override
    public Page apply(PageRaw pageRaw) throws Exception {
        String feedId = pageRaw.getFeedId();
        String feedType = pageRaw.getFeedType();
        String id = pageRaw.getId();
        String imageUrl = pageRaw.getImageUrl();
        String publishedDate = pageRaw.getPublishedDate();
        String title = pageRaw.getTitle();
        Page page = new Page.Builder()
                .id(id)
                .feedId(feedId)
                .feedType(feedType)
                .feedId(feedId)
                .publishedDate(publishedDate)
                .imageUrl(imageUrl)
                .title(title)
                .build();
        return page;
    }
}