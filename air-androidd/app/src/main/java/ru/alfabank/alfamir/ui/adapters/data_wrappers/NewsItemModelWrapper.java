package ru.alfabank.alfamir.ui.adapters.data_wrappers;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.dto.comment.CommentInterface;
import ru.alfabank.alfamir.data.dto.old_trash.models.news_block.TextBlocks;
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler;

/**
 * Created by U_M0WY5 on 26.10.2017.
 */

public class NewsItemModelWrapper {

    private String mPostImageUrl;
    private List<TextBlocks> mAndroidricharray;
    private List<NewsItemElement> mElements;
    private NewsItemElement mNewsItemElement;
    private List<CommentInterface> mComments;
    private CommentsSet iCommentsSet;
    private int mNewsItemPartCounter = 0;
    private int mCommentsCounter;

//    public NewsItemModelWrapper (List<TextBlocks> androidricharray) {
//        this.androidricharray = androidricharray;
//        list = new ArrayList<>();
//        prepareList();
//    }

    public NewsItemModelWrapper (List<TextBlocks> androidricharray, String postImageUrl) {
        mElements = new ArrayList<>();
        mAndroidricharray = androidricharray;
        if(Strings.isNullOrEmpty(postImageUrl)){
            prepareList();
            return;
        }
        mPostImageUrl = postImageUrl;
        prepareListForMedia();
    }

    private void prepareListForMedia(){
        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_HEADER);
        mElements.add(mNewsItemPartCounter, mNewsItemElement);
        mNewsItemPartCounter++;

        mNewsItemElement = new NewsItemElement("", mPostImageUrl, null, Constants.Post.POST_PICTURE);
        mElements.add(mNewsItemPartCounter, mNewsItemElement);
        mNewsItemPartCounter++;

