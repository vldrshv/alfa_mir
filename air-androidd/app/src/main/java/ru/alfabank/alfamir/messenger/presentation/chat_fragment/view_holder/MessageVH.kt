package ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract
import ru.alfabank.alfamir.messenger.presentation.dto.StatusUpdate

open class MessageVH(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), ChatAdapterContract.Message {

    protected val layout: ConstraintLayout = view.findViewById(R.id.message_vh_layout)
    private val date: TextView = view.findViewById(R.id.message_vh_date)
    private val mStatus: ImageView = view.findViewById(R.id.message_vh_status)
    private val dateStatus: androidx.cardview.widget.CardView = view.findViewById(R.id.date_status_container)

    private var read = Color.parseColor("#3776E8")
    private var notRead = 0

    override fun setDate(date: String) {
        this.date.text = date
    }

    override fun setMessageBackgroundTint(color: Int) {
        layout.backgroundTintList = ColorStateList.valueOf(color)
    }

    override fun positionRight() {
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.RIGHT
        params.setMargins((72 * Constants.Initialization.DENSITY).toInt(), (6 * Constants.Initialization.DENSITY).toInt(), (12 * Constants.Initialization.DENSITY).toInt(), (6 * Constants.Initialization.DENSITY).toInt())
        layout.layoutParams = params
    }

    override fun positionLeft() {
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.LEFT
        params.setMargins((6 * Constants.Initialization.DENSITY).toInt(), (6 * Constants.Initialization.DENSITY).toInt(), (72 * Constants.Initialization.DENSITY).toInt(), (6 * Constants.Initialization.DENSITY).toInt())
        layout.layoutParams = params
    }

    override fun setStatus(status: Int) {
        when (status) {
            StatusUpdate.PENDING -> {
                mStatus.setImageResource(R.drawable.ic_msg_status_pending)
                mStatus.setColorFilter(notRead)
            }
            StatusUpdate.SENT -> {
                mStatus.setImageResource(R.drawable.ic_msg_status_sent)
                mStatus.setColorFilter(notRead)
            }
            StatusUpdate.DELIVERED -> {
                mStatus.setImageResource(R.drawable.ic_msg_status_delivered)
                mStatus.setColorFilter(notRead)
            }
            StatusUpdate.READ -> {
                mStatus.setImageResource(R.drawable.ic_msg_status_delivered)
                mStatus.setColorFilter(read)
            }
        }
    }

    override fun hideStatus() {
        mStatus.visibility = View.GONE
    }

    override fun showStatus() {
        mStatus.visibility = View.VISIBLE
    }

    @Suppress("LiftReturnOrAssignment")
    protected fun setDateStatusColor(isImage: Boolean) {
        if (isImage) {
            date.setTextColor(Color.parseColor("#ffffff"))
            notRead = Color.parseColor("#ffffff")
        } else {
            dateStatus.setBackgroundColor(Color.TRANSPARENT)
            date.setTextColor(Color.parseColor("#858585"))
            notRead = Color.parseColor("#333333")
        }
    }
}