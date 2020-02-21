package ru.alfabank.alfamir.calendar_event.presentation.view_holder

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.calendar_event.presentation.contract.CalendarEventAdapterContract

class CalendarEventCardVH(itemView: View, private val mListener: VHClickListener) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), CalendarEventAdapterContract.CalendarEventCardRowView {

    private var tvDescription: TextView = itemView.findViewById(R.id.calendar_event_card_viewholder_tv_description)
    internal var tvDate: TextView = itemView.findViewById(R.id.calendar_event_card_viewholder_tv_date)
    private var tvTime: TextView = itemView.findViewById(R.id.calendar_event_card_viewholder_tv_time)
    private var imageBackground: ImageView = itemView.findViewById(R.id.calendar_event_card_viewholder_image_background)

    init {
        imageBackground.setOnClickListener { mListener.onItemClicked(adapterPosition) }
    }

    override fun setTitle(title: String) {
        tvDescription.text = title
    }

    override fun setPicture(bitmap: Bitmap, isAnimated: Boolean) {

        imageBackground.setImageBitmap(bitmap)
        imageBackground.visibility = View.VISIBLE
        if (isAnimated) {
            imageBackground.alpha = 0f
            val mShortAnimationDuration = 200
            imageBackground.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration.toLong())
                    .setListener(null)
        }
    }

    override fun setDate(date: String) {
        tvDate.text = date
    }

    override fun setTime(time: String) {
        tvTime.text = time
    }

    interface VHClickListener {
        fun onItemClicked(index: Int)
    }
}
