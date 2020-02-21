package ru.alfabank.alfamir.main.main_activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import com.google.common.base.Strings
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.main_activity_new.*
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TRANSITION_ADD
import ru.alfabank.alfamir.Constants.Companion.INTENT_SOURCE
import ru.alfabank.alfamir.Constants.Companion.INTENT_SOURCE_PUSH
import ru.alfabank.alfamir.Constants.Companion.QUIZ_ID
import ru.alfabank.alfamir.Constants.Feed.NEWS_TYPE_COMMENT_NOTIFICATION
import ru.alfabank.alfamir.Constants.Feed.NEWS_TYPE_POST_NOTIFICATION
import ru.alfabank.alfamir.Constants.Initialization.MESSENGER_ENABLED
import ru.alfabank.alfamir.Constants.Messenger.CHAT_ID
import ru.alfabank.alfamir.Constants.Messenger.CHAT_TYPE
import ru.alfabank.alfamir.Constants.Messenger.USER_ID
import ru.alfabank.alfamir.Constants.Post.COMMENT_ID
import ru.alfabank.alfamir.Constants.Post.FEED_TYPE
import ru.alfabank.alfamir.Constants.Post.POST_ID
import ru.alfabank.alfamir.Constants.Post.POST_TYPE
import ru.alfabank.alfamir.Constants.Post.POST_URL
import ru.alfabank.alfamir.Constants.RequestCode.CALL_HD
import ru.alfabank.alfamir.Constants.RequestCode.CALL_HH
import ru.alfabank.alfamir.Constants.RequestCode.CAMERA
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseActivity
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.data.source.remote.api.WebService
import ru.alfabank.alfamir.favorites.presentation.favorite_post.FavoritePostFragment
import ru.alfabank.alfamir.main.home_fragment.presentation.HomeFragment
import ru.alfabank.alfamir.main.main_activity.contract.MainActivityContract
import ru.alfabank.alfamir.main.main_feed_fragment.MainFeedFragment
import ru.alfabank.alfamir.main.media_fragment.MediaFragment
import ru.alfabank.alfamir.messenger.presentation.chat_activity.MessengerActivity
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.ChatFragment
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.ChatListFragment
import ru.alfabank.alfamir.post.presentation.post.PostActivity
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity
import ru.alfabank.alfamir.profile.presentation.profile.WarningOnExtraChargeDialogFragment
import ru.alfabank.alfamir.survey.presentation.SurveyActivity
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper
import javax.inject.Inject

class MainActivity : BaseActivity(), MainActivityContract.View, ChatListFragment.OnChatSelectedListener, LoggerContract.Client.MainActivity, LoggerContract.Client.AppStartedTracker, WarningOnExtraChargeDialogFragment.WarningDialogListener {

    @Inject
    lateinit var mPresenter: MainActivityContract.Presenter

    @Inject
    lateinit var webService: WebService

