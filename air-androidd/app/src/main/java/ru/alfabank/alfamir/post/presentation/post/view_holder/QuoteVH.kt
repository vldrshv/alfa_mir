package ru.alfabank.alfamir.post.presentation.post.view_holder

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper


/**
 * Loading Quote to feed publication
 *
 * Listeners enable to view profile, when clicking on it
 */
class QuoteVH(itemView: View, listener: ViewHolderClickListener) : RecyclerView.ViewHolder(itemView), PostAdapterContract.QuoteRowView {
    @BindView(R.id.rc_quote_image_profile_icon)
    lateinit var profileImage: ImageView
    @BindView(R.id.rc_quote_tv_profile_initials)
    lateinit var profileInitials: TextView
    @BindView(R.id.rc_quote_item_tv_profile_name)
    lateinit var profileName: TextView
    @BindView(R.id.rc_quote_tv_profile_job_position)
    lateinit var profileJobPosition: TextView
    @BindView(R.id.rv_quote_tv_body)
    lateinit var quoteBody: TextView

    init {
        ButterKnife.bind(this, itemView)

        profileImage.setOnClickListener { listener.onQuoteProfileClicked(adapterPosition) }
        profileInitials.setOnClickListener { listener.onQuoteProfileClicked(adapterPosition) }
        profileName.setOnClickListener { listener.onQuoteProfileClicked(adapterPosition) }
        profileJobPosition.setOnClickListener { listener.onQuoteProfileClicked(adapterPosition) }
        quoteBody.setOnClickListener { listener.onQuoteProfileClicked(adapterPosition) }
    }

    override fun setAuthorAvatar(encodedImage: Bitmap?, isAnimated: Boolean) {
        val roundedImage = PictureClipper.makeItRound(encodedImage)
        profileImage.setImageDrawable(roundedImage)
        profileImage.visibility = View.VISIBLE
        if (isAnimated) {
            profileImage.alpha = 0f
            profileImage.animate()
                    .alpha(1f)
                    .setDuration(200L)
                    .setListener(null)
        }
    }

    override fun setAuthorPlaceholder(initials: String?) {
        val drawable: Drawable = profileImage.context.resources.getDrawable(R.drawable.background_profile_empty)//, itemView.context.theme)
        drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)),
                PorterDuff.Mode.SRC_IN)
        profileImage.setImageDrawable(drawable)
    }

    override fun setAuthorName(name: String?) {
        profileName.text = name
    }

    override fun setAuthorInitials(initials: String?) {
        setAuthorPlaceholder(initials)
        profileInitials.text = initials
    }

    override fun setAuthorJobPosition(jobPosition: String?) {
        profileJobPosition.text = jobPosition
    }

    override fun setQuoteBody(body: String?) {
        quoteBody.text = body
    }

    interface ViewHolderClickListener {
        fun onQuoteProfileClicked(position: Int)
    }
}