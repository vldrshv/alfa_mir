package ru.alfabank.alfamir.utility.static_utilities

import com.google.common.base.Strings
import com.google.gson.Gson
import ru.alfabank.alfamir.Constants.Companion.BUILD_TYPE_PROD
import ru.alfabank.alfamir.Constants.Companion.FEED_UPLOAD_AMOUNT
import ru.alfabank.alfamir.Constants.Companion.NOTIFICATION_UPLOAD_AMOUNT
import ru.alfabank.alfamir.Constants.Companion.PLATFORM
import ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_8
import ru.alfabank.alfamir.Constants.Initialization.ANDROID_RICH_TEXT
import ru.alfabank.alfamir.Constants.Initialization.APP_VERSION
import ru.alfabank.alfamir.Constants.Initialization.CITY
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_DP
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_PHYSICAL
import ru.alfabank.alfamir.Constants.Initialization.UNIQUE_APP_ID
import ru.alfabank.alfamir.Constants.Initialization.USER_LOGIN
import ru.alfabank.alfamir.Constants.Messenger.DIRECTION_UP
import ru.alfabank.alfamir.data.dto.notifications_settings.NotificationsSettings
import ru.alfabank.alfamir.messenger.data.source.remote.Parcel
import ru.alfabank.alfamir.survey.data.dto.Answer
import ru.alfabank.alfamir.utility.logging.remote.events.Event
import java.util.*

object RequestFactory {

    val settings = "{'wcf':'service_functions','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'getsettings'}}"
    lateinit var publicKey: String

