package ru.alfabank.alfamir.feed_new.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.feed_new.data.dto.ImageElementRaw;
import ru.alfabank.alfamir.feed_new.data.dto.PostInteractionElementRaw;
import ru.alfabank.alfamir.feed_new.data.dto.UriElementRaw;
import ru.alfabank.alfamir.feed_new.data.dto.VideoElementRaw;
import ru.alfabank.alfamir.feed_new.presentation.dto.ImageElement;
import ru.alfabank.alfamir.feed_new.presentation.dto.PostInteractionElement;
import ru.alfabank.alfamir.feed_new.presentation.dto.UriElement;
import ru.alfabank.alfamir.feed_new.presentation.dto.VideoElement;

public class PostInteractionElementMapper implements Function<PostInteractionElementRaw, PostInteractionElement> {

    private VideoElementMapper mVideoElementMapper;
    private ImageElementMapper mImageElementMapper;
    private UriElementMapper mUriElementMapper;

    @Inject
    public PostInteractionElementMapper(VideoElementMapper videoElementMapper,
                                        ImageElementMapper imageElementMapper,
                                        UriElementMapper uriElementMapper){
        mVideoElementMapper = videoElementMapper;
        mImageElementMapper = imageElementMapper;
        mUriElementMapper = uriElementMapper;
    }

    @Override
    public PostInteractionElement apply(PostInteractionElementRaw postInteractionElementRaw) {
        ImageElement[] imageArray = mapImageElement(postInteractionElementRaw.getImageArray());
        VideoElement[] videoArray = mapVideoElement(postInteractionElementRaw.getVideoArray());
        UriElement[] uriArray = mapUriElement(postInteractionElementRaw.getUriArray());
        PostInteractionElement postInteractionElement = new PostInteractionElement.Builder()
                .imageArray(imageArray)
                .videoArray(videoArray)
                .uriArray(uriArray)
                .build();
        return postInteractionElement;
    }

    private ImageElement[] mapImageElement(ImageElementRaw[] imageRawArray){
        if(imageRawArray==null) return new ImageElement[0];
        ImageElement[] imageArray = new ImageElement[imageRawArray.length];
        for (int i = 0; i < imageRawArray.length; i++){
            imageArray[i] = mImageElementMapper.apply(imageRawArray[i]);
        }
        return imageArray;
    }

    private VideoElement[] mapVideoElement(VideoElementRaw[] videoElementArray){
        if(videoElementArray == null) return new VideoElement[0];
        VideoElement[] videoElements = new VideoElement[videoElementArray.length];
        for (int i = 0; i < videoElementArray.length; i++){
            videoElements[i] = mVideoElementMapper.apply(videoElementArray[i]);
        }
        return videoElements;
    }

    private UriElement[] mapUriElement(UriElementRaw[] uriElementRawArray){
        if(uriElementRawArray == null) return new UriElement[0];
        UriElement[] uriElementArray = new UriElement[uriElementRawArray.length];
        for (int i = 0; i < uriElementRawArray.length; i++){
            uriElementArray[i] = mUriElementMapper.apply(uriElementRawArray[i]);
        }
        return uriElementArray;
    }

}