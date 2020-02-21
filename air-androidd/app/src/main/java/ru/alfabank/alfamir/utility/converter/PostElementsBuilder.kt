package ru.alfabank.alfamir.utility.converter

import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.Constants.Companion.POST_VIEW
import ru.alfabank.alfamir.Constants.Post.POST_SUB_HEADER
import ru.alfabank.alfamir.data.dto.comment.Author
import ru.alfabank.alfamir.data.dto.comment.Comment
import ru.alfabank.alfamir.data.dto.old_trash.models.news_block.TextBlocks
import ru.alfabank.alfamir.post.data.dto.JsonPage
import ru.alfabank.alfamir.post.data.dto.PostInterface.ContentElementHtml
import ru.alfabank.alfamir.post.data.dto.PostInterface.ContentElementImage
import ru.alfabank.alfamir.post.data.dto.PostRaw
import ru.alfabank.alfamir.post.presentation.dto.*
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler.getPhotoLink
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler.getPhotoSize
import java.util.*

class PostElementsBuilder(private val initialsMaker: InitialsMaker) {
    fun convertToPostElements(comments: List<Comment>): List<PostElement>  {
        val builder = CommentsBuilder()
        val postElements: MutableList<PostElement> = ArrayList()
        for (comment in comments) {
            val author: Author = builder.makeAuthorFromComment(comment)
            val commentElement: CommentElement = builder.makeCommentElementFromComment(comment, author)
            postElements.add(commentElement)
        }
        return postElements
    }

    fun convertToPostElements(post: PostRaw): List<PostElement>  {
        val builder = MainBodyBuilder(initialsMaker)
        val postElements: MutableList<PostElement> = ArrayList()
        postElements.add(builder.buildHeader(post))
        addPostPicture(postElements, builder, post)

        postElements.addAll(builder.buildMainPostElement(post, postElements))

        postElements.add(builder.buildFooter(post))
        postElements.add(builder.buildExtraSpace())
        return postElements
    }
    private fun addPostPicture(postElements: MutableList<PostElement>, builder: MainBodyBuilder, post: PostRaw) {
        if (post.imageUrl != null && post.imageUrl != "") {
//            for (p in postElements) {
//                if (p is PictureElement) {
//                    val picturePost = p as PictureElement
//                    println("post url = " + post.imageUrl)
//                    println("picture post url = " + post.imageUrl)
//
//                    if (post.imageUrl == picturePost.url)
//                        return
//                }
//            }
            postElements.add(builder.buildHeaderPicture(post))
        }
    }

    private class CommentsBuilder {
        fun makeAuthorFromComment(comment: Comment): Author {
            val author = comment.author
            return Author(author.id, author.name, author.picLink, author.title)
        }

        fun makeCommentElementFromComment(comment: Comment, author: Author): CommentElement {
            val text = comment.bodytext
            val date = comment.createtime
            val id = comment.commentid
            val parentId = comment.commentParentid
            val deletable = comment.candelete
            val userLike = comment.currentuserlike
            val likes = comment.likecount
            return CommentElement(text, date, id, parentId, deletable, userLike, likes, author)
        }
    }

    private class MainBodyBuilder(val mInitialsMaker: InitialsMaker){
         fun buildHeader(post: PostRaw): PostElement {
            val options = post.options
            val authorId = post.author.id
            val authorName = post.author.name
            val authorInitials: String = mInitialsMaker.getInitials(authorName)
            val unformattedAvatarUrl = post.author.picLink
            val date = post.date
            val headingTitle = post.headingTitle
            val title = post.title
            val optionsMenuEnabled = options.optionsMenuEnabled
            val headingVisible = options.headingVisible
            val titleVisible = options.titleVisible
            return HeaderElement(authorId, authorName, authorInitials,
                    unformattedAvatarUrl, date, headingTitle, title, optionsMenuEnabled, headingVisible, titleVisible)
        }

        fun buildHeaderPicture(post: PostRaw): PostElement {
            return PictureElement(post.imageUrl, 960, 1280)
        }