    private val mCompositeDisposable = CompositeDisposable()
    private lateinit var mBottomNavigationView: BottomNavigationView
    private var mIsNotificationClicked: Boolean = false
    private var current: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_new)
        mBottomNavigationView = activity_main_nav_bar
        mBottomNavigationView.setOnNavigationItemSelectedListener {
            switchToFragment(it.itemId)
            true
        }
    }

    private fun switchToFragment(id: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        val container = R.id.activity_main_content_frame
        val fragment = when (id) {
            R.id.main_navigation_main -> {
                var fragment = supportFragmentManager.findFragmentByTag(MainFeedFragment::class.java.name)
                if (fragment == null) {
                    fragment = MainFeedFragment()
                    transaction.add(container, fragment, MainFeedFragment::class.java.name)
                }
                fragment
            }
            R.id.main_navigation_media -> {
                var fragment = supportFragmentManager.findFragmentByTag(MediaFragment::class.java.name)
                if (fragment == null) {
                    fragment = MediaFragment()
                    transaction.add(container, fragment, MediaFragment::class.java.name)
                }
                fragment

            }
            R.id.main_navigation_home -> {
                var fragment = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.name)
                if (fragment == null) {
                    fragment = HomeFragment()
                    transaction.add(container, fragment, HomeFragment::class.java.name)
                }
                fragment

            }
            R.id.main_navigation_messanger -> {
                var fragment = supportFragmentManager.findFragmentByTag(ChatListFragment::class.java.name)
                if (fragment == null) {
                    fragment = ChatListFragment()
                    transaction.add(container, fragment, ChatListFragment::class.java.name)
                }
                fragment

            }
            R.id.main_navigation_favorites -> {
                var fragment = supportFragmentManager.findFragmentByTag(FavoritePostFragment::class.java.name)
                if (fragment == null) {
                    fragment = FavoritePostFragment()
                    transaction.add(container, fragment, FavoritePostFragment::class.java.name)
                }
                fragment
            }
            else -> null
        }

        current = current ?: fragment as BaseFragment

        fragment?.also { frag ->
            if (current != frag) {
                transaction.show(frag)
                transaction.hide(current!!)
                current = frag as BaseFragment
            }
        }
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if (!checkIfInitialized()) return
        if (intent.extras != null) onNotificationClicked()
        mPresenter.takeView(this)
    }

    override fun onDestroy() {
        mPresenter.dropView()
        mCompositeDisposable.dispose()
        super.onDestroy()
    }

    override fun showMainFeed() {
        mBottomNavigationView.selectedItemId = R.id.main_navigation_main
    }

    override fun showHome() {
        if (!MESSENGER_ENABLED) {
            mBottomNavigationView.menu.findItem(R.id.main_navigation_messanger).isVisible = false
        }
        mBottomNavigationView.selectedItemId = R.id.main_navigation_home
    }

    override fun showBottomNavBar() {
        mBottomNavigationView.visibility = View.VISIBLE
    }

    override fun hideBottomNavBar() {
        mBottomNavigationView.visibility = View.GONE
    }

    override fun onChatSelected(userId: String, chatId: String, chatType: String) {
        intent.putExtra(USER_ID, userId)
        intent.putExtra(CHAT_ID, chatId)
        intent.putExtra(CHAT_TYPE, chatType)
        ActivityUtils.addFragmentToActivity(supportFragmentManager,
                ChatFragment(), R.id.activity_main_content_frame, FRAGMENT_TRANSITION_ADD, true)
    }

    private fun onNotificationClicked() {
        if (mIsNotificationClicked) return
        val bundle = intent.extras
        val type = bundle.getString("type")
        val data = bundle.getString("getData")
        if (Strings.isNullOrEmpty(type)) return
        mIsNotificationClicked = true
        if (type == "notification_profile") {
            val notificationProfile = JsonWrapper.getNotificationProfile(data) ?: return

            val profileId = notificationProfile.id
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id", profileId)
            startActivity(intent)
        } else if (type == "notification_comment") {
            val notificationComment = JsonWrapper.getNotificationComment(data) ?: return

            val postId = notificationComment.postId
            val postType = notificationComment.type
            val url = notificationComment.postUrl
            val commentId = notificationComment.commentId
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra(POST_ID, postId)
            intent.putExtra(POST_URL, url)
            intent.putExtra(FEED_TYPE, NEWS_TYPE_COMMENT_NOTIFICATION)
            intent.putExtra(COMMENT_ID, commentId)
            intent.putExtra(POST_TYPE, postType)
            startActivity(intent)
        } else if (type == "notification_post") {
            val notificationPost = JsonWrapper.getNotificationPost(data) ?: return

            val postId = notificationPost.id
            val postType = notificationPost.type
            val url = notificationPost.url
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra(POST_ID, postId)
            intent.putExtra(POST_URL, url)
            intent.putExtra(FEED_TYPE, NEWS_TYPE_POST_NOTIFICATION)
            intent.putExtra(POST_TYPE, postType)
            startActivity(intent)
        } else if (type == "notification_survey") {
            val notificationSurvey = JsonWrapper.getNotificationSurvey(data) ?: return
            val surveyId = notificationSurvey.id
            val intent = Intent(this, SurveyActivity::class.java)
            intent.putExtra(QUIZ_ID, surveyId)
            intent.putExtra(INTENT_SOURCE, INTENT_SOURCE_PUSH)
            startActivity(intent)
        } else if (type == "notification_messenger") {
            val chatMessage = JsonWrapper.getChatMessage(data)
            val chatId = chatMessage.chatId
            val intent = Intent(this, MessengerActivity::class.java)
            intent.putExtra(CHAT_ID, chatId)
            startActivity(intent)
        }
    }

    override fun logSendFeedback() {}

    override fun logOpenMenu() {}

    override fun logSearch(phrase: String) {}

    override fun logOpenFeed(id: String, name: String) {}

    override fun logOpenFavPeople(name: String) {}

    override fun logOpenAlfaTv() {}

    override fun logError(message: String, stackTrace: String) {}

    override fun logStartSession() {}

    override fun onWarningAccepted(isChecked: Boolean) {
        val fragment = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.name) as HomeFragment
        fragment.saveCallParameter(isChecked)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                CAMERA -> {
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        (supportFragmentManager.findFragmentByTag(ChatFragment::class.java.name) as ChatFragment)
                    }
                }
                CALL_HH -> (supportFragmentManager.findFragmentByTag(HomeFragment::class.java.name) as HomeFragment).callHH()

                CALL_HD -> (supportFragmentManager.findFragmentByTag(HomeFragment::class.java.name) as HomeFragment).callHD()
            }
        }
    }
}
