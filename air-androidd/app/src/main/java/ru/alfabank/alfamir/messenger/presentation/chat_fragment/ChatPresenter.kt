package ru.alfabank.alfamir.messenger.presentation.chat_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.filechooser.ChooserDialog
import com.google.common.base.Strings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import ru.alfabank.alfamir.Constants.Initialization.AUTHORITY
import ru.alfabank.alfamir.Constants.Initialization.USER_LOGIN
import ru.alfabank.alfamir.Constants.Messenger.ATTACHMENTS_LIST
import ru.alfabank.alfamir.Constants.Messenger.CHOOSE_FILE_INTENT_CODE
import ru.alfabank.alfamir.Constants.Messenger.DIRECTION_DOWN
import ru.alfabank.alfamir.Constants.Messenger.DIRECTION_UP
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_STATUS_READ
import ru.alfabank.alfamir.Constants.Messenger.MESSAGE_STATUS_UPDATING
import ru.alfabank.alfamir.Constants.Messenger.SELECTED_ITEM
import ru.alfabank.alfamir.Constants.Messenger.TAKE_PHOTO_INTENT_CODE
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.di.qualifiers.ID
import ru.alfabank.alfamir.di.qualifiers.messenger.ChatId
import ru.alfabank.alfamir.di.qualifiers.messenger.Type
import ru.alfabank.alfamir.image.domain.usecase.GetImage
import ru.alfabank.alfamir.messenger.data.dto.Attachment
import ru.alfabank.alfamir.messenger.domain.usecase.*
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract.Adapter
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract.TextRow
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatContract
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.dummy_view.ChatAdapterDummy
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.dummy_view.ChatViewDummy
import ru.alfabank.alfamir.messenger.presentation.dto.*
import ru.alfabank.alfamir.profile.domain.usecase.GetProfile
import ru.alfabank.alfamir.utility.logging.local.LogWrapper
import ru.alfabank.alfamir.utility.network.Downloader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@SuppressLint("LogNotTimber")
class ChatPresenter @Inject constructor(@ID private var mUserId: String?,
                                        @ChatId private var mChatId: String?,
                                        @Type private val type: String,
                                        private val mLog: LogWrapper,
                                        private val mGetMessages: GetMessages,
                                        private val mAddNewMessageDecorator: AddNewMessageDecorator,
                                        private val mAddTimeBubbleDecorator: AddTimeBubbleDecorator,
                                        private val mCreateNewMessage: CreateNewMessage,
                                        private val mGetProfile: GetProfile,
                                        private val mGetChat: GetChat,
                                        private val mGetImage: GetImage,
                                        private val mAddDisplayableItem: AddDisplayableItem,
                                        private val mSendMessageStatus: SendMessageStatus,
                                        private val mSendMessage: SendMessage,
                                        private val mObserveNewMessage: ObserveNewMessage,
                                        private val mObserveNewUserStatus: ObserveNewUserStatus,
                                        private val mObserveNewMessageStatus: ObserveNewMessageStatus,
                                        private val mUpdateMessage: UpdateMessage,
                                        private val downloader: Downloader,
                                        private val mUpdateDeliveredStatus: UpdateDeliveredStatus) : ChatContract.Presenter {

    private var mView: ChatContract.View? = null
    private var mAdapter: Adapter? = null
    private var mCorrespondentLogin: String? = null
    private var mFirstMessageId: Long = 0
    private var mFirstUnreadMessageId: Long = 0
    private var mLastMessageId: Long = 0
    private var mIsDataLoaded: Boolean = false
    private var mIsLoadingMore: Boolean = false
    private var mNewMessageViewId: Long = 0
    private val mCompositeDisposable = CompositeDisposable()
    private var mDisplayableMessageItems: List<DisplayableMessageItem> = ArrayList()
    private var userAttachments: MutableList<Attachment> = ArrayList()
    private val onPhotoClickListener = View.OnClickListener { v -> onPhotoClicked(v) }
    private val onItemClickListener = View.OnClickListener { v -> onItemClicked(v) }

    private val recipient = Color.argb(255, 255, 255, 255)
    private val sender = Color.argb(255, 238, 244, 253)
    private val imageExtensions = arrayListOf("png", "jpg", "jpeg", "bmp")


    private lateinit var currentPhotoPath: String
    private lateinit var fragment: ChatFragment
    private lateinit var context: Context

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    override fun takeView(view: ChatContract.View) {
        mView = view
        fragment = view as ChatFragment
        context = fragment.context!!

        if (mIsDataLoaded) return
        getView().setLoadingState(true)
        if (!Strings.isNullOrEmpty(mUserId)) loadProfile()
        loadChat()
    }

    override fun dropView() {
        job.cancel()
        mCompositeDisposable.clear()
        mView = null
        mAdapter = null
    }

    override fun getView(): ChatContract.View {
        return if (mView == null) ChatViewDummy() else mView!!
    }

    override fun getPhotoListener(): View.OnClickListener {
        return onPhotoClickListener
    }

    override fun getItemListener(): View.OnClickListener {
        return onItemClickListener
    }

    override fun onCorrespondentProfileClicked() {
        if (mCorrespondentLogin != null)
            view.openActivityProfileUi(mCorrespondentLogin)
    }

    override fun onTextInputClicked() {}

    override fun onSendMessageClicked(message: String) {
        view.clearTextInput()
        sendMessage(message)
    }

    override fun test() {
        sendMessage("tst")
    }

    private fun loadProfile() {
        mCompositeDisposable.add(
                mGetProfile.execute(mUserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ profile ->
                            profile.name?.also { name -> view.setAuthorName(name) }
                            mCorrespondentLogin = profile.login
                            val picUrl = profile.picUrl

                            ioScope.launch {
                                withContext(Dispatchers.IO) { mGetImage.bitmap(picUrl, 48) }
                                        .subscribe({ bitmap -> view.setAuthorAvatar(bitmap, true) },
                                                Throwable::printStackTrace)
                            }
                        }, Throwable::printStackTrace))
    }

    private fun loadChat() {
        val userIds = ArrayList<String>()
        if (!Strings.isNullOrEmpty(mUserId)) userIds.add(mUserId!!)
        mCompositeDisposable.add(
                mGetChat.execute(type, userIds, mChatId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { responseValue ->
                            if (Strings.isNullOrEmpty(mUserId)) {
                                mUserId = responseValue.respondentId
                                loadProfile()
                            }

                            val chat = responseValue.chat
                            val lastOnline = responseValue.correspondentOnlineStatus

                            mFirstMessageId = chat.firstMessageId
                            mFirstUnreadMessageId = chat.firstUnreadMessageId
                            mLastMessageId = chat.lastMessageId
                            mChatId = chat.id

                            if (lastOnline == 0) {
                                view.setLastSeen(responseValue.lastOnline)
                            } else if (lastOnline == 1) {
                                view.setLastSeen("online")
                            }
                            observeNewData()

                            chat.displayableMessageItemList
                        }
                        .map { sort(it) }
                        .map { mAddTimeBubbleDecorator.execute(it, mFirstMessageId) }
                        .flatMap { responseValue -> mAddNewMessageDecorator.execute(AddNewMessageDecorator.RequestValues(responseValue, mFirstUnreadMessageId)).subscribeOn(Schedulers.io()) }
                        .map { items ->
                            mDisplayableMessageItems = items.displayableMessageItems
                            loadAttachments()
                            items
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            val lastNewItemPosition = responseValue.lastNewItemPosition
                            val hasMessageNewDecorator = responseValue.isHasMessageNewDecorator
                            val lastMessageId = responseValue.lastMessageId
                            if (lastMessageId < mLastMessageId) onLoadMore(DIRECTION_DOWN)
                            if (hasMessageNewDecorator) {
                                view.showMessages()
                                view.scrollToPosition(lastNewItemPosition)
                                if (!mIsLoadingMore) view.setLoadingState(false)
                            } else {
                                view.showMessages()
                                if (!mIsLoadingMore) view.setLoadingState(false)
                            }
                            mIsDataLoaded = true
                        }, { throwable ->
                            view.setLoadingState(false)
                            view.setLoadingState(mIsLoadingMore)
                            Log.w("Chat", "Chat presenter loadChat Error: " + throwable.message)
                        }))
    }

    @Suppress("UNCHECKED_CAST")
    private fun sort(items: List<DisplayableMessageItem>): List<DisplayableMessageItem> {
        return (items as List<Message>).sortedByDescending { message -> message.id }
    }

    private fun loadAttachments() {

        for (i in mDisplayableMessageItems.indices) {

            if (mDisplayableMessageItems[i] is Message) {
                val message = mDisplayableMessageItems[i] as Message
                val attachments = message.attachments

                if (!attachments.isNullOrEmpty()) {
                    for (attachment in attachments) {

                        if (attachment.encodedValue.isNotEmpty() || attachment.file != null) {
                            continue
                        }
                        if (attachment.type == "image") {
                            ioScope.launch {
                                mCompositeDisposable.add(
                                        mGetImage.bitmap(attachment.guid, false)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({ bitmap ->
                                                    attachment.file = bitmap
                                                    mAdapter?.also { (it as ChatAdapter).notifyItemChanged(i) }
                                                }, Throwable::printStackTrace))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun sendMessage(messageText: String) {

        val message = mCreateNewMessage.createMessage(messageText, mChatId!!, userAttachments)
        val newMessageList = ArrayList<DisplayableMessageItem>()

        mNewMessageViewId = message.viewId
        newMessageList.add(message)
        mAddDisplayableItem.execute(newMessageList, mDisplayableMessageItems, DIRECTION_DOWN)
        mDisplayableMessageItems = mAddTimeBubbleDecorator.execute(mDisplayableMessageItems, mFirstMessageId)
        adapter.notifyItemInserted(mDisplayableMessageItems.size - 1)
        mCompositeDisposable.add(mSendMessage.sendMessage(mChatId, messageText, userAttachments, mNewMessageViewId, type)
                .subscribeOn(Schedulers.io())
                .flatMap { responseValue -> mUpdateMessage.execute(mDisplayableMessageItems, responseValue) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadAttachments()
                }, Throwable::printStackTrace))
        userAttachments = ArrayList()
    }

    private fun observeNewData() {
        observeNewMessage()
        observeNewMessageStatus()
        observeNewUserStatus()
    }

    private fun observeNewMessage() {
        mCompositeDisposable.add(
                mObserveNewMessage.execute(ObserveNewMessage.RequestValues())
                        .flatMap { responseValue -> responseValue.messageObservable }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ message ->
                            if (message.chatId == mChatId && message.id > mLastMessageId) {
                                addMessagesToList(message)
                                mLastMessageId = message.id
                            }
                        }, Throwable::printStackTrace))
    }

    private fun observeNewMessageStatus() {
        mCompositeDisposable.add(
                mObserveNewMessageStatus.execute(ObserveNewMessageStatus.RequestValues())
                        .flatMap { responseValue -> responseValue.statusObservable }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ status -> updateMessageStatus(status) }, Throwable::printStackTrace))
    }

    private fun observeNewUserStatus() {
        mCompositeDisposable.add(
                mObserveNewUserStatus.execute(ObserveNewUserStatus.RequestValues())
                        .flatMap { responseValue -> responseValue.userObservable }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ user -> updateUserStatus(user) }, Throwable::printStackTrace))
    }

    private fun addMessagesToList(nMessage: Message?) {
        if (nMessage == null) return
        val messages = ArrayList<DisplayableMessageItem>()
        messages.add(nMessage)
        mAddDisplayableItem.execute(messages, mDisplayableMessageItems, DIRECTION_DOWN)
        loadAttachments()
        for (i in messages.size downTo 0) {
            adapter.notifyItemInserted(mDisplayableMessageItems.size - 1 - i)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateUserStatus(nUser: User) {
        val users = ArrayList<User>()
        users.add(nUser)
        for (user in users) {
            if (user.id.toLowerCase() == mUserId) {
                val isOnline = user.onlineStatus
                if (isOnline == 0) {
                    view.setLastSeen("offline")
                } else if (isOnline == 1) {
                    view.setLastSeen("online")
                }
            }
        }
    }

    private fun updateMessageStatus(status: Status) {
        val statuses = ArrayList<Status>()
        statuses.add(status)
        if (statuses.isEmpty()) return
        mCompositeDisposable.add(
                mUpdateDeliveredStatus.execute(mDisplayableMessageItems, statuses)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ updatedPositions ->
                            updatedPositions.forEach { adapter.notifyItemChanged(it) }
                        }, Throwable::printStackTrace))
    }

    override fun getListSize(): Int {
        return mDisplayableMessageItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return mDisplayableMessageItems[position].type
    }

    override fun takeListAdapter(adapter: Adapter) {
        mAdapter = adapter
    }

    override fun getAdapter(): Adapter {
        return if (mAdapter == null) ChatAdapterDummy() else mAdapter as Adapter
    }

    @SuppressLint("CheckResult")
    override fun bindTextMessage(position: Int, view: TextRow) {
        val list = ArrayList(mDisplayableMessageItems)
        val item = list[position]
        val message = item as Message
        val userId = message.senderUser.id

        setPosition(userId, view, message.currentStatus, message, message.id)
        view.setText(message.text!!)
        view.setDate(message.date!!)
    }

    override fun bindImageRow(position: Int, view: ChatAdapterContract.ImageRow) {
        val message = mDisplayableMessageItems[position] as Message
        val attachments = message.attachments

        setPosition(message.senderUser.id, view, message.currentStatus, message, message.id)

        if (!attachments.isNullOrEmpty()) {
            val images = ArrayList<Bitmap>()
            for (attachment in attachments) {
                if (attachment.type == "image" && attachment.file != null) {
                    images.add(attachment.file as Bitmap)
                }
            }
            view.setImage(images, position)
        } else {
            view.setImage(null, -1)
        }
        view.setDate(message.date!!)
    }

    override fun bindFileRow(position: Int, view: ChatAdapterContract.FileRow) {
        val message = mDisplayableMessageItems[position] as Message
        val attachment = message.attachments[0]
        val isDownloaded = downloader.isDownloaded(attachment.name)

        setPosition(message.senderUser.id, view, message.currentStatus, message, message.id)
        view.setDate(message.date!!)
        view.setParams(attachment.name, attachment.extension, position, isDownloaded)
    }

    private fun <T : ChatAdapterContract.Message> setPosition(userId: String, view: T, status: Int, message: Message, id: Long) {

        if (userId.equals(USER_LOGIN, ignoreCase = true)) {
            view.positionRight()
            view.setMessageBackgroundTint(sender)
            view.showStatus()
            view.setStatus(status)
        } else {
            view.positionLeft()
            view.setMessageBackgroundTint(recipient)
            view.hideStatus()
            if (status != MESSAGE_STATUS_READ) {
                message.currentStatus = MESSAGE_STATUS_UPDATING
                val msgIds = ArrayList(mutableListOf(id))
                mCompositeDisposable.add(
                        mSendMessageStatus.execute(SendMessageStatus.RequestValues(msgIds, mChatId))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ message.currentStatus = MESSAGE_STATUS_READ },
                                        Throwable::printStackTrace))
            }
        }
    }

    override fun bindTimeBubble(position: Int, rowView: ChatAdapterContract.MessageTimeBubbleRowView) {
        val displayableMessageItem = mDisplayableMessageItems[position]
        val messageTimeBubble = displayableMessageItem as MessageTimeBubble
        val date = messageTimeBubble.date
        rowView.setDate(date)
    }

    override fun onLoadMore(direction: Int) {
        if (mIsLoadingMore) return
        mIsLoadingMore = true
        var messageId: Long = 0
        if (direction == DIRECTION_UP) {
            for (i in mDisplayableMessageItems.size - 1 downTo 1) {
                val item = mDisplayableMessageItems[i]
                if (item is Message) {
                    messageId = item.id
                    if (mFirstMessageId == messageId) {
                        mIsLoadingMore = false
                        view.setLoadingState(mIsLoadingMore)
                        return
                    }
                    break
                }
            }
        } else if (direction == DIRECTION_DOWN) {
            for (i in mDisplayableMessageItems.indices) {
                val item = mDisplayableMessageItems[i]
                if (item is Message) {
                    messageId = item.id
                    if (mLastMessageId <= messageId) {
                        mIsLoadingMore = false
                        view.setLoadingState(mIsLoadingMore)
                        return
                    }
                    break
                }
            }
        }
        if (messageId == 0L) {
            mIsLoadingMore = false
            view.setLoadingState(mIsLoadingMore)
            return
        }
        view.setLoadingState(mIsLoadingMore)

        mCompositeDisposable.add(
                mGetMessages.execute(type, mChatId!!, messageId, direction)
                        .subscribeOn(Schedulers.io())
                        .flatMap { responseValue ->
                            val displayableMessageItemList = ArrayList<DisplayableMessageItem>(responseValue)
                            Observable.just(mAddTimeBubbleDecorator.execute(displayableMessageItemList, mFirstMessageId))
                        }
                        .flatMap { responseValue -> Observable.just(mAddDisplayableItem.execute(responseValue, mDisplayableMessageItems, direction)) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ responseValue ->
                            loadAttachments()
                            val newMsgCount = responseValue!!
                            if (direction == DIRECTION_DOWN) {
                                for (i in 0 until newMsgCount) {
                                    adapter.onItemInserted(i)
                                }
                            } else {
                                val size = mDisplayableMessageItems.size - 1
                                for (i in size - newMsgCount until size) {
                                    adapter.onItemInserted(i)
                                }
                            }
                            mIsLoadingMore = false
                            view.setLoadingState(mIsLoadingMore)
                        }, {
                            mIsLoadingMore = false
                            view.setLoadingState(mIsLoadingMore)
                        }))
    }

    override fun getItemId(position: Int): Long {
        val displayableMessageItem = mDisplayableMessageItems[position]
        return displayableMessageItem.viewId
    }

    private fun onPhotoClicked(view: View) {
        val position = view.getTag(R.id.TAG_POSITION) as Int
        val item = view.getTag(R.id.TAG_ITEM) as Int
        val imageAttachments = ArrayList<Attachment>()
        for (attachment in (mDisplayableMessageItems[position] as Message).attachments) {
            if (attachment.type == "image") imageAttachments.add(attachment)
        }
        val fragment = FullScreenFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList(ATTACHMENTS_LIST, imageAttachments)
        bundle.putInt(SELECTED_ITEM, item)
        fragment.arguments = bundle
        (mView as ChatFragment).activity!!.supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_content_frame, fragment, fragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun onItemClicked(view: View) {
        val position = view.getTag(R.id.TAG_POSITION) as Int
        val attachment = (mDisplayableMessageItems[position] as Message).attachments[0]
        if (!downloader.isDownloaded(attachment.name)) {

            mCompositeDisposable.add(downloader.download(attachment.guid, attachment.name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ adapter.notifyItemChanged(position) }, {}))
        }
    }

    @Suppress("DeferredResultUnused")
    override fun onActivityResult(data: Intent?, requestCode: Int) {
        when (requestCode) {
            TAKE_PHOTO_INTENT_CODE -> {
                sendFile(currentPhotoPath)
            }
            CHOOSE_FILE_INTENT_CODE -> {
                if (data?.data != null) {
                    uiScope.launch {
                        val file = createImageFile()
                        fragment.activity?.contentResolver?.openInputStream(data.data!!)?.toFile(file)
                        sendFile(file.path)
                    }

                } else {
                    data?.clipData?.also { clipData ->
                        uiScope.launch {
                            val files = mutableMapOf<String, File>()
                            for (i in 0 until clipData.itemCount) {
                                val file = createImageFile()
                                fragment.activity?.contentResolver?.openInputStream(clipData.getItemAt(i).uri)?.toFile(file)
                                files[i.toString()] = file
                            }
                            processFiles(files)
                        }
                    }
                }
            }
        }
    }

    private fun sendFile(path: String) {
        uiScope.launch {
            val bitmap = BitmapFactory.decodeFile(path)
            makeAttachment(".jpg", convertBitmap(bitmap), bitmap, "image")
            sendMessage("")
        }
    }

    private fun convertBitmap(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

    fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            // Ensure that there's a camera activity to handle the intent
            intent.resolveActivity(fragment.activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.e("Chat", "error creating temp file: ${ex.message}")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(context, AUTHORITY, it)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(intent, TAKE_PHOTO_INTENT_CODE)
                }
            }
        }
    }

    fun choosePhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        fragment.startActivityForResult(intent, CHOOSE_FILE_INTENT_CODE)
    }

    fun chooseFile() {
        ChooserDialog(fragment.activity)
                .displayPath(true)
                .withChosenListener { pathFile -> processFiles(pathFile) }
                .build()
                .show()
    }

    @Suppress("DeferredResultUnused")
    private fun processFiles(files: Map<String, File>) {

        val list = arrayListOf<Deferred<Unit>>()
        uiScope.launch {
            for ((_, file) in files) {
                if (imageExtensions.contains(file.extension)) {
                    list.add(uiScope.async {
                        val bitmap = BitmapFactory.decodeFile(file.path)
                        makeAttachment("." + file.extension, convertBitmap(bitmap), bitmap, "image")
                    })
                } else {
                    val encoded = Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
                    makeAttachment("." + file.extension, encoded, file, "file")
                    sendMessage("")
                }
            }
            if (list.isNotEmpty()) {
                list.forEach { it.await() }
                sendMessage("")
            }
        }
    }

    private fun makeAttachment(extension: String, encodedString: String, file: Any, type: String) {
        val attachment = Attachment()
        if (file is File) {
            attachment.name = file.nameWithoutExtension
        }
        attachment.extension = extension
        attachment.encodedValue = encodedString
        attachment.type = type
        attachment.file = file
        userAttachments.add(attachment)
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun InputStream.toFile(file: File) {
        this.use { input ->
            file.outputStream().use {
                input.copyTo(it)
            }
        }
    }
}