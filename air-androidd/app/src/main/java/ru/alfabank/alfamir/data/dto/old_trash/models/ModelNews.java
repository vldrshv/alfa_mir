package ru.alfabank.alfamir.data.dto.old_trash.models;

/**
 * Created by U_M0WY5 on 27.07.2017.
 */

public class ModelNews extends AbstractModelNews {

    private FeedHeader header;

    public ModelNews (FeedHeader header){
        this.header = header;
    }

    public FeedHeader getHeader() {
        return header;
    }
}
