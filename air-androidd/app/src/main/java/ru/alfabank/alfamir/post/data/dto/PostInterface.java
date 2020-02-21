package ru.alfabank.alfamir.post.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.alfabank.alfamir.data.dto.old_trash.models.news_block.TextBlocks;

public interface PostInterface {

    String getId();

    String getTitle();

    String getPubTime();

    String getUpdTime();

    int getLikes();

    void setLikes(int likes);

    int getCurrentUserLike();

    void setCurrentUserLike(int status);

    int getCommentsCount();

    String getBody();

    String getImageUrl();

    String getTags();

    String getPostUrl();

    String getThreadId();

    String getType();

    int isCommentable();

    String getHeadingTitle();

    int isDeletable();

    int getIsFavorite();

    String getImageUrls();

    List<TextBlocks> getTextBlocks();

    int getIsSubscribed();

    PostRaw.Video getVideo_info();

    ModelAuthor getAuthor();

    class ModelAuthor {
        String id;
        @SerializedName("fullname")
        String name;
        @SerializedName("photobase64")
        String picLink;
        @SerializedName("jobtitle")
        String title;

        public String getId() {
            return id.toLowerCase();
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicLink() {
            return picLink;
        }

        public void setPicLink(String picLink) {
            this.picLink = picLink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    class TextBlock implements TextBlocks {
        private String type;
        private String content;


        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getContent() {
            return content;
        }
    }

    class Video {
        @SerializedName("video_description")
        String description;
        @SerializedName("video_title")
        String title;
        @SerializedName("video_thumb")
        String thumbnail;
        String id;

        public String getDescription() {
            return description;
        }

        public String getTitle() {
            return title;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getId() {
            return id;
        }
    }

    class InteractionElement {
        @SerializedName("page_images_array")
        PostImage[] images;

        @SerializedName("page_videos_array")
        PostVideo[] videos;

        @SerializedName("page_references_array")
        PostUri [] uris;

        public PostImage[] getImages() {
            return images;
        }

        public PostVideo[] getVideos() {
            return videos;
        }

        public PostUri[] getUris() { return uris; }
    }

    class PostVideo {
        @SerializedName("id")
        String archive_id;
        @SerializedName("html_id")
        String id;
        @SerializedName("poster_url")
        String poster_url;
        @SerializedName("poster_width")
        int poster_width;
        @SerializedName("poster_height")
        int poster_height;

        public String getArchive_id() {
            return archive_id;
        }

        public String getId() {
            return id;
        }

        public String getPosterUrl() {
            return poster_url;
        }

        public int getPoster_width() {
            return poster_width;
        }

        public int getPoster_height() {
            return poster_height;
        }
    }

    class PostImage {
        @SerializedName("id")
        String id;
        @SerializedName("height")
        int height;
        @SerializedName("width")
        int width;
        @SerializedName("url")
        String url;

        public String getId() {
            return id;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String getUrl() {
            return url;
        }
    }

    class PostUri {
        @SerializedName("id")
        String id;
        @SerializedName("ref_type")
        String type;
        @SerializedName("content")
        String value;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    interface ContentElement { }


    class ContentElementImage implements ContentElement {
        @SerializedName("image_url")
        String imageUrl;
        @SerializedName("height")
        int height;
        @SerializedName("width")
        int width;

        public String getImageUrl() {
            return imageUrl;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    class ContentElementHtml implements ContentElement {

        @SerializedName("content")
        String mHtml;

        public String getHtml() {
            return mHtml;
        }
    }

    class Options {
        @SerializedName("like_available")
        int likesEnabled;
        @SerializedName("comment_available")
        int commentsEnabled;
        @SerializedName("menu_available")
        int optionsMenuEnabled;
        @SerializedName("rubric_visible")
        int headingVisible;
        @SerializedName("title_visible")
        int titleVisible;

        public int getLikesEnabled() {
            return likesEnabled;
        }

        public int getCommentsEnabled() {
            return commentsEnabled;
        }

        public int getOptionsMenuEnabled() {
            return optionsMenuEnabled;
        }

        public int getHeadingVisible() {
            return headingVisible;
        }

        public int getTitleVisible() {
            return titleVisible;
        }
    }

}
