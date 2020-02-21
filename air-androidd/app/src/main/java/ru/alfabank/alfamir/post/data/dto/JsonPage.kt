package ru.alfabank.alfamir.post.data.dto

import com.google.gson.annotations.SerializedName


/**
 * Described page coming on request with parameter new_news_list: 1
 *
 * No body returns, but a JSON array of blocks with types
 */
class JsonPage {
    var title = ""
    var background: Background = Background()
    @SerializedName("blocks")
    var pageBlocks: List<Block> = arrayListOf()

    class Background {
        var url: String = ""
        var width: Int = -1
        var height: Int = -1
        var isWide: Boolean = false
        @SerializedName("wide_offset")
        var wideOffset: Int = -1
    }

    class Block {
        var type: String = ""

        @SerializedName("body")
        var textBody: String = ""

        @SerializedName("url")
        var imageUrl: String = ""
        var width: Int = -1
        var height: Int = -1

        @SerializedName("text")
        var text = ""
        @SerializedName("name")
        var quoteName = ""
        @SerializedName("job")
        var quoteJob = ""
        @SerializedName("photo")
        var photo: String = ""
        @SerializedName("account")
        var quoteAuthorAccount: String = ""

        var streamId: String = ""

    }

}

