package ru.alfabank.alfamir.favorites.presentation.favorite_profile

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileAdapterContract
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.contract.FavoriteProfileContract
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.view_holder.FavoriteProfileVH
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper
import ru.alfabank.alfamir.utility.logging.local.LogWrapper

import javax.inject.Inject

class FavoriteProfileAdapter @Inject
internal constructor(
        private val mPresenter: FavoriteProfileContract.Presenter,
        private val mImageCropper: ImageCropper,
        private val mLog: LogWrapper) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), FavoriteProfileAdapterContract.Adapter {

    private var mRecyclerView: androidx.recyclerview.widget.RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
        mPresenter.takeListAdapter(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return FavoriteProfileVH(LayoutInflater.from(viewGroup.context).inflate(R.layout.favorite_profile_viewholder, viewGroup, false), mPresenter, mImageCropper)
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, i: Int) {
        val favoriteProfileVH = viewHolder as FavoriteProfileVH
        mPresenter.bindListRowProfile(favoriteProfileVH, i)
    }

    override fun getItemCount(): Int {
        return mPresenter.listSize
    }

    override fun onItemInserted(position: Int) {

    }

    override fun onDataSetChanged() {
        notifyDataSetChanged()
    }

    override fun delete(position: Int) {
        mPresenter.delete(position)
        notifyItemRemoved(position)
    }
}