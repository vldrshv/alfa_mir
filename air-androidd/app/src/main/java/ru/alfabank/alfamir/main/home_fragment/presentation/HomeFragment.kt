package ru.alfabank.alfamir.main.home_fragment.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TRANSITION_ADD
import ru.alfabank.alfamir.Constants.Companion.PROFILE_ID
import ru.alfabank.alfamir.Constants.Feed.NEWS_TYPE_MY_ALFA_LIFE
import ru.alfabank.alfamir.Constants.Initialization.DENSITY
import ru.alfabank.alfamir.Constants.Post.POST_URL
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.calendar_event.presentation.CalendarEventAdapter
import ru.alfabank.alfamir.calendar_event.presentation.EventDecoration
import ru.alfabank.alfamir.main.home_fragment.presentation.contract.HomeFragmentContract
import ru.alfabank.alfamir.main.menu_fragment.presentation.MenuFragment
import ru.alfabank.alfamir.notification.presentation.NotificationFragment
import ru.alfabank.alfamir.post.presentation.post.PostActivity
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity
import ru.alfabank.alfamir.profile.presentation.profile.WarningOnExtraChargeDialogFragment
import ru.alfabank.alfamir.search.presentation.SearchFragment
import ru.alfabank.alfamir.ui.activities.ActivityCreatePost
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils
import javax.inject.Inject

class HomeFragment : BaseFragment(), HomeFragmentContract.View {

    private lateinit var flMenu: FrameLayout
    private lateinit var flNotifications: FrameLayout
    private lateinit var mImageNotification: ImageView
    private lateinit var flNewPost: FrameLayout
    private lateinit var imageAvatar: ImageView
    private lateinit var llSearch: LinearLayout
    private lateinit var flTempBackground: FrameLayout
    private lateinit var imageHumanHelp: ImageView
    private lateinit var imageHumanDesk: ImageView
    private lateinit var mRecycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var posterRecycler: androidx.recyclerview.widget.RecyclerView

    @Inject
    lateinit var mPresenter: HomeFragmentContract.Presenter
    @Inject
    lateinit var mImageCropper: ImageCropper
    @Inject
    lateinit var mAdapter: TopNewsAdapter
    @Inject
    lateinit var mCalendarEventAdapter: CalendarEventAdapter

    private val separatorColor = Color.argb(255, 250, 250, 250)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flMenu = view.findViewById(R.id.home_fragment_toolbar_menu)
        flNotifications = view.findViewById(R.id.home_fragment_toolbar_notifications)
        mImageNotification = view.findViewById(R.id.home_fragment_toolbar_image_notifications)
        flNewPost = view.findViewById(R.id.home_fragment_toolbar_new_post)
        imageAvatar = view.findViewById(R.id.home_fragment_toolbar_image_avatar)
        llSearch = view.findViewById(R.id.home_fragment_search)
        flTempBackground = view.findViewById(R.id.home_fragment_top_news_fl_temp_background)
        imageHumanHelp = view.findViewById(R.id.human_help_image_call)
        imageHumanDesk = view.findViewById(R.id.human_desk_image_call)
        mRecycler = view.findViewById(R.id.recycler_container)
        posterRecycler = view.findViewById(R.id.poster_horizontal_recycler)

        flMenu.setOnClickListener {
            childFragmentManager.beginTransaction()
                    .add(R.id.container, MenuFragment())
                    .setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right, R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                    .addToBackStack("menu")
                    .commit()
        }
        imageAvatar.setOnClickListener { mPresenter.onProfileClicked() }
        flNotifications.setOnClickListener { mPresenter.onNotificationClicked() }
        llSearch.setOnClickListener { mPresenter.onSearchClicked() }
        imageHumanHelp.setOnClickListener { mPresenter.onCallHumanHelpClicked() }
        imageHumanDesk.setOnClickListener { mPresenter.onCallHumanDeskClicked() }
        posterRecycler.addItemDecoration(EventDecoration(separatorColor, 12))

