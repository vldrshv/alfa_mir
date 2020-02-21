package ru.alfabank.alfamir.feed_new.presentation.dto;

public class VideoElement {
    private String mId;
    private String mPostUrl;

    private VideoElement(
            String id,
            String postUrl) {
        mId = id;
        mPostUrl = postUrl;
    }

    public String getId() {
        return mId;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public static class Builder {
        private String mArchiveId;
        private String mId;
        private String mPostUrl;
        private int mPostWidth;
        private int mPostHeight;

        public Builder archiveId(String archiveId) {
            mArchiveId = archiveId;
            return this;
        }

        public Builder id(String id) {
            mId = id;
            return this;
        }

        public Builder postUrl(String postUrl) {
            mPostUrl = postUrl;
            return this;
        }

        public Builder postWidth(int postWidth) {
            mPostWidth = postWidth;
            return this;
        }

        public Builder postHeight(int postHeight) {
            mPostHeight = postHeight;
            return this;
        }

        public VideoElement build() {
            return new VideoElement(mId, mPostUrl);
        }
    }

}
