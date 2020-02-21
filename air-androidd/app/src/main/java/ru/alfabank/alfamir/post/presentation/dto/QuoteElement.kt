package ru.alfabank.alfamir.post.presentation.dto

import ru.alfabank.alfamir.Constants.Post.POST_QUOTE
import ru.alfabank.alfamir.post.data.dto.JsonPage

/**
 * Quote element defined in QuoteVH.class
 *
 * Represents Quote, given from back.
 *
 * Visualized in post_quote_viewholder.xml
 */
class QuoteElement (private val block: JsonPage.Block) : PostElement {

    private val VIEW_TYPE = POST_QUOTE
    override fun getViewType() = POST_QUOTE


    fun getAuthor() = block.quoteName

    fun getAuthorJobPosition() = block.quoteJob

    fun getAuthorImageUri() = block.photo

    fun getAuthorInitials(): String {
        val fio = getAuthor().split(" ")

        return if (fio.size == 3)
            "${fio[1][0]}${fio[2][0]}"
        else
            "${fio[0][0]}${fio[1][0]}"
    }

    fun getAuthorAccount() = block.quoteAuthorAccount

    fun getQuoteText() = block.text
}