        flNewPost.setOnClickListener {
            val intent = Intent(context, ActivityCreatePost::class.java)
            val feedId = NEWS_TYPE_MY_ALFA_LIFE
            intent.putExtra("feedType", feedId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkIfInitialized()) return
        mPresenter.takeView(this)
        mCalendarEventAdapter.onHostViewResume(mPresenter)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dropView()
        mCalendarEventAdapter.onHostViewDestroy()
    }

    override fun setUserPic(bitmap: Bitmap, isCached: Boolean) {

        CoroutineScope(Dispatchers.Main).launch {
            var cropped: Bitmap? = null
            withContext(Dispatchers.IO) {
                cropped = mImageCropper.getRoundedCornerBitmap(bitmap, (360 * DENSITY).toInt())
            }
            imageAvatar.setImageBitmap(cropped)
            imageAvatar.visibility = View.VISIBLE
            if (!isCached) {
                imageAvatar.alpha = 0f
                val mShortAnimationDuration = 200
                imageAvatar.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration.toLong())
                        .setListener(null)
            }
        }
    }

    override fun openProfileUi(userId: String) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra(PROFILE_ID, userId)
        startActivity(intent)
    }

    override fun openNotificationUi() {
        ActivityUtils.addFragmentToActivity(fragmentManager!!,
                NotificationFragment(), R.id.activity_main_content_frame,
                FRAGMENT_TRANSITION_ADD, true)
    }

    override fun openSearchUi() {
        ActivityUtils.addFragmentToActivity(fragmentManager!!,
                SearchFragment(), R.id.activity_main_content_frame,
                FRAGMENT_TRANSITION_ADD, true)
    }

    override fun showTopNews() {
        mRecycler.adapter = mAdapter
        val mLm = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        mRecycler.layoutManager = mLm
        mRecycler.addItemDecoration(CirclePagerDecoration())
        val snapHelper = androidx.recyclerview.widget.PagerSnapHelper()
        mRecycler.onFlingListener = null
        snapHelper.attachToRecyclerView(mRecycler)
    }

    override fun showCalendarEventListCards() {
        mCalendarEventAdapter.setFragmentView(this)
        posterRecycler.adapter = mCalendarEventAdapter
        val mLm2 = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        posterRecycler.layoutManager = mLm2
    }

    override fun openPost(postUrl: String) {
        val intent = Intent(context, PostActivity::class.java)
        intent.putExtra(POST_URL, postUrl)
        startActivity(intent)
    }

    override fun hideTempBackground() {
        flTempBackground.visibility = View.GONE
    }

    override fun setSantaIcon() {
        mImageNotification.setImageDrawable(resources.getDrawable(R.drawable.icn_santa_hat_white_24x24))
    }

    /**
     * For some unknown reason the method calls onPause twice,
     * which prevents the normal save of the state in the presenter.
     * Which in turn causes extra phones to be hidden if the users
     * chooses to onCallClicked while they are shown.
     */
    @SuppressLint("MissingPermission")
    override fun makeCall(phone: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    override fun showWarningOnExtraCharge() {
        val dialogFragment = WarningOnExtraChargeDialogFragment.newInstance()
        val fragmentManager = fragmentManager
        dialogFragment.show(fragmentManager!!, "")
    }

    override fun checkCallPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context!!, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestCallPermission(phoneType: Int) {
        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CALL_PHONE), phoneType)
    }

    fun saveCallParameter(accept: Boolean) {
        mPresenter.saveCallParameter(accept)
    }

    override fun callHH() {
        mPresenter.onCallHumanHelpClicked()
    }

    override fun callHD() {
        mPresenter.onCallHumanDeskClicked()
    }

    override fun update(show: Boolean) {
        updateContainer.visibility = if (show) View.VISIBLE else View.GONE
        updateContainer.setOnClickListener { mPresenter.showHomo() }
    }
}
