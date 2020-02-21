package ru.alfabank.alfamir.favorites.presentation.favorite_post

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TRANSITION_REPLACE
import ru.alfabank.alfamir.Constants.Post.COMMENTS_FIRST
import ru.alfabank.alfamir.Constants.Post.FEED_ID
import ru.alfabank.alfamir.Constants.Post.FEED_TYPE
import ru.alfabank.alfamir.Constants.Post.FEED_URL
import ru.alfabank.alfamir.Constants.Post.POST_ID
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.favorites.SwipeController
import ru.alfabank.alfamir.favorites.presentation.favorite_post.contract.FavoritePostContract
import ru.alfabank.alfamir.favorites.presentation.favorite_post.view_holder.PostDecorator
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.FavoriteProfileFragment
import ru.alfabank.alfamir.post.presentation.post.PostActivity
import ru.alfabank.alfamir.search.presentation.SearchFragment
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils
import javax.inject.Inject

class FavoritePostFragment : BaseFragment(), FavoritePostContract.View, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var flSearch: FrameLayout
    private lateinit var llToggle: LinearLayout
    private lateinit var tvToggleProfile: TextView
    private lateinit var tvTogglePost: TextView
    private lateinit var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    @Inject
    lateinit var mAdapter: FavoritePostAdapter
    @Inject
    lateinit var mPresenter: FavoritePostContract.Presenter

    private val separatorColor = Color.argb(255, 228, 228, 228)
    private val toggleColorActive = Color.argb(255, 133, 133, 133)
    private val toggleColorInactive = Color.argb(255, 255, 255, 255)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.favorite_post_fragment, container, false)

        recycler = view.findViewById(R.id.recycler_container)
        flSearch = view.findViewById(R.id.favorite_toolbar_fl_search)
        llToggle = view.findViewById(R.id.favorite_post_fragment_toolbar_ll_toggle)
        tvToggleProfile = view.findViewById(R.id.favorite_post_fragment_toolbar_tv_profile)
        tvTogglePost = view.findViewById(R.id.favorite_post_fragment_toolbar_tv_post)
        swipeRefreshLayout = view.findViewById(R.id.swipe_container)

        tvToggleProfile.setOnClickListener { openProfileFragment() }
        tvTogglePost.setOnClickListener { openPostFragment() }
        flSearch.setOnClickListener { mPresenter.onSearchClicked() }
        llToggle.setBackgroundResource(R.drawable.toggle_blue_two_elements_right)
        tvToggleProfile.setTextColor(toggleColorActive)
        tvTogglePost.setTextColor(toggleColorInactive)
        swipeRefreshLayout.setOnRefreshListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)
    }

    override fun onRefresh() {
        mPresenter.takeView(this)
    }

    override fun showFavoriteList() {
        recycler.adapter = mAdapter
        val mLm = androidx.recyclerview.widget.LinearLayoutManager(context)
        recycler.layoutManager = mLm
        recycler.addItemDecoration(PostDecorator(separatorColor, 1))
        val swipeController = object : SwipeController(context!!) {
            override fun onSwiped(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, p: Int) {
                mAdapter.delete(holder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recycler)
        swipeRefreshLayout.isRefreshing = false
    }

    override fun openPost(feedId: String, postId: String, feedUrl: String, feedType: String) {
        val intent = Intent(context, PostActivity::class.java)
        intent.putExtra(FEED_ID, feedId)
        intent.putExtra(POST_ID, postId)
        intent.putExtra(FEED_URL, feedUrl)
        intent.putExtra(FEED_TYPE, feedType)
        intent.putExtra(COMMENTS_FIRST, false)
        startActivity(intent)
    }

    override fun openSearchUi() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.add(R.id.favorite_container, SearchFragment(), null)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun showLoadingProgress() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoadingProgress() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun openPostFragment() {
        ActivityUtils.addFragmentToActivity(fragmentManager!!, FavoritePostFragment(), R.id.activity_main_content_frame, FRAGMENT_TRANSITION_REPLACE, false)
    }

    private fun openProfileFragment() {

        childFragmentManager
                .beginTransaction()
                .add(R.id.favorite_container, FavoriteProfileFragment(), "")
                .addToBackStack(null)
                .commit()

    }
}
