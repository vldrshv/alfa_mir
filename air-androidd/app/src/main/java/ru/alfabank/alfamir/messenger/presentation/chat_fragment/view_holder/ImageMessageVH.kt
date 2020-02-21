package ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder

import android.graphics.Bitmap
import android.view.View
import kotlinx.android.synthetic.main.message_image_viewholder.view.*
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract

class ImageMessageVH(view: View, val listener: View.OnClickListener) : MessageVH(view), ChatAdapterContract.ImageRow {

    private val imageGroup = view.image_group

    override fun setImage(bitmaps: List<Bitmap>?, position: Int) {
        if (bitmaps == null) {
            imageGroup.visibility = View.GONE
            imageGroup.removeOnClickListener()
        } else {
            setDateStatusColor(true)
            imageGroup.visibility = View.VISIBLE
            imageGroup.setImages(bitmaps)
            imageGroup.setOnClickListener(listener, position)
        }
    }

}