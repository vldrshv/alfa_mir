package ru.alfabank.alfamir.image.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.image.data.dto.ImageRaw;
import ru.alfabank.alfamir.image.presentation.dto.Image;

public class ImageMapper implements Function<ImageRaw, Image> {

    @Inject
    public ImageMapper(){}

    @Override
    public Image apply(ImageRaw imageRaw) {
        String picUrl = imageRaw.getImageUrl();
        int picHeight = imageRaw.getImageHeight();
        int picWidth = imageRaw.getImageWidth();
        int isOriginal = imageRaw.getIsOriginal();
        boolean isCached = imageRaw.getIsCached();
        String encodedImage = imageRaw.getEncodedImage();
        Image image = new Image.Builder()
                .picUrl(picUrl)
                .picHeight(picHeight)
                .picWidth(picWidth)
                .isOriginal(isOriginal)
                .encodedImage(encodedImage)
                .isCached(isCached)
                .build();
        return image;
    }

}