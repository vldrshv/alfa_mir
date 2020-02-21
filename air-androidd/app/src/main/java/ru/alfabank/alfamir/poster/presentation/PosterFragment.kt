package ru.alfabank.alfamir.poster.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.poster_fragment.*
import kotlinx.android.synthetic.main.poster_fragment_toolbar.*
import ru.alfabank.alfamir.Constants.Poster.POSTER_ID
import ru.alfabank.alfamir.Constants.Poster.POSTER_TITLE
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.poster.data.dto.Poster
import javax.inject.Inject

class PosterFragment : BaseFragment() {

    @Inject
    lateinit var presenter: PosterPresenter
    @Inject
    lateinit var sliDoAdapter: PosterAdapter
    @Inject
    lateinit var htmlAdapter: HtmlAdapter
    var posterID = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.poster_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.takeView(this)
        arguments?.apply {
            posterID = getInt(POSTER_ID)
            title.text = getString(POSTER_TITLE)
            getData()
        }

        toolbar.setOnClickListener { activity?.onBackPressed() }
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recycler.isVerticalScrollBarEnabled = true

        swipe_container.setOnRefreshListener {
            getData()
        }
    }

    fun setData(poster: Poster) {
        sliDoAdapter.setPresenter(presenter)
        recycler.adapter = sliDoAdapter
        sliDoAdapter.setData(poster)
        swipe_container.isRefreshing = false
    }

    fun setHtmlData(poster: Poster, drawables: HashMap<String, Drawable>) {
        htmlAdapter.setPresenter(presenter)
        recycler.adapter = htmlAdapter
        htmlAdapter.setData(poster, drawables)
        swipe_container.isRefreshing = false
    }

    fun setItem(position: Int, poster: Poster) {
        sliDoAdapter.setItem(position, poster)
    }

    private fun getData() {
        presenter.getPoster(posterID)
//        swipe_container.isRefreshing = true
    }

    fun onError(key: String) {
        val snackbar = Snackbar.make(swipe_container, R.string.something_went_wrong, Snackbar.LENGTH_LONG)
        snackbar
                .setAction(R.string.resend_question_positive) { presenter.sendQuestion(key) }
                .setActionTextColor(resources.getColor(R.color.retry_button))
                .show()
    }

    override fun onResume() {
        super.onResume()
        sliDoAdapter.onResume()
    }

    override fun onPause() {
        super.onPause()
        sliDoAdapter.onPause()
    }
}