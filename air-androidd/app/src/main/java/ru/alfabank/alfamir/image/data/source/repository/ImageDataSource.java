package ru.alfabank.alfamir.image.data.source.repository;

import io.reactivex.Observable;
import ru.alfabank.alfamir.image.data.dto.ImageRaw;

public interface ImageDataSource {

    Observable<ImageRaw> getImage(RequestValues requestValues);

    Observable<String> getImage(String guid, boolean isHD);

    void uploadImage(String encodedImage, UploadImageCallback callback);

    interface UploadImageCallback {
        void onImageUploaded();

        void onServerNotAvailable();
    }

    class RequestValues {
        private String mPicUrl;
        private int mHeight;
        private int mWidth;
        private int mIsOriginal;

        public RequestValues(String picUrl,
                             int height,
                             int width,
                             int isOriginal) {
            mPicUrl = picUrl;
            mHeight = height;
            mWidth = width;
            mIsOriginal = isOriginal;
        }

        public String getPicUrl() {
            return mPicUrl;
        }

        public int getHeight() {
            return mHeight;
        }

        public int getWeight() {
            return mWidth;
        }

        public int isIsOriginal() {
            return mIsOriginal;
        }
    }

}
