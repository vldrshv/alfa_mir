package ru.alfabank.alfamir.main.home_fragment.presentation.view_holder

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.TopNewsAdapterContract


class TopNewsVH(itemView: View, private val mListener: TopNewsVHClickListener) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), TopNewsAdapterContract.TopNewsRowView {

    internal var image: ImageView = itemView.findViewById(R.id.top_news_viewholder_image)
    internal var tvTitle: TextView = itemView.findViewById(R.id.top_news_viewholder_tv_title)
    private var flMain: FrameLayout = itemView.findViewById(R.id.top_news_viewholder_fl_main)

    init {
        flMain.setOnClickListener { mListener.onItemClicked(adapterPosition) }
    }

    override fun setTitle(title: String) {
        tvTitle.text = title
    }

    override fun setPicture(bitmap: Bitmap, isAnimated: Boolean) {

        image.setImageBitmap(bitmap)
        image.visibility = View.VISIBLE
        if (isAnimated) {
            image.alpha = 0f
            val mShortAnimationDuration = 200
            image.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration.toLong())
                    .setListener(null)
        }
    }

    interface TopNewsVHClickListener {
        fun onItemClicked(position: Int)
    }

}
