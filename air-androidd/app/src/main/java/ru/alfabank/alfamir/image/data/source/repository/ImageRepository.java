package ru.alfabank.alfamir.image.data.source.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.image.data.dto.ImageRaw;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;


@Singleton
public class ImageRepository {

    private final ImageDataSource mImageRemoteDataSource;
    private final Map<String, ImageRaw> mImageRawCache = new LinkedHashMap<>();
    private List<String> mRequestPull = new ArrayList<>();
    private LogWrapper mLogWrapper;

    @Inject
    public ImageRepository(@Remote ImageDataSource imageRemoteDataSource,
                           LogWrapper logWrapper) {
        mImageRemoteDataSource = imageRemoteDataSource;
        mLogWrapper = logWrapper;
    }

    public Observable<ImageRaw> getImage(ImageDataSource.RequestValues requestValues) {

        return mImageRemoteDataSource.getImage(requestValues)
                .flatMap(imageRaw -> {
                    removeFromRequestPull(imageRaw.getImageUrl());
                    saveToCache(imageRaw);
                    return Observable.just(imageRaw);
                });
    }

    public Observable<String> getImage(String guid, boolean isHD) {
        return mImageRemoteDataSource.getImage(guid, isHD);
    }

    public void uploadImage(String encodedImage, ImageDataSource.UploadImageCallback callback) {
        mImageRemoteDataSource.uploadImage(encodedImage, new ImageDataSource.UploadImageCallback() {
            @Override
            public void onImageUploaded() {
                callback.onImageUploaded();
            }

            @Override
            public void onServerNotAvailable() {
                callback.onServerNotAvailable();
            }
        });
    }

    public void refreshImage() {
        boolean mIsCacheDirty = true;
    }

    public void dropCache() { // TODO have to consider
        synchronized (mImageRawCache) {
            mImageRawCache.clear();
        }
    }

    private void saveToCache(ImageRaw img) {
        synchronized (mImageRawCache) {
            mImageRawCache.put(img.getImageUrl(), img);
        }
    }

    private void removeFromRequestPull(String url) {
        synchronized (mRequestPull) {
            mRequestPull.remove(url);
        }
    }
}
