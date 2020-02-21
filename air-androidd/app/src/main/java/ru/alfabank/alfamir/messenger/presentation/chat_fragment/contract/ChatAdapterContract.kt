package ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract

import android.graphics.Bitmap
import ru.alfabank.alfamir.base_elements.BaseAdapter
import ru.alfabank.alfamir.base_elements.BaseListPresenter
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder.MessageNewDecoratorVH
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder.MessageTimeBubbleVH

interface ChatAdapterContract {

    interface Presenter : BaseListPresenter<Adapter>, MessageTimeBubbleVH.ViewHolderClickListener, MessageNewDecoratorVH.ViewHolderClickListener {
        fun bindTextMessage(position: Int, view: TextRow)

        fun bindImageRow(position: Int, view: ImageRow)

        fun bindFileRow(position: Int, view: FileRow)

        fun bindTimeBubble(position: Int, rowView: MessageTimeBubbleRowView)

        fun onLoadMore(direction: Int)

        fun getItemId(position: Int): Long
    }

    interface Adapter : BaseAdapter {
        fun onItemInserted(position: Int)

        fun onDataSetChanged()
        fun notifyItemChanged(position: Int)
        fun notifyItemInserted(index: Int)
    }

    interface Message {

        fun setDate(date: String)

        fun setStatus(status: Int)

        fun hideStatus()

        fun showStatus()

        fun setMessageBackgroundTint(color: Int)

        fun positionRight()

        fun positionLeft()
    }

    interface TextRow : Message {
        fun setText(text: String)
    }

    interface ImageRow : Message {
        fun setImage(bitmaps: List<Bitmap>?, position: Int)
    }

    interface FileRow : Message {
        fun setParams(name: String, extension: String, position: Int, isDownloaded: Boolean)
        fun setIcon(bitmap: Bitmap)
    }

    interface MessageTimeBubbleRowView {
        fun setDate(date: String)
    }

    interface MessageNewDecoratorRowView

}
