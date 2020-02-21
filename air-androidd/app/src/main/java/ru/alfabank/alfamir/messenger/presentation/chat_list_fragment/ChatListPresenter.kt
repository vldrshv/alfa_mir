package ru.alfabank.alfamir.messenger.presentation.chat_list_fragment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.messenger.domain.usecase.GetChatList
import ru.alfabank.alfamir.messenger.domain.usecase.ObserveNewMessage
import ru.alfabank.alfamir.messenger.domain.usecase.ObserveNewUserStatus
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListAdapterContract
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListContract
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.dummy_view.ChatListViewDummy
import ru.alfabank.alfamir.messenger.presentation.dto.ChatLight
import ru.alfabank.alfamir.messenger.presentation.dto.Message
import ru.alfabank.alfamir.messenger.presentation.dto.User
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile
import java.util.*
import javax.inject.Inject

class ChatListPresenter @Inject
internal constructor(private val mGetChatList: GetChatList,
                     private val mObserveNewUserStatus: ObserveNewUserStatus,
                     private val mGetImage: GetImage,
                     private val mGetProfile: GetProfile,
                     private val mObserveNewMessage: ObserveNewMessage) : ChatListContract.Presenter {
    private var mView: ChatListContract.View? = null
    private lateinit var mAdapter: ChatListAdapterContract.Adapter
    private val mCompositeDisposable = CompositeDisposable()
    private var mChatLightList: MutableList<ChatLight> = ArrayList()
    private var mIsDataLoaded: Boolean = false

    override fun takeView(view: ChatListContract.View) {
        mView = view
        getView()!!.setRefreshState(true)
        loadChats()
        observeNewData()
    }

    private fun loadChats() {
        mCompositeDisposable.add(
                mGetChatList.execute(GetChatList.RequestValues(false))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            mChatLightList = responseValue.chatLightList
                            if (mChatLightList.isEmpty()) {
                                view!!.showEmptyMessage()
                            } else {
                                view!!.showChatList()
                            }
                            mIsDataLoaded = true
                            view!!.setRefreshState(false)
                            view!!.setEnabledState(true)
                        }, {
                            view!!.setRefreshState(false)
                            view!!.setEnabledState(true)
                        }))
    }

    private fun observeNewUserStatus() {
        mCompositeDisposable.add(mObserveNewUserStatus
                .execute(ObserveNewUserStatus.RequestValues())
                .flatMap { it.userObservable }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.updateUserStatus(it) }, Throwable::printStackTrace))
    }

    private fun observeNewMessage() {
        mCompositeDisposable.add(mObserveNewMessage
                .execute(ObserveNewMessage.RequestValues())
                .flatMap<Message> { it.messageObservable }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.updateMessages(it) }, Throwable::printStackTrace))
    }

    private fun updateUserStatus(user: User) {
        for (i in mChatLightList.indices) {
            val chatLight = mChatLightList[i]
            if (chatLight.correspondentLogin == user.id) {
                chatLight.setCorrespondentOnlineStatus(user.onlineStatus)
            }
        }
        adapter.onDataSetChanged()
    }

    private fun updateMessages(message: Message) { // todo refactor
        val chatId = message.chatId
        val messageText = message.text
        val messageDate = message.date
        val authorId = message.senderUser.id
        mCompositeDisposable.add(mGetProfile.execute(authorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { responseValue ->
                    val authorName = responseValue.name
                    val picUrl = responseValue.picUrl
                    var isChatNew = true
                    for (i in mChatLightList.indices) {
                        val chatLight = mChatLightList[i]
                        if (chatLight.id == chatId) {
                            chatLight.lastMessage = messageText
                            chatLight.correspondentLogin = authorId
                            chatLight.lastMessageDate = messageDate
                            chatLight.title = authorName
                            chatLight.correspondentPicUrl = picUrl
                            chatLight.unreadCount = chatLight.unreadCount + 1
                            isChatNew = false
                        }
                    }
                    if (isChatNew) {
                        val newChatLight = ChatLight.Builder()
                                .id(chatId)
                                .lastMessage(messageText)
                                .lastMessageDate(messageDate)
                                .correspondentLogin(authorId)
                                .title(authorName)
                                .correspondentPicUrl(picUrl)
                                .unreadCount(1)
                                .build()
                        if (mChatLightList.isEmpty()) {
                            view!!.hideEmptyMessage()
                            view!!.showChatList()
                        }
                        mChatLightList.add(newChatLight)
                    }
                    adapter.onDataSetChanged()
                }
        )
    }

    private fun observeNewData() {
        observeNewMessage()
        observeNewUserStatus()
    }

    override fun dropView() {
        mView = null
        mCompositeDisposable.dispose()
    }

    override fun getView(): ChatListContract.View? {
        return mView
    }

    /**
     * [ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.contract.ChatListAdapterContract.Presenter]  methods
     */

    override fun bindListRowChat(position: Int, rowView: ChatListAdapterContract.ChatRowView) {
        val chatLight = mChatLightList[position] ?: return
        val picUrl = chatLight.correspondentPicUrl
        val title = chatLight.title
        val lastMessageTime = chatLight.lastMessageDate
        val lastMessage = chatLight.lastMessage
        val unreadMessageCount = chatLight.unreadCount
        val isOnline = chatLight.СorrespondentOnlineStatus
        rowView.setChatTitle(title)
        rowView.setLastMessageTime(lastMessageTime)
        rowView.setLastMessage(lastMessage)
        if (isOnline == 0) {
            rowView.setOnlineStatus(false)
        } else if (isOnline == 1) {
            rowView.setOnlineStatus(true)
        }
        if (unreadMessageCount == 0) {
            rowView.hideNewMessageBadge()
        } else {
            rowView.setNewMessageCount("" + unreadMessageCount)
            rowView.showNewMessageBadge()
        }

        CoroutineScope(Dispatchers.Main).launch {
            mCompositeDisposable.add(mGetImage.bitmap(picUrl, 48)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ rowView.setChatPic(it, true) }, Throwable::printStackTrace))
        }
    }

    override fun getItemId(position: Int): Long {
        val chatLight = mChatLightList[position]
        return chatLight.viewId
    }

    override fun getListSize(): Int {
        return mChatLightList.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun takeListAdapter(adapter: ChatListAdapterContract.Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): ChatListAdapterContract.Adapter {
        return if (mAdapter == null) ChatListViewDummy() else mAdapter
    }

    override fun onChatClicked(position: Int) {
        val chatLight = mChatLightList[position]

        val userId = chatLight.correspondentLogin
        val chatId = chatLight.id
        view!!.showChatUi(userId.toLowerCase(), chatId, chatLight.type)
    }

    override fun onRefresh() {
        mCompositeDisposable.add(
                mGetChatList.execute(GetChatList.RequestValues(true))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            if (mIsDataLoaded) {
                                mChatLightList = responseValue.chatLightList
                                adapter.onDataSetChanged()
                                view!!.setRefreshState(false)
                                view!!.setEnabledState(true)
                            } else {
                                mChatLightList = responseValue.chatLightList
                                if (mChatLightList.isEmpty()) {
                                    view!!.showEmptyMessage()
                                } else {
                                    view!!.showChatList()
                                }
                                mIsDataLoaded = true
                                view!!.setRefreshState(false)
                                view!!.setEnabledState(true)
                            }

                        }, {
                            view!!.setRefreshState(false)
                            view!!.setEnabledState(true)
                        }))
    }
}
