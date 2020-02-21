package ru.alfabank.alfamir.messenger.presentation.chat_fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_FILE
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_IMAGE
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_TEXT
import ru.alfabank.alfamir.Constants.Messenger.VIEW_TYPE_TIME_BUBBLE
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatContract
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder.*
import javax.inject.Inject

class ChatAdapter @Inject constructor(private val mPresenter: ChatContract.Presenter) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), ChatAdapterContract.Adapter {

    private var mRecyclerView: androidx.recyclerview.widget.RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        mPresenter.takeListAdapter(this)
    }

    override fun getItemViewType(position: Int): Int {
        return mPresenter.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {

            MESSAGE_TEXT -> TextMessageVH(LayoutInflater.from(parent.context).inflate(R.layout.message_text_viewholder, parent, false))

            MESSAGE_IMAGE -> ImageMessageVH(LayoutInflater.from(parent.context).inflate(R.layout.message_image_viewholder, parent, false), mPresenter.photoListener)

            MESSAGE_FILE -> FileMessageVH(LayoutInflater.from(parent.context).inflate(R.layout.message_file_viewholder, parent, false), mPresenter.itemListener)

            VIEW_TYPE_TIME_BUBBLE -> MessageTimeBubbleVH(LayoutInflater.from(parent.context).inflate(R.layout.message_time_buble_viewholder, parent, false), mPresenter)

            else -> return MessageNewDecoratorVH(LayoutInflater.from(parent.context).inflate(R.layout.message_new_decorator_viewholder, parent, false), mPresenter)
        }
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is TextMessageVH -> mPresenter.bindTextMessage(position, holder)
            is ImageMessageVH -> mPresenter.bindImageRow(position, holder)
            is FileMessageVH -> mPresenter.bindFileRow(position, holder)
            is MessageTimeBubbleVH -> mPresenter.bindTimeBubble(position, holder)
        }
    }

    override fun getItemId(position: Int): Long {
        return mPresenter.getItemId(position)
    }

    override fun getItemCount(): Int {
        return mPresenter.listSize
    }

    override fun onItemInserted(position: Int) {
        notifyItemInserted(position)
    }

    override fun onDataSetChanged() {
        notifyDataSetChanged()
    }
}