        fun buildMainPostElement(post: PostRaw, postElements: MutableList<PostElement>): List<PostElement> {
            val htmlElement = getMainPostElement(post, postElements)
            return if (isBlocksGeneratedPost(htmlElement)) buildBlocks(post) else arrayListOf(htmlElement ?: HtmlElement(""))
        }

        private fun getMainPostElement(post: PostRaw, postElements: MutableList<PostElement>): PostElement?{
            var htmlElement: PostElement? = null
            when (POST_VIEW) {
                Constants.POST_VIEW_HYBRID -> {
                    buildHybridView(post, postElements)
                }
                Constants.POST_VIEW_SINGLE_WEB -> {
                    htmlElement = buildSingleHtmlElement(post)
                }
                Constants.POST_VIEW_NATIVE -> {
                    buildNativeView(post, postElements)
                }
            }
            return htmlElement
        }

        private fun buildHybridView(post: PostRaw, postElements: MutableList<PostElement>) {
            val elements = post.contentElements ?: return
            // TODO check if needed
            val contentElements = listOf(elements)
            for (element in contentElements) {
                if (element is ContentElementHtml) {
                    postElements.add(buildHtmlElement(element))
                } else if (element is ContentElementImage) {
                    postElements.add(buildImageElement(element))
                }
            }
        }

        private fun buildNativeView(post: PostRaw, postElements: MutableList<PostElement>) {
            val blocks = post.textBlocks
            for (textBlock in blocks) {
                if (textBlock.type == "img") {
                    postElements.add(buildImageElement(textBlock))
                } else if (textBlock.type == "text") {
                    postElements.add(buildTextElement(textBlock))
                }
            }
        }

        private fun isBlocksGeneratedPost(htmlElement: PostElement?): Boolean {
            return htmlElement == null || (htmlElement as SingleHtmlElement).html == null
        }

        private fun buildTextElement(textBlock: TextBlocks): PostElement {
            val text = textBlock.content
            return TextElement(text)
        }

        private fun buildImageElement(textBlock: TextBlocks): PostElement {
            val data = textBlock.content
            val url = getPhotoLink(data)
            val photoParameters = getPhotoSize(data)
            val height = photoParameters["height"] ?: error("")
            val width = photoParameters["width"] ?: error("")
            return PictureElement(url, height, width)
        }


        private fun buildImageElement(elementImage: ContentElementImage): PostElement {
            return PictureElement(elementImage.imageUrl, elementImage.height, elementImage.width)
        }

        private fun buildHtmlElement(elementHtml: ContentElementHtml): PostElement {
            return HtmlElement(elementHtml.html)
        }

        private fun buildSingleHtmlElement(post: PostRaw): PostElement? {
            if (post.json != null)
                return null
            return SingleHtmlElement(post.bodyHtml)
        }
        /**
         * If the page in html doesn't exist, this method makes elements
         * from blocks, that defined in JsonPage.class
         *
         * current types: Text, Image, Quote, Video, Header
         */
        private fun buildBlocks(post: PostRaw): List<PostElement> {
            return convertBlocksToElements(post.json.pageBlocks)
        }

        private fun convertBlocksToElements(blocksFromJson: List<JsonPage.Block>): List<PostElement> {
            val elementList: MutableList<PostElement> = ArrayList()
            for (block in blocksFromJson) {
                when (block.type) {
                    "Text" -> elementList.add(SingleHtmlElement(block.textBody))
                    "Image" -> elementList.add(PictureElement(block.imageUrl, block.height, block.width))
                    "Quote" -> elementList.add(QuoteElement(block))
                    "Header" -> elementList.add(TextElement(block.text, POST_SUB_HEADER))
                    "Video" -> elementList.add(VideoElement(block.streamId, block.photo))
                }
            }
            return elementList
        }

        fun buildFooter(post: PostRaw): PostElement {
            val options = post.options
            val likes = post.likes
            val currentUserLike = post.currentUserLike
            val comments = post.commentsCount
            val likesEnabled = options.likesEnabled
            val commentsEnabled = options.commentsEnabled
            return FooterElement(likes, currentUserLike, comments, likesEnabled, commentsEnabled)
        }

        fun buildExtraSpace(): PostElement {
            return SpaceElement()
        }
    }
}