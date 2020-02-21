package ru.alfabank.alfamir.data.dto.comment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by U_M0WY5 on 23.04.2018.
 */

public interface CommentInterface {
    String getBodytext();

    void setBodytext(String bodytext);

    String getCommentid();

    void setCommentid(String commentid);

    String getCommentParentid();

    void setCommentParentid(String commentParentid);

    String getCreatetime();

    void setCreatetime(String createtime);

    String getUpdatedtime();

    void setUpdatedtime(String updatedtime);

    int getLikecount();

    void setLikecount(int likecount);

    int getCurrentuserlike();

    void setCurrentuserlike(int currentuserlike);

    Comment.Author getAuthor();

    void setAuthor(Comment.Author author);

    int getCandelete();

    void setCandelete(int candelete);

    class Author {
        String id;
        @SerializedName("fullname")
        String name;
        @SerializedName("photobase64")
        String picLink;
        @SerializedName("jobtitle")
        String title;

        public String getPicLink() {
            return picLink;
        }

        public void setPicLink(String picLink) {
            this.picLink = picLink;
        }

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
