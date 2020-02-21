package ru.alfabank.alfamir.post.presentation.dto

import ru.alfabank.alfamir.Constants.Post.POST_VIDEO


// todo VLAD add video uri
class VideoElement : PostElement {
    private val VIEW_TYPE = POST_VIDEO

    override fun getViewType() = VIEW_TYPE

    private var streamId = ""
    private var photoUri = ""

    constructor(_streamId: String, _photoUri: String?) {
        streamId = _streamId
        photoUri = _photoUri ?: ""
    }

    fun getVideoId() = streamId
}