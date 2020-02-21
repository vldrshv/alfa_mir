package ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.cardview.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract

class FileMessageVH(view: View, val listener: View.OnClickListener) : MessageVH(view), ChatAdapterContract.FileRow {

    private val icon: ImageView = view.findViewById(R.id.file_icon)
    private val fileName: TextView = view.findViewById(R.id.file_name)
    private val sizeType: TextView = view.findViewById(R.id.size_type)
    private val container: androidx.cardview.widget.CardView = view.findViewById(R.id.icon_container)

    override fun positionRight() {
        super.positionRight()
        layout.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun positionLeft() {
        super.positionLeft()
        layout.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun setParams(name: String, extension: String, position: Int, isDownloaded: Boolean) {
        fileName.text = name
        sizeType.text = extension
        setDateStatusColor(false)
        val drawable: Drawable = if(isDownloaded) view.resources.getDrawable(R.drawable.icn_file_24x24, null)
        else view.resources.getDrawable(R.drawable.icn_download_24x24, null)
        icon.setImageDrawable(drawable)

        container.setTag(R.id.TAG_POSITION, position)
        container.setOnClickListener(listener)
    }

    override fun setIcon(bitmap: Bitmap) {
        Glide.with(view).load(bitmap).centerCrop().into(icon)
    }
}