    fun sendMessage(chatId: String, text: String, attachments: List<String>?, type: String): String {
        val attachmentsStr = attachments?.toString() ?: ""
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'sendmessage','type':'$type','guid':'$chatId','value':'$text','attachmentguids':$attachmentsStr,codekey:'',codevector:''}}"
    }

    fun sendMessage(parcel: Parcel, type: String): String {
        parcel.wcf = "messenger"
        parcel.request!!.action = "sendmessage"
        parcel.platform = PLATFORM
        parcel.ariVersion = APP_VERSION
        parcel.userLogin = USER_LOGIN
        val r = parcel.request!!
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'sendmessage','type':'$type','guid':'${r.guid}','value':'${r.value}','attachmentguids':${r.attachmentguids},'codekey':'${r.codekey}','codevector':'${r.codevector}',publickey:'$publicKey'}}"
    }

    fun formSendMessageReadStatus(msgIds: List<Long>, chatId: String): String {
        val ids = Gson().toJson(msgIds)
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'sendreadstatus', receivedmessageids:$ids, guid:'$chatId'}}"
    }

    fun formLoadChatList(): String {
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'loadchatslist',publickey:'$publicKey'}}"
    }

    fun formLongPollRequest(pollDataId: List<String>): String {
        val groupedIds = Gson().toJson(pollDataId)
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'newdata',receivedguids:$groupedIds}}"
    }

    fun formLoadMoreChatMessages(type: String, chatId: String, messageId: Long, amount: Int, direction: Int): String {
        val sDirection = if (direction == DIRECTION_UP) "upward" else "downward"

        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'chatwith','type':'$type''guid':'$chatId','lastmessageid':'$messageId','messagecount':'$amount','direction':'$sDirection',publickey:'$publicKey'}}"
    }

    fun formCreateChat(userIds: List<String>, chatId: String, amount: Int, type: String): String {
        var groupIds = ""
        if (chatId.isEmpty() && userIds.isNotEmpty()) {
            for (i in userIds.indices) {
                val id = userIds[i]
                groupIds = if (i == 0) { id } else { "$groupIds;#$id" }
            }
        }
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'chatwith','type':'$type','users':'$groupIds','guid':'$chatId','messagecount':$amount,publickey:'$publicKey'}}"
    }

    fun connection(publicKey: String): String {
        this.publicKey = publicKey
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'connection',publickey:'$publicKey'}}"
    }

    fun formReportOfflineRequest(): String {
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'disconnection', publickey:''}}"
    }

    fun getImage(guid: String, isHD: Boolean): String {
        return if (isHD)
            getFile(guid)
        else
            "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION',request:{action:'getimage',guid:'$guid','width':'400','height':'400',publickey:'$publicKey'}}"
    }

    fun getFile(guid: String): String {
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION',request:{action:'getfile',guid:'$guid',publickey:'$publicKey'}}"
    }

    fun saveFile(name: String, file: String, key: String, vector: String): String {
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'savefile',name:'$name','baseimage':'$file','codekey':'$key','codevector':'$vector'}}"
    }

    //  AlfaTV

    fun formGetTokenRequest(videoId: String, phoneIp: String, password: String): String {
        return "{type:'alfatv',parameters:{obj:'streamtoken',postid:'$videoId',message:'$phoneIp;#$password'}}"
    }

    @JvmStatic
    fun formGetAlfaTvShowsRequest(): String {
        return "{type:'alfatv',parameters:{obj:'tvprogram'}}"
    }

    @JvmStatic
    fun formGetShowCurrentStateRequest(videoId: Int): String {
        return "{type:'alfatv',parameters:{obj:'isstreaming', id:'$videoId'}}"
    }

    //Post

    fun formGetCommentsRequest(postType: String, threadId: String, postUrl: String): String {
        return "{'type':'comments','parameters':{'obj':'$postType','threadid':'$threadId','posturl':'$postUrl', authorsimageloadmode:1}}"
    }

    fun formGetNotifications(lastId: Int): String {
        return "{type:'notifications',parameters:{obj:'',postid:'$lastId',newscount:'15'}}"
    }

    fun formGetNotificationsRequest(lastId: Long): String {
        return if (!BUILD_TYPE_PROD) {
            "{'wcf':'favourites','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'getnotifications', 'lastid':'$lastId', 'itemcount':$NOTIFICATION_UPLOAD_AMOUNT}}"
        } else {
            "{type:'notifications',parameters:{obj:'',postid:'$lastId',newscount:'$NOTIFICATION_UPLOAD_AMOUNT'}}"
        }
    }

    fun formMarkNotificationAsRead(messageId: Int): String {
        return "{type:'notifications',parameters:{obj:'setNotyViewed',message:'$messageId'}}"
    }

    fun subscribeOnPost(postId: String, postUrl: String, threadId: String, type: String): String {
        return "{type:'notifications',parameters:{obj:'subscribePost', postid:'$postId', posturl:'$postUrl', threadid:'$threadId',type:'$type'}}"
    }

    fun subscribeOnFeed(postUrl: String, type: String): String {
        return "{type:'notifications',parameters:{obj:'subscribeFeed', postid:'', posturl:'$postUrl', threadid:'',type:'$type'}}"
    }

    fun formPostCommentRequestNew(postId: String, postType: String, postUrl: String, threadId: String, parentId: String?, message: String): String {
        val isReply: Int = if (Strings.isNullOrEmpty(parentId)) 0 else 1

        return "{type:'post',parameters:{obj:'${postType}comment', posturl:'$postUrl', postid:'$postId', message:'$message', threadid:'$threadId', isreply:'$isReply',replyid:'$parentId'}}"
    }

    fun formDeleteCommentRequest(commentId: String, commentType: String, postUrl: String): String {
        return "{type:'delete', parameters:{obj: '$commentType',, postid:'$commentId', posturl:'$postUrl'}}"
    }

    fun formLikeCommentRequest(commentId: String, postUrl: String, commentType: String, likeStatus: Int): String {
        return "{'type':'like','parameters':{'obj':'$commentType','likeval':'$likeStatus','postid':'$commentId','posturl':'$postUrl'}}"
    }

    fun formLikePostRequest(postId: String, feedUrl: String, feedType: String, newUserLikeStatus: Int): String {
        return "{'type':'like','parameters':{'obj':'$feedType','likeval':'$newUserLikeStatus','postid':'$postId','posturl':'$feedUrl'}}"
    }

    fun formPostRequestNew(postId: String, feedUrl: String, feedType: String, contentType: String): String {
        return "{'type':'open','parameters':{'obj':'$feedType', postid:'$postId', posturl:'$feedUrl', content_type:'$contentType', screen_width_px:'$SCREEN_WIDTH_PHYSICAL', screen_width_dp:'$SCREEN_WIDTH_DP', release_candidate:0, 'new_news_list':1}}"
    }

    fun formPostRequest(postUrl: String): String {
        return "{'type':'news','parameters':{'obj':'getfromurl','pageurl':'$postUrl', pubtime:'', newscount:'1',imageloadmode:'1', authorsimageloadmode:'1', content_type:'edited_html', screen_width_px:'$SCREEN_WIDTH_PHYSICAL', screen_width_dp:'$SCREEN_WIDTH_DP'}}"
    }

    fun formMainRequest(feedId: String, timeStamp: String, newsCount: Int): String {
        return "{'type':'news','parameters':{'obj':'$feedId', 'pubtime':'$timeStamp', newscount:'$newsCount', imageloadmode:'1', authorsimageloadmode:'1', androidrichtext:'$ANDROID_RICH_TEXT', city:'$CITY'}}"
    }

    @JvmStatic
    fun formMainRequestNew(feedId: String, timeStamp: String, newsCount: Int, contentType: String): String {
        return "{'type':'news','parameters':{'obj':'$feedId', 'pubtime':'$timeStamp', newscount:'$newsCount', content_type:'$contentType', screen_width_px:'$SCREEN_WIDTH_PHYSICAL', screen_width_dp:'$SCREEN_WIDTH_DP', city:'$CITY', release_candidate:0, 'new_news_list':1}}" // added new news list
    }

    fun unsubscribeFromPost(postId: String, postUrl: String, threadId: String, type: String): String {
        return "{type:'notifications',parameters:{obj:'unsubscribePost', postid:'$postId', posturl:'$postUrl', threadid:'$threadId',type:'$type'}}"
    }

    fun unsubscribeFromFeed(postUrl: String, type: String): String {
        return "{type:'notifications',parameters:{obj:'unsubscribeFeed', postid:'', posturl:'$postUrl', threadid:'',type:'$type'}}"
    }

    fun formGetActiveSubscriptions(): String {
        return "{type:'notifications',parameters:{obj:'getUserSubscribes',type:'android'}}"
    }

    fun formCreatePostRequest(destination: String, title: String, messagebody: String, urls: List<String>): String {
        var destination = destination
        val sBuilder = StringBuilder()
        for (i in 1 until urls.size) {
            if (urls[i] != null) {
                if (i == 1) {
                    sBuilder.append("{fileurl:'" + urls[i] + "'}")
                } else {
                    sBuilder.append(", {fileurl:'" + urls[i] + "'}")
                }
            }
        }
        destination = "http://alfa/Info/Stories"
        return "{type:'post', parameters:{obj: 'blog', destination:'$destination', messagetitle: '$title', messagebody:'$messagebody', blogimages: [$sBuilder] }}"
    }

    fun formCreatePostInCommunityRequest(destination: String, category: Int, title: String, messagebody: String, urls: List<String>): String {
        val sBuilder = StringBuilder()
        for (i in 1 until urls.size) {
            if (urls[i] != null) {
                if (i == 1) {
                    sBuilder.append("{fileurl:'" + urls[i] + "'}")
                } else {
                    sBuilder.append(", {fileurl:'" + urls[i] + "'}")
                }
            }
        }
        return "{type:'post', parameters:{obj: 'community', category: '$category', destination:'$destination', messagetitle: '$title', messagebody:'$messagebody', images: [$sBuilder] }}"
    }

    fun formPostMediaRequest(destination: String?, title: String?, messageBody: String?, url: String?): String {
        return "{type:'post', parameters:{obj: 'blogpost', destination:'$destination', messagetitle: '$title', messagebody:'$messageBody', blogimages: [{fileurl: '$url'}] }}"
    }

    @JvmStatic
    fun formFeedRequest(feedId: String, feedType: String, timeStamp: String?, newsCount: Int, loadHeader: Boolean, contentType: String): String {
        val requestHeader: Int = if (loadHeader) 1 else 0

        return "{'type':'news','parameters':{'obj':'$feedId', type:'$feedType', 'pubtime':'$timeStamp', newscount:'$newsCount', content_type:'$contentType', screen_width_px:'$SCREEN_WIDTH_PHYSICAL', screen_width_dp:'$SCREEN_WIDTH_DP', city:'$CITY', release_candidate:0, header_request:'$requestHeader'}}"
    }

    fun formFeedRequestNew(feedId: String, feedType: String, isHeaderRequired: Int, timeStamp: String): String {
        return "{'type':'news','parameters':{'obj':'$feedId', type:'$feedType', 'pubtime':'$timeStamp', newscount:'$FEED_UPLOAD_AMOUNT', content_type:'edited_html', screen_width_px:'$SCREEN_WIDTH_PHYSICAL', screen_width_dp:'$SCREEN_WIDTH_DP', city:'$CITY', release_candidate:0, header_request:'$isHeaderRequired'}}"
    }

    fun formGetNotificationsSettings(): String {
        return  "{type:'notifications',parameters:{obj:'getEnabledEvents'}}"
    }

    fun formSaveNotificationsSettings(savedSettings: NotificationsSettings): String {
        val birthday = savedSettings.isBirthdaysActivated
        val vacation = savedSettings.isVacationsActivated
        val posts = savedSettings.isPostsActivated
        val comments = savedSettings.isCommentsActivated
        val settings = ("birthdays:$birthday,vacations:$vacation,news_posts:$posts,news_comments:$comments")

        return "{type:'notifications',parameters:{obj:'setEnabledEvents',enabled_events:{$settings}}}"
    }

    fun formImageRequest(url: String, width: Int, height: Int, isOriginal: Int): String {
        return "{'type':'image','parameters':{'obj':'getfromurl','url':'$url','width':'$width','height':'$height', isoriginal:'$isOriginal', hq:'2' }}"
    }

    fun formCheckSecretSantaStatus(): String {
        return "{wcf:'service_functions',platform:'ios',airversion:'2.0',request:{action:'isnewyearcome'}}"
    }

    fun formSendPushTokenRequest(token: String): String {
        return "{type:'notifications',parameters:{obj:'pushTokenRefresh',token:'$token', applicationid:'$UNIQUE_APP_ID',devicetype:'android',airversion:'$APP_VERSION'}}"
    }

    @JvmStatic
    fun formProfileRequest(profileId: String): String {
        return "{'type':'get_profile','login':'${profileId.doubleSlash()}'}"
    }

    @JvmStatic
    fun formChatAvailabilityRequest(respondentLogin: String): String {
        return "{'wcf':'messenger','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'canusemessenger', users:'${respondentLogin.doubleSlash()}'}}"
    }

    fun formCheckVersionRequest(): String {
        return "{'wcf':'service_functions','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'checkairversion'}}"
    }

    fun formProfilesRequest(profileId: String): String {
        return "{'type':'like','parameters':{obj:'profilelikers','user':'${profileId.doubleSlash()}'}}"
    }

    @JvmStatic
    fun formLikeUserRequest(isLiked: Int, profileId: String): String {
        return "{type:'like',parameters:{obj:'profile', likeprofile:'${profileId.doubleSlash()}',likeval:'$isLiked'}}"
    }

    fun formGetCalendarEventRequest(amount: Int): String {
        return "{'wcf':'feed','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'getafisha', 'itemcount':'$amount'}}"
    }

    fun formGetPosterEventRequest(id: Int): String {
        return "{'wcf':'afisha','platform':'$PLATFORM','request':{'action':'get_event', 'EventId':$id}}"
    }

    fun sendQuestion(body: String, anonymous: Int, eventId: Int): String {
        return "{'wcf':'afisha','platform':'$PLATFORM','request':{'action':'edit_question','Body':'$body','Answered':0,'Anonimus':$anonymous,'Hidden':0,'Pin':0,'Approved':0,'EventId':$eventId}}"
    }

    fun likeQuestion(id: Int): String {
        return "{'wcf':'afisha','platform':'$PLATFORM','request':{'action':'like_question','QuestionId':$id}}"
    }

    fun unlikeQuestion(id: Int): String {
        return "{'wcf':'afisha','platform':'$PLATFORM','request':{'action':'dislike_question','QuestionId':$id}}"
    }

    fun formTopNewsRequest(amount: Int): String {
        return "{'wcf':'feed','platform':'$PLATFORM','airversion':'$APP_VERSION','request':{'action':'getnewsofbank', 'newscount':$amount}}"
    }

    fun formSearchRequest(keyword: String, peopleAmount: Int, pageAmount: Int): String {
        return "{type:'search',parameters:{term:'$keyword',peoplecount:'$peopleAmount',pagecount:'$pageAmount', city:'$CITY'}}"
    }

    fun formGetFullUserId(userId: String): String {
        return "{type:'account',parameters:{user:'$userId'}}"
    }

    fun formLogEvents(events: List<Event>): String {
        val gson = Gson()
        val json = gson.toJson(events)
        return "{'type':'setlog','logs':$json}"
    }

    fun formSaveMobileRequest(number: String): String {
        return "{type:'account',parameters:{obj:'setvalue', destination:'CellPhone', message:'$number'}}"
    }

    @JvmStatic
    fun formSaveAboutMe(text: String): String {
        return "{type:'account',parameters:{obj:'setvalue', destination:'AboutMe', message:'$text'}}"
    }

    fun formDeletePostRequest(postType: String, postId: String, postUrl: String): String {
        return "{type:'delete', parameters:{obj:'${postType}post',postid:'$postId', posturl:'$postUrl'}}"
    }

    fun formUploadImage(encodedImage: String): String {
        return "{type:'userphoto', parameters:{ image:'$encodedImage'}}"
    }

    // Surveys

    fun formQuizRequest(quizId: String): String {
        return "{'type':'surveys','subtype':'survey','action':'getresult','id':'$quizId'}"
    }

    fun formHideQuizRequest(quizId: String): String {
        return "{'type':'surveys','subtype':'survey','action':'hide','id':'$quizId'}"
    }

    fun formUploadSurveyAnswer(quizId: String, answer: Answer): String {
        var text: String? = answer.text
        val answerId = answer.answerId
        if (text == null) text = ""

        val currentTime = DateConverter.convertToUtcDate(Date().time, DATE_PATTERN_8)
        return "{'type':'surveys','subtype':'answer','action':'set', 'surveyid':'$quizId','responddate':'$currentTime','answers':[{'questionid':'${answer.questionId}', 'answerid':'$answerId', 'text':'$text'}]}"
    }

    fun formNewsInjectionQuizRequest(): String {
        return "{'type':'surveys','subtype':'survey','action':'getactive','surveyscount':'1'}"
    }

    fun formUploadResults(quizId: String, answers: List<Answer>): String {
        val sBuilder = StringBuilder()
        for (i in answers.indices) {
            if (i > 0) sBuilder.append(", ")

            var text: String? = answers[i].text
            if (text == null) text = ""

            sBuilder.append("{'questionid':'")
                    .append(answers[i].questionId)
                    .append("', 'answerid':'")
                    .append(answers[i].answerId)
                    .append("', 'text':'")
                    .append(text).append("'}")
        }
        val currentTime = DateConverter.convertToUtcDate(Date().time, DATE_PATTERN_8)

        return "{'type':'surveys','subtype':'answer','action':'set', 'surveyid':'$quizId','responddate':'$currentTime','answers':[$sBuilder]}"
    }

    // Favorite

    fun addUserToFavorite(id: String): String {
        return "{'type':'favorite','parameters':{'obj':'adduser','friend':'$id'}}"
    }

    fun removeUserFromFavorite(id: String): String {
        return "{'type':'favorite','parameters':{'obj':'deleteuser','friend':'$id'}}"
    }

    fun addPostToFavorite(feedUrl: String, feedType: String, postId: String): String {
        return "{'type':'favorite','parameters':{'obj':'addpage','posturl':'$feedUrl',type:'$feedType','postid':'$postId'}}"
    }

    fun removePostFromFavorite(postId: String): String {
        return "{'type':'favorite','parameters':{'obj':'deletepage','postid':'$postId'}}"
    }

    fun formFavoritePostRequest(): String {
        return "{'type':'favorite','parameters':{'obj':'pages'}}"
    }

    fun favoriteProfiles(): String {
        return "{'type':'favorite','parameters':{'authorsimageloadmode':1}}"
    }

    fun getPostInfo(postUrl: String, postId: String): String {
        return "{'type':'newscounters','parameters':{'counters':[{'posturl':'$postUrl','postid':'$postId','obj':'news'}]}}"
    }

    fun team(type: String, id: String): String? {
        return "{type:'team',parameters:{user:'${id.doubleSlash()}',teamvalue:'$type',authorsimageloadmode:1}}"
    }

    private fun String.doubleSlash(): String{
        return replace("\\", "\\\\")
    }
}
