package ru.alfabank.alfamir.notification.presentation

import com.google.common.base.Strings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_0
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_2
import ru.alfabank.alfamir.Constants.DateFormat.TIME_ZONE_GREENWICH
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BIRTHDAY
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BLOG
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_BLOG_COMMENT
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_COMMUNITY
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_COMMUNITY_COMMENT
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_MEDIA
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_MEDIA_COMMENT
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_NEWS
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_NEWS_COMMENT
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_SECRET_SANTA
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_SURVEY
import ru.alfabank.alfamir.Constants.Notification.NOTIFICATION_TYPE_VACATION
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.notification.domain.usecase.GetNotificationList
import ru.alfabank.alfamir.notification.presentation.contract.NotificationAdapterContract
import ru.alfabank.alfamir.notification.presentation.contract.NotificationContract
import ru.alfabank.alfamir.notification.presentation.dto.Notification
import ru.alfabank.alfamir.notification.presentation.dummy_view.NotificationDummyView
import ru.alfabank.alfamir.utility.enums.FormatElement
import ru.alfabank.alfamir.utility.static_utilities.DateConverter
import ru.alfabank.alfamir.utility.static_utilities.NotificationsTextEditor
import javax.inject.Inject

class NotificationPresenter @Inject
internal constructor(private val mGetNotificationList: GetNotificationList, private val mGetImage: GetImage) : NotificationContract.Presenter {

    private var mView: NotificationContract.View? = null
    private var mAdapter: NotificationAdapterContract.Adapter? = null
    private val mCompositeDisposable = CompositeDisposable()
    private var mNotificationList: MutableList<Notification>? = null
    private var mIsLoading: Boolean = false
    private var mIsDateLoaded: Boolean = false

    override fun takeView(view: NotificationContract.View) {
        mView = view
        if (mIsDateLoaded) return
        getView().showLoading()
        loadNotificationList()
    }

    private fun loadNotificationList() {
        mCompositeDisposable.add(mGetNotificationList.execute(GetNotificationList.RequestValues(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mIsDateLoaded = true
                    mNotificationList = responseValue.notificationList
                    view.showNotifications()
                    view.hideLoading()
                }, { view.hideLoading() }))
    }

    override fun dropView() {
        mView = null
        mAdapter = null
        mCompositeDisposable.dispose()
    }

    override fun getView(): NotificationContract.View {
        return if (mView == null) NotificationDummyView() else mView!!
    }

    override fun bindListRowNotification(position: Int, rowView: NotificationAdapterContract.NotificationRowView) {
        val notification = mNotificationList!![position]
        val date = notification.date
        val imageUrl = notification.imageUrl
        val viewId = notification.viewId
        rowView.clearImage()
        rowView.hideSantaHat()

        // set textView getData
        when (val notificationType = notification.notificationType) {
            NOTIFICATION_TYPE_BIRTHDAY, NOTIFICATION_TYPE_VACATION, NOTIFICATION_TYPE_NEWS_COMMENT, NOTIFICATION_TYPE_BLOG_COMMENT, NOTIFICATION_TYPE_COMMUNITY_COMMENT, NOTIFICATION_TYPE_SECRET_SANTA -> if (notificationType == NOTIFICATION_TYPE_NEWS_COMMENT ||
                    notificationType == NOTIFICATION_TYPE_BLOG_COMMENT ||
                    notificationType == NOTIFICATION_TYPE_COMMUNITY_COMMENT) {
                rowView.setDate(DateConverter.formatDate(date, DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_GREENWICH,
                        FormatElement.NOTIFICATION) + " — " + notification.author.name)
                rowView.setText(NotificationsTextEditor.getCommentNotificationText(notification.displayInfo))
            } else if (notificationType == NOTIFICATION_TYPE_BIRTHDAY) {
                rowView.setText(NotificationsTextEditor.getBirthdayNotification(notification.displayInfo, notification.date))
                rowView.setDate(DateConverter.formatDate(date, DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_GREENWICH,
                        FormatElement.NOTIFICATION))
            } else if (notificationType == NOTIFICATION_TYPE_VACATION) {
                rowView.setText(NotificationsTextEditor.getVacationNotification(notification.displayInfo))
                rowView.setDate(DateConverter.formatDate(date, DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_GREENWICH,
                        FormatElement.NOTIFICATION))
            } else if (notificationType == NOTIFICATION_TYPE_SECRET_SANTA) {
                rowView.setText(NotificationsTextEditor.getSantaNotification(notification.displayInfo))
                rowView.setDate(DateConverter.formatDate(date, DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_GREENWICH,
                        FormatElement.NOTIFICATION))
                rowView.showSantaHat()
            }
            NOTIFICATION_TYPE_NEWS, NOTIFICATION_TYPE_BLOG, NOTIFICATION_TYPE_COMMUNITY, NOTIFICATION_TYPE_MEDIA -> {
                rowView.setText(notification.text)
                rowView.setDate(DateConverter.formatDate(date, DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_GREENWICH,
                        FormatElement.NOTIFICATION) + " — " + NotificationsTextEditor.getDepartment(notification.displayInfo))
            }
            NOTIFICATION_TYPE_SURVEY -> {
                rowView.setText(NotificationsTextEditor.getDepartment(notification.displayInfo))
                rowView.setDate(DateConverter.formatDate(date, DATE_PATTERN_0, DATE_PATTERN_2, TIME_ZONE_GREENWICH,
                        FormatElement.NOTIFICATION) + " — " + NotificationsTextEditor.getDepartment(notification.displayInfo))
            }
        }

        if (Strings.isNullOrEmpty(imageUrl)) { // todo what if user?
            rowView.hideImage()
            rowView.hideSantaHat()
            return
        }

        rowView.showImage()
        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(imageUrl, 80)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ rowView.setImage(it, false) }, Throwable::printStackTrace))
        }
    }

    override fun getItemId(position: Int): Long {
        val notification = mNotificationList!![position]
        return notification.viewId
    }

    override fun getListSize(): Int {
        return mNotificationList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun takeListAdapter(adapter: NotificationAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): NotificationAdapterContract.Adapter {
        return if (mAdapter == null) NotificationDummyView() else mAdapter!!
    }

    override fun onItemClicked(position: Int) {
        if (position == -1 || position > mNotificationList!!.size) {
            return
        }
        val notification = mNotificationList!![position]
        val openInfo = notification.openInfo
        when (notification.notificationType) {
            NOTIFICATION_TYPE_NEWS, NOTIFICATION_TYPE_BLOG, NOTIFICATION_TYPE_COMMUNITY, NOTIFICATION_TYPE_NEWS_COMMENT, NOTIFICATION_TYPE_BLOG_COMMENT, NOTIFICATION_TYPE_COMMUNITY_COMMENT, NOTIFICATION_TYPE_MEDIA, NOTIFICATION_TYPE_MEDIA_COMMENT -> {
                val postId = openInfo.postId
                val feedUrl = openInfo.feedUrl
                val feedType = openInfo.notificationType
                view.openPostUi(postId, feedUrl, feedType)
            }
            NOTIFICATION_TYPE_BIRTHDAY, NOTIFICATION_TYPE_SECRET_SANTA, NOTIFICATION_TYPE_VACATION -> {
                val userLogin = openInfo.userLogin
                view.openProfileUi(userLogin)
            }
            NOTIFICATION_TYPE_SURVEY -> {
                val quizId = openInfo.postId
                view.openSurveyUi(quizId)
            }
        }
    }

    override fun onLoadMore() {
        if (mIsLoading) return
        mIsLoading = true
        view.showLoading()
        val notification = mNotificationList!![mNotificationList!!.size - 1]
        val lastItemId = notification.id
        mCompositeDisposable.add(mGetNotificationList.execute(GetNotificationList.RequestValues(lastItemId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseValue ->
                    mNotificationList!!.addAll(responseValue.notificationList)
                    adapter.onDataSetChanged()
                    mIsLoading = false
                    view.hideLoading()
                }, {
                    mIsLoading = false
                    view.hideLoading()
                }))
    }
}