        if(mAndroidricharray.size()!=0){
            mNewsItemElement = new NewsItemElement(mAndroidricharray.get(0).getContent(), "", null, Constants.Post.POST_TEXT);
            mElements.add(mNewsItemPartCounter, mNewsItemElement);
            mNewsItemPartCounter++;
        }

        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_FOOTER);
        mElements.add(mNewsItemPartCounter++, mNewsItemElement);

        /* blank basement */
        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_EXTRA_SPACE);
        mElements.add(mNewsItemPartCounter, mNewsItemElement);
    }

    private void prepareList(){
        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_HEADER);
        mElements.add(mNewsItemPartCounter, mNewsItemElement);
        mNewsItemPartCounter++;
        for (int i = 0; i < mAndroidricharray.size(); i++){
            if(mAndroidricharray.get(i).getType().equals("text")){
                mNewsItemElement = new NewsItemElement(mAndroidricharray.get(i).getContent(), "", null, Constants.Post.POST_TEXT);
            } else if(mAndroidricharray.get(i).getType().equals("img")){
                mNewsItemElement = new NewsItemElement("", mAndroidricharray.get(i).getContent(), null, Constants.Post.POST_PICTURE);
            }

            mElements.add(mNewsItemPartCounter, mNewsItemElement);
            mNewsItemPartCounter++;
        }

        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_FOOTER);
        mElements.add(mNewsItemPartCounter++, mNewsItemElement);

        /* blank basement */
        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_EXTRA_SPACE);
        mElements.add(mNewsItemPartCounter, mNewsItemElement);
    }

    private void updateList(){
        int oldSize = mElements.size();

        // if list contains some old comments, clear it

        if(mElements.size()!= mNewsItemPartCounter +1){
            for (int i = mElements.size()-1; i >= mNewsItemPartCounter; i--){
                mElements.remove(i);
            }
        } else {
            mElements.remove(mElements.size()-1);
        }
        mCommentsCounter = mNewsItemPartCounter;
        for (int i = 0; i < mComments.size(); i++){
            mNewsItemElement = new NewsItemElement("", "", mComments.get(i), Constants.Post.POST_COMMENT);
            mElements.add(mCommentsCounter++, mNewsItemElement);
        }

        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_EXTRA_SPACE);
        mElements.add(mCommentsCounter++, mNewsItemElement);

        int newSize = mElements.size();
        iCommentsSet.commentsSet(mNewsItemPartCounter, oldSize, newSize);
    }

    private int[] updateListNew(){
        int oldSize = mElements.size();

        // if list contains some old comments, clear it

        if(mElements.size()!= mNewsItemPartCounter +1){
            for (int i = mElements.size()-1; i >= mNewsItemPartCounter; i--){
                mElements.remove(i);
            }
        } else {
            mElements.remove(mElements.size()-1);
        }
        mCommentsCounter = mNewsItemPartCounter;
        for (int i = 0; i < mComments.size(); i++){
            mNewsItemElement = new NewsItemElement("", "", mComments.get(i), Constants.Post.POST_COMMENT);
            mElements.add(mCommentsCounter++, mNewsItemElement);
        }

        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_EXTRA_SPACE);
        mElements.add(mCommentsCounter++, mNewsItemElement);

        int newSize = mElements.size();
        return new int[]{ oldSize, newSize };
    }

    public void setComments(List<CommentInterface> comments, CommentsSet commentsSet){
        this.mComments = comments;
        this.iCommentsSet = commentsSet;
        updateList();
    }

    public int[] setCommentsNew(List<CommentInterface> comments){
        this.mComments = comments;
        return updateListNew();
    }

    public void setComments(List<CommentInterface> comments){
        this.mComments = comments;
        int oldSize = mElements.size();

        // if list contains some old comments, clear it

        if(mElements.size()!= mNewsItemPartCounter +1){
            for (int i = mElements.size()-1; i >= mNewsItemPartCounter; i--){
                mElements.remove(i);
            }
        } else {
            mElements.remove(mElements.size()-1);
        }
        mCommentsCounter = mNewsItemPartCounter;
        for (int i = 0; i < comments.size(); i++){
            mNewsItemElement = new NewsItemElement("", "", comments.get(i), Constants.Post.POST_COMMENT);
            mElements.add(mCommentsCounter++, mNewsItemElement);
        }

        mNewsItemElement = new NewsItemElement("", "", null, Constants.Post.POST_EXTRA_SPACE);
        mElements.add(mCommentsCounter++, mNewsItemElement);
    }

    private class NewsItemElement {
        String postText;
        String postPic;
        CommentInterface comment;
        int type;
        NewsItemElement(String postText, String postPic, CommentInterface comment, int type){
            this.postText = postText;
            this.postPic = postPic;
            this.comment = comment;
            this.type = type;
        }
    }

    public int size(){
        return mElements.size();
    }

    public int postItemsSize(){
        return mNewsItemPartCounter;
    }

    public int getItemViewType(int position){
        return mElements.get(position).type;
    }

    public String getText(int position){
        return mElements.get(position).postText;
    }

    public String getPicUrl(int position){
        return mElements.get(position).postPic;
    }

    public String getPicUrlNew(int position){
        return LinkHandler.getPhotoLink(mElements.get(position).postPic);
    }
    public int getPicHeight(int position){
        String [] parameters = (mElements.get(position).postPic.substring(mElements.get(position).postPic.indexOf(";#"),
                mElements.get(position).postPic.length())).split(";#");
        String width = parameters[1];
        String height = parameters[2];

        int w = Integer.parseInt(width);
        int h = Integer.parseInt(height);
        double coef = (double) Constants.Initialization.getSCREEN_WIDTH_PHYSICAL() /w;
        int properHight = (int)(h* coef);
        return properHight;
    }



    public CommentInterface getComment(int position){
        return mElements.get(position).comment;
//        if(position!=-1){
//            return list.get(position).comment;
//        } else {
//            return null;
//        }
    }

    public interface CommentsSet{
        void commentsSet(int postItemsSide, int oldSize, int newSize);
    }

    public void remove(int position){
        if(position!= mElements.size()){
            mElements.remove(position);
        }
    }

    public int getCommentPosition(String commentId){
        for (int i = 0; i < mComments.size(); i++){
            if(mComments.get(i).getCommentid().contains(commentId)){
                return i;
            }
        }
        return -1;
    }
}
