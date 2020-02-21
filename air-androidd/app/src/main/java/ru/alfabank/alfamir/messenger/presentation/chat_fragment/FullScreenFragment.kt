package ru.alfabank.alfamir.messenger.presentation.chat_fragment

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.CircularProgressDrawable.LARGE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.full_screen_image.view.*
import kotlinx.android.synthetic.main.full_screen_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants.Messenger.ATTACHMENTS_LIST
import ru.alfabank.alfamir.Constants.Messenger.SELECTED_ITEM
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.messenger.data.dto.Attachment
import javax.inject.Inject


class FullScreenFragment : DaggerFragment() {

    @Inject
    lateinit var getImage: GetImage

    private lateinit var image: Bitmap
    private lateinit var cpd: androidx.swiperefreshlayout.widget.CircularProgressDrawable
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.full_screen_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cpd = androidx.swiperefreshlayout.widget.CircularProgressDrawable(context!!)
        cpd.strokeWidth = 5f
        cpd.centerRadius = 30f
        cpd.setStyle(LARGE)
        cpd.setColorFilter(context!!.resources.getColor(R.color.progress_bar_color), PorterDuff.Mode.SRC_ATOP)

        backButton.setOnClickListener { activity?.onBackPressed() }
        val adapter = FullScreenAdapter()
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        androidx.recyclerview.widget.PagerSnapHelper().attachToRecyclerView(recycler)

        arguments?.apply {
            getParcelableArrayList<Attachment>(ATTACHMENTS_LIST)?.also { images ->
                val ids = arrayListOf<String>()
                for (attachment in images) {
                    ids.add(attachment.guid)
                }
                adapter.ids = ids
            }

            layoutManager.scrollToPosition(getInt(SELECTED_ITEM))
        }
    }

    inner class FullScreenAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<FullScreenVH>() {

        var ids: ArrayList<String> = arrayListOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullScreenVH {
            return FullScreenVH(LayoutInflater.from(parent.context).inflate(R.layout.full_screen_image, parent, false))
        }

        override fun getItemCount(): Int {
            return ids.size
        }

        override fun onBindViewHolder(holder: FullScreenVH, position: Int) {
            cpd.start()
            holder.image.setImageDrawable(cpd)

            CoroutineScope(Dispatchers.Main).launch {
                compositeDisposable
                        .add(getImage.bitmap(ids[position], true)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ image ->
                                    Glide.with(context!!)
                                            .load(image)
                                            .into(holder.image)
                                    cpd.stop()
                                }, Throwable::printStackTrace))
            }
        }
    }

    class FullScreenVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val image: ImageView = view.image
    }
}


