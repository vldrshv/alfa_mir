package ru.alfabank.alfamir.people.presentation.view_holder

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleListContract
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator
import ru.alfabank.alfamir.utility.static_utilities.PictureClipper

/**
 * Created by U_M0WY5 on 16.04.2018.
 */

class ProfileViewHolder(private val mMainView: View, private val mListener: ProfileVhClickListener) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mMainView), PeopleListContract.ListRowView {

    internal var name: TextView = mMainView.findViewById(R.id.item_favorites_name)
    internal var title: TextView = mMainView.findViewById(R.id.item_favorites_position)
    internal var initials: TextView = mMainView.findViewById(R.id.item_favorites_tv_profile_initials)
    private var profileIcon: ImageView = mMainView.findViewById(R.id.item_favorites_image_profile_icon)

    init {
        ButterKnife.bind(this, mMainView)

        mMainView.setOnClickListener { mListener.onItemClicked(adapterPosition) }
    }

    override fun setName(name: String) {
        this.name.text = name
    }

    override fun setTitle(title: String?) {
        this.title.text = title
    }

    override fun setProfileImage(bitmap: Bitmap?, isAnimated: Boolean) {
        if (bitmap == null) {
            profileIcon.setImageDrawable(null)
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            var roundedImage: RoundedBitmapDrawable? = null
            withContext(Dispatchers.IO){
                 roundedImage = PictureClipper.makeItRound(bitmap)
            }

            profileIcon.setImageDrawable(roundedImage)
            profileIcon.visibility = View.VISIBLE

            if (isAnimated) {
                profileIcon.alpha = 0f
                val mShortAnimationDuration = 200
                profileIcon.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration.toLong())
                        .setListener(null)
            }
        }
    }

    override fun setProfilePlaceholder() {
        val drawable = mMainView.context.resources.getDrawable(R.drawable.ic_avatar_default)
        profileIcon.setImageDrawable(drawable)
    }

    override fun setInitials(initials: String) {
        this.initials.text = initials
        val drawable = mMainView.context.resources.getDrawable(R.drawable.background_profile_empty)
        drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)),
                PorterDuff.Mode.SRC_IN)
        profileIcon.setImageDrawable(drawable)
    }

    override fun clearInitials() {
        this.initials.text = ""
    }


    interface ProfileVhClickListener {
        fun onItemClicked(position: Int)
    }
}
