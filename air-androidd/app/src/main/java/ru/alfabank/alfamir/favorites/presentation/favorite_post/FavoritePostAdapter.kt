package ru.alfabank.alfamir.favorites.presentation.favorite_post

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostAdapterContract
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostContract
import ru.alfabank.alfamir.favorites.presentation.favorite_post.view_holder.FavoritePostVH
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper
import javax.inject.Inject

class FavoritePostAdapter
@Inject constructor(
        internal val mPresenter: FavoritePostContract.Presenter,
        internal val mImageCropper: ImageCropper) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), FavoritePostAdapterContract.Adapter {

    override fun delete(position: Int) {
        mPresenter.delete(position)
        notifyItemRemoved(position)
    }

    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mPresenter.takeListAdapter(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return FavoritePostVH(LayoutInflater.from(viewGroup.context).inflate(R.layout.favorite_post_viewholder, viewGroup, false), mPresenter, mImageCropper)
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, i: Int) {
        val favoritePostVH = viewHolder as FavoritePostVH
        mPresenter.bindListRowPage(favoritePostVH, i)
    }

    override fun getItemCount(): Int {
        return mPresenter.listSize
    }

    override fun onItemInserted(position: Int) {

    }

    override fun onDataSetChanged() {
        notifyDataSetChanged()
    }
}
