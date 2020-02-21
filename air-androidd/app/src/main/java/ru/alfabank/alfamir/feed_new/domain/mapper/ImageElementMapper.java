package ru.alfabank.alfamir.feed_new.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.ImageElementRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.ImageElement;

public class ImageElementMapper implements Function<ImageElementRaw, ImageElement> {

    @Inject
    public ImageElementMapper(){}

    @Override
    public ImageElement apply(ImageElementRaw messageRaw) {
        String id = messageRaw.getId();
        String url = messageRaw.getUrl();
        int height = messageRaw.getHeight();
        int width = messageRaw.getWidth();
        ImageElement imageElement = new ImageElement.Builder()
                .id(id)
                .url(url)
                .height(height)
                .width(width)
                .build();
        return imageElement;
    }

}