package ru.alfabank.alfamir.messenger.presentation.chat_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.chat_fragment.*
import kotlinx.android.synthetic.main.chat_text_input_view.*
import kotlinx.android.synthetic.main.chat_toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alfabank.alfamir.Constants.Companion.BUILD_TYPE_PROD
import ru.alfabank.alfamir.Constants.Companion.PROFILE_ID
import ru.alfabank.alfamir.Constants.Initialization.DENSITY
import ru.alfabank.alfamir.Constants.Messenger.CHOOSE_FILE_INTENT_CODE
import ru.alfabank.alfamir.Constants.Messenger.DIRECTION_DOWN
import ru.alfabank.alfamir.Constants.Messenger.DIRECTION_UP
import ru.alfabank.alfamir.Constants.Messenger.TAKE_PHOTO_INTENT_CODE
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.main.main_activity.MainActivity
import ru.alfabank.alfamir.main.main_activity.contract.MainActivityContract
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatContract
import ru.alfabank.alfamir.messenger.presentation.dto.ChooserFragment
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator
import javax.inject.Inject

class ChatFragment : BaseFragment(), ChatContract.View {

    @Inject
    lateinit var mPresenter: ChatContract.Presenter
    @Inject
    lateinit var mAdapter: ChatAdapter
    @Inject
    lateinit var mImageCropper: ImageCropper
    @Inject
    lateinit var mLog: LogWrapper

    private lateinit var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    private lateinit var rlMain: RelativeLayout
    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var llMain: LinearLayout
    private lateinit var edInput: EditText
    private lateinit var flSend: FrameLayout
    private lateinit var imageSend: ImageView
    private lateinit var flBack: FrameLayout
    private lateinit var flOptions: FrameLayout
    private lateinit var imageProfile: ImageView
    private lateinit var tvInitials: TextView
    private lateinit var tvName: TextView
    private lateinit var tvTime: TextView
    private lateinit var flAttach: FrameLayout
    private lateinit var mLm: androidx.recyclerview.widget.LinearLayoutManager

    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var lastVisibleItemPosition: Int = 0
    private val visibleThreshold = 4

    private val disabledColor = Color.argb(255, 153, 153, 153)
    private val enabledColor = Color.argb(255, 0, 122, 255)

    private var mParentView: MainActivityContract.View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mParentView = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = fragment_chat_swipe_refresh_view
        rlMain = fragment_chat_rl_main
        recycler = fragment_chat_recycler_view
        llMain = text_input_ll_main
        edInput = text_input_ed_input
        flSend = text_input_fl_send
        imageSend = text_input_image_send
        flBack = messenger_appbar_fl_back
        flOptions = messenger_appbar_fl_options
        imageProfile = messenger_appbar_image_profile
        tvInitials = messenger_appbar_tv_initials
        tvName = messenger_appbar_tv_name
        tvTime = messenger_appbar_tv_time
        flAttach = attachment_fl_send

        llMain.setOnClickListener { mPresenter.onTextInputClicked() }
        imageProfile.setOnClickListener { mPresenter.onCorrespondentProfileClicked() }
        tvName.setOnClickListener { mPresenter.onCorrespondentProfileClicked() }
        flOptions.setOnClickListener { mPresenter.test() }

        edInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (edInput.text.toString().isEmpty()) {
                    imageSend.setColorFilter(disabledColor)
                } else {
                    imageSend.setColorFilter(enabledColor)
                }
            }
        })

        flSend.setOnClickListener {
            val message = edInput.text.toString()
            if (!message.isEmpty()) {
                mPresenter.onSendMessageClicked(message)
            }
        }

        flBack.setOnClickListener {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(rlMain.windowToken, 0)
            activity?.onBackPressed()
        }

        flAttach.setOnClickListener {

            ChooserFragment().setPresenter(mPresenter).show(activity?.supportFragmentManager!!, "chooser_fragment")
//            startActivityForResult(getPickImageChooserIntent(), 200)
        }

        swipeRefreshLayout.isEnabled = false
        if (!BUILD_TYPE_PROD) {
            flOptions.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkIfInitialized()) return
        mPresenter.takeView(this)
        mParentView?.apply { hideBottomNavBar() }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dropView()
        mParentView?.apply { showBottomNavBar() }
    }

    override fun showMessages() {
        mAdapter.setHasStableIds(true)
        recycler.adapter = mAdapter
        mLm = androidx.recyclerview.widget.LinearLayoutManager(context)
        mLm.reverseLayout = true
        recycler.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = recycler.childCount
                totalItemCount = mLm.itemCount
                firstVisibleItem = mLm.findFirstVisibleItemPosition()
                lastVisibleItemPosition = mLm.findLastVisibleItemPosition()
                if (totalItemCount == 0) return
                if (lastVisibleItemPosition + visibleThreshold >= totalItemCount) mPresenter.onLoadMore(DIRECTION_UP)
                if (firstVisibleItem - visibleThreshold <= 0) mPresenter.onLoadMore(DIRECTION_DOWN)
            }
        })
        recycler.itemAnimator = null
        recycler.layoutManager = mLm
    }

    override fun scrollToPosition(position: Int) {
        recycler.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val test3 = (112 * DENSITY).toInt()
        mLm.scrollToPositionWithOffset(position, test3)
    }

    override fun clearTextInput() {
        edInput.setText("")
        imageSend.setColorFilter(disabledColor)
    }

    override fun openActivityProfileUi(id: String) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra(PROFILE_ID, id)
        context?.startActivity(intent)
    }

    override fun setAuthorAvatar(bitmap: Bitmap, isAnimated: Boolean) {

        CoroutineScope(Dispatchers.Main).launch {
            var cropped: Bitmap? = null
            withContext(Dispatchers.IO) {
                cropped = mImageCropper.getRoundedCornerBitmap(bitmap, (4 * DENSITY).toInt())
            }
            imageProfile.setImageBitmap(cropped)
            imageProfile.visibility = View.VISIBLE

            if (isAnimated) {
                imageProfile.alpha = 0f
                val mShortAnimationDuration = 200
                imageProfile.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration.toLong())
                        .setListener(null)
            }
        }
    }

    override fun setAuthorPlaceholder(initials: String) {
        val drawable = imageProfile.context.resources.getDrawable(R.drawable.background_profile_empty)
        drawable.setColorFilter(Color.parseColor("#" + BackGroundGenerator.getBackgroundAvatarColor(initials)),
                PorterDuff.Mode.SRC_IN)
        imageProfile.setImageDrawable(drawable)
    }

    override fun setAuthorName(name: String) {
        tvName.text = name
    }

    override fun setAuthorInitials(initials: String) {
        tvInitials.text = initials
    }

    override fun setLastSeen(time: String) {
        tvTime.text = time
    }

    override fun setLoadingState(isLoading: Boolean) {
        if (isLoading) {
            swipeRefreshLayout.isRefreshing = isLoading
        } else {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ swipeRefreshLayout.isRefreshing = isLoading }, 400)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PHOTO_INTENT_CODE || requestCode == CHOOSE_FILE_INTENT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mPresenter.onActivityResult(data, requestCode)
            }
        }
    }
}
