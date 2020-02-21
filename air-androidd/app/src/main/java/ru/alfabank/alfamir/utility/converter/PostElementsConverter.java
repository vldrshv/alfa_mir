package ru.alfabank.alfamir.utility.converter;


import java.util.List;

import javax.inject.Inject;

import ru.alfabank.alfamir.data.dto.comment.Comment;
import ru.alfabank.alfamir.post.data.dto.PostRaw;
import ru.alfabank.alfamir.post.presentation.dto.PostElement;
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker;

public class PostElementsConverter {

    private InitialsMaker mInitialsMaker;
    private PostElementsBuilder builder;

    @Inject
    public PostElementsConverter(InitialsMaker initialsMaker) {
        mInitialsMaker = initialsMaker;
        builder = new PostElementsBuilder(initialsMaker);
    }

    /**
     * method returns elements of comment to view holder
     * @param comments - parsed comments from json
     * @return list of Post Comments
     */
    public List<PostElement> getCommentElements(List<Comment> comments) {
        return builder.convertToPostElements(comments);
    }


    /**
     * Method get the parsed response from server and make a list of Elements to display it as content
     * @param post - parsed response with header, base info, pictures, sub-headers and videos. Can be in HTML format or in Blocks format.
     * @return list of Post Elements
     */
    public List<PostElement> getPostElements(PostRaw post) {
        return builder.convertToPostElements(post);
    }
}
