package ru.alfabank.alfamir.main.home_fragment.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.main.home_fragment.data.dto.TopNewsRaw;
import ru.alfabank.alfamir.main.home_fragment.presentation.dto.TopNews;

public class TopNewsMapper implements Function<TopNewsRaw, TopNews> {

    @Inject
    public TopNewsMapper(){ }

    @Override
    public TopNews apply(TopNewsRaw pageRaw) throws Exception {
        String url = pageRaw.getUrl();
        String title = pageRaw.getTitle();
        String picUrl = pageRaw.getPicUrl();
        TopNews topNews = new TopNews.Builder()
                .url(url)
                .title(title)
                .picUrl(picUrl)
                .build();
        return topNews;
    }
}