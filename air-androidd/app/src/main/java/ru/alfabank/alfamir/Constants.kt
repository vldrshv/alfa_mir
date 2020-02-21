package ru.alfabank.alfamir

class Constants {

    companion object {

        val USE_HOMO = false
        val BUILD_TYPE_PROD = true

        var LOG_ITEMS_COUNT = 20

        const val PHOTO_FORMAT_ERROR = -1
        const val PHOTO_FORMAT_CUSTOM = 0
        const val PHOTO_FORMAT_SQUARE = 1

        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1

        const val SUBSCRIBE_TYPE_POST = 0
        const val SUBSCRIBE_TYPE_FEED = 1

        const val REQUEST_TYPE_GET_ACTIVE_SUBSCRIPTIONS = 0
        const val REQUEST_TYPE_SUBSCRIBE = 1
        const val REQUEST_TYPE_UNSUBSCRIBE = 3
        const val REQUEST_TYPE_GET_NOTIFICATIONS = 2
        const val REQUEST_TYPE_MARK_NOTIFICATION_AS_VIEWED = 3
        const val REQUEST_TYPE_MARK_ALL_NOTIFICATION_AS_VIEWED = 4
        const val REQUEST_TYPE_MARK_ALL_NOTIFICATION_AS_UNVIEWED = 5
        const val REQUEST_TYPE_GET_NEW_NOTIFICATIONS_COUNT = 6

        const val ITEM_STATE_UNMARKED = 1

        const val ADD = 0
        const val EDIT = 1
        const val DELETE = 2

        const val REQUEST_TYPE_LOAD = 0
        const val REQUEST_TYPE_LOAD_MORE = 1
        const val REQUEST_TYPE_RELOAD = 2

        var FEED_LOAD_MORE_ITEMS = 6

        const val PREF_UNIQUE_ID = "UNIQUE_APP_ID"

        const val PLATFORM = "android"

        const val PROFILE_ID = "id"
        const val QUIZ_ID = "id"

        const val INTENT_SOURCE = "intent_source"
        const val INTENT_SOURCE_PUSH = 0
        const val INTENT_SOURCE_NOTIFICATION = 1
        const val INTENT_SOURCE_CARD = 2
        const val INTENT_SOURCE_DEFAULT = 3

        const val FRAGMENT_TYPE_QUIZ_RADIO_BUTTON_QUESTION = 0
        const val FRAGMENT_TYPE_QUIZ_CHECKBOX = 1
        const val FRAGMENT_TYPE_QUIZ_RESULT = 2
        const val FRAGMENT_TYPE_QUIZ_INTRO = 3
        const val FRAGMENT_TYPE_QUIZ_CONTINUE = 4
        const val FRAGMENT_TYPE_QUIZ_TEXTAREA_QUESTION = 5

        const val SHORT_ANIMATION_DURATION = 200

        const val FRAGMENT_TRANSITION_ADD = 0
        const val FRAGMENT_TRANSITION_REPLACE = 1
        const val FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT = 2
        const val FRAGMENT_TRANSITION_REPLACE_TO_LEFT = 3
        const val FRAGMENT_TRANSITION_ADD_TO_LEFT = 4

        const val TOAST_ACTION_TYPE_LOAD = 0
        const val TOAST_ACTION_TYPE_LOAD_MORE = 1

        const val VIEW_HOLDER_TYPE_NEWS = 0
        const val VIEW_HOLDER_TYPE_QUIZ = 1

        const val FEED_UPLOAD_AMOUNT = 6
        const val NOTIFICATION_UPLOAD_AMOUNT = 16

        const val REGISTRATION_XML = ("<?xml version=\"1.0\" encoding=\"utf-8\"?>  "
                + "  \n<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\"  "
                + "  \nxmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">  "
                + "  \n<content type=\"application/xml\">  "
                + "  \n<m:properties>  "
                + "  \n<d:DeviceType>Android</d:DeviceType>  "
                + "  \n</m:properties>  "
                + "  \n</content></entry>")

        const val MAIN_FEED = "main"

        const val REGISTRATION_SUF = "/odata/applications/v4/ru.alfabank.alfamir/Connections"
        const val DATA_SUF = "/mobile/api/getdata"
        val API_SERVER =
                when (USE_HOMO) {
                    true -> if (BUILD_TYPE_PROD) "https://mobilelab.alfabank.ru:8445/" else "https://mobilelab.alfabank.ru:443/"
                    false -> if (BUILD_TYPE_PROD) "https://smpapi.alfabank.ru/" else "https://smpapi.alfabank.ru:8443/"
                }

        val REQUEST_HEADER = if (BUILD_TYPE_PROD) "AlfaMirRequest" else "AlfaMirRequestTest"

        // test config
        var POST_VIEW = 3

        const val POST_VIEW_HYBRID = 1 // hybrid
        const val POST_VIEW_NATIVE = 2 // native
        const val POST_VIEW_SINGLE_WEB = 3// web

        const val ACTION_POST_SENDING = "action_post_sending"
        const val POST_SENDING_SUCCESS = "post_sending_success"
        const val POST_IMAGE_LOADING_FAILED = "post_image_loading_failed"
        const val POST_SENT = "post_sending_failed"

        const val NO_SERVER_CONNECTION = "Нет связи с сервером"

        const val HTML_STYLE_PREFIX = "<style>" +
        "ul, ol, li, p, div, h3, h2, h1, span, a " +
                "{ " +
                    "font-family: Roboto !important; " +
                    "font-size: 13pt !important; " +
                    "line-height:28px !important; " +
                    "font-weight: normal !important;  " +
                "}" +
        "</style>"
    }

    object Initialization {
        @JvmStatic
        var USER_LOGIN = ""
        @JvmStatic
        var ALFA_TV_ENABLED = true
        @JvmStatic
        var SCREEN_WIDTH_PHYSICAL = 0
        @JvmStatic
        var DENSITY = 0f
        var USER_ID = ""
        var AUTH_STRING = ""
        var CITY = ""
        var MESSENGER_ENABLED: Boolean = false
        @JvmStatic
        var MESSENGER_LP_ENABLED: Boolean = false
        var IS_NEW_YEAR_COME: Boolean = false
        var PHOTOS_DISABLED: Boolean = false
        var MESSENGER_LP_TIMEOUT: Long = 60
        var POSTER_LP_ENABLED: Boolean = false
        var APP_VERSION = BuildConfig.VERSION_NAME
        var APP_BUILD = BuildConfig.VERSION_CODE
        var UNIQUE_APP_ID = ""
        var SESSION_ID = ""
        var SCREEN_HEIGHT_PHYSICAL = 0
        var SCREEN_HEIGHT_ACTIVE_SIZE = 0
        var SCREEN_WIDTH_DP = 0
        var ANDROID_RICH_TEXT = 0 // TODO delete
        var WHATS_NEW = ""
        var AUTHORITY = "ru.alfabank.alfamir.fileprovider"
        var UPDATE_AVAILABLE = "update_available_key"
        internal var TIMEZONE = 3
        internal var PHONE_MODEL = ""
    }

    object Search {
        const val VIEW_TYPE_PERSON = 0
        const val VIEW_TYPE_PAGE = 1
        const val VIEW_TYPE_HEADER = 2
        const val VIEW_TYPE_FOOTER = 3
        const val VIEW_TYPE_EMPTY = 4
        const val VIEW_TYPE_PAGE_HEADER = 5
        const val VIEW_TYPE_PERSON_HEADER = 6
        const val VIEW_TYPE_PAGE_MORE = 7
        const val VIEW_TYPE_PERSON_MORE = 8
    }

    object Post {

        // bundle
        const val FEED_TYPE = "type" // "community"
        const val POST_ID = "id"
        const val POST_URL = "post_url"
        const val FEED_URL = "url" // "http://alfa/Community/forum"
        const val FEED_ID = "feed_id" // "http://alfa/Community/forum"
        const val FEED_TITLE = "feedTitle"
        const val COMMENTS_FIRST = "showComments"
        const val COMMENT_ID = "commentId"
        const val POST_TYPE = "contentType"
        const val POST_POSITION = "position" // TODO should be deleted?
        const val POST_CREATION_ENABLED = "postCreationEnabled"

        // view

        const val POST_HEADER = 0
        const val POST_TEXT = 1
        const val POST_HTML = 2
        const val POST_SINGLE_HTML = 3
        const val POST_PICTURE = 4
        const val POST_FOOTER = 5
        const val POST_COMMENT = 6
        const val POST_EXTRA_SPACE = 7
        const val POST_QUOTE = 8
        const val POST_VIDEO = 9
        const val POST_SUB_HEADER = 10
    }

    object Poster {
        const val POSTER_ENABLED = true
        const val POSTER_ID = "posterId"
        const val POSTER_TITLE = "posterTitle"
        const val IS_SLIDO = "isSliDo"
        const val TEMP_QUESTION_KEY = "tempQuestionKey"
        const val TEMP_STRING_KEY = "tempStringKey"
    }

    enum class QuestionSortType {
        POPULAR, RECENT
    }

    object Show {
        const val SHOW = 0
        const val SHOW_CURRENT = 1
        const val SHOW_SEPARATOR = 2
        const val SHOW_POSITION = "position"
        const val SHOW_ID = "id"
        const val PROGRESS_STATUS_CURRENT = 0
        const val PROGRESS_STATUS_PROLONGED = 1
        const val PROGRESS_STATUS_ENDED = 2
        const val PASSWORD_NETURAL = 0
        const val PASSWORD_FOCUSED = 1
        const val PASSWORD_WRONG = 2
    }

    object Feed_element {
        const val FEED_HEADER = 0
        const val FEED_POST = 1
    }

    object Feed_new {
        const val MAIN_FEED = "main"
    }

    object Feed {
        const val NEWS_TYPE_MY_ALFA_LIFE = 3
        const val NEWS_TYPE_SPORT = 6
        const val NEWS_TYPE_FORUM = 7
        const val NEWS_TYPE_NOTICE_BOARD = 8
        const val NEWS_TYPE_SEARCH_NEWS = 9
        const val NEWS_TYPE_SEARCH_MEDIA = 13
        const val NEWS_TYPE_FAVORITE_MEDIA = 11
        const val NEWS_TYPE_NEWS_NOTIFICATION = 18
        const val NEWS_TYPE_POST_NOTIFICATION = 21
        const val NEWS_TYPE_COMMENT_NOTIFICATION = 22
    }

    object Notification {
        const val NOTIFICATION_TYPE_BIRTHDAY = 0
        const val NOTIFICATION_TYPE_VACATION = 1
        const val NOTIFICATION_TYPE_NEWS = 2
        const val NOTIFICATION_TYPE_BLOG = 3
        const val NOTIFICATION_TYPE_COMMUNITY = 4
        const val NOTIFICATION_TYPE_NEWS_COMMENT = 5
        const val NOTIFICATION_TYPE_BLOG_COMMENT = 6
        const val NOTIFICATION_TYPE_COMMUNITY_COMMENT = 7
        const val NOTIFICATION_TYPE_MEDIA = 8
        const val NOTIFICATION_TYPE_MEDIA_COMMENT = 9
        const val NOTIFICATION_TYPE_SURVEY = 10
        const val NOTIFICATION_TYPE_SECRET_SANTA = 11
    }

    object NotificationSettings {
        const val SWITCH_MAIN = 0
        const val SWITCH_BIRTHDAYS = 1
        const val SWITCH_VACATIONS = 2
        const val SWITCH_POSTS = 3
        const val SWITCH_COMMENTS = 4
    }

    object Profile {
        const val ADMINISTRATION_TEAM = "dep"
        const val FUNCTIONAL_TEAM = "func"
        const val USER_NAME = "user_name"
        const val USER_IMAGE = "user_image"
        const val PHONE_WORK_0 = 0
        const val PHONE_WORK_1 = 1
        const val PHONE_WORK_2 = 2
        const val PHONE_WORK_3 = 3
        const val PHONE_WORK_4 = 4
        const val PHONE_MOBILE_0 = 5
        const val PHONE_MOBILE_1 = 6
        const val PHONE_MOBILE_2 = 7
        const val PHONE_MOBILE_3 = 8
        const val PHONE_MOBILE_4 = 9
        const val ADD_PHONE_MOBILE = 10
    }

    object Survey {
        const val RADIO_BUTTON_QUESTION = 0
        const val CHECKBOX_QUESTION = 1
        const val TEXT_AREA = 2
    }

    object DateFormat {
        const val DATE_PATTERN_0 = "dd.MM.yyyy HH:mm:ss"
        const val DATE_PATTERN_1 = "dd.MM.yyyy"
        const val DATE_PATTERN_2 = "dd MMMM"
        const val DATE_PATTERN_3 = "dd.MM"
        const val DATE_PATTERN_4 = "dd MMMM yyyy"
        const val DATE_PATTERN_5 = "dd MMMM HH:mm"
        const val DATE_PATTERN_7 = "HH:mm"
        const val DATE_PATTERN_8 = "yyyy-MM-dd HH:mm:ss"
        const val DATE_PATTERN_9 = "yyyy-MM-dd'T'HH:mm:sss"
        const val TIME_ZONE_GREENWICH = 0
        const val TIME_ZONE_MOSCOW = 1
    }

    object DateFormatter {
        const val DATE_PATTERN_0 = "dd.MM.yyyy HH:mm:ss"
        const val DATE_PATTERN_1 = "dd.MM.yyyy"
        const val DATE_PATTERN_2 = "dd MMMM"
        const val DATE_PATTERN_3 = "dd.MM"
        const val DATE_PATTERN_4 = "dd MMMM yyyy"
        const val DATE_PATTERN_5 = "dd MMMM HH:mm"
        const val DATE_PATTERN_7 = "HH:mm"
        const val DATE_PATTERN_8 = "yyyy-MM-dd HH:mm:ss"
        const val DATE_PATTERN_9 = "yyyy-MM-dd'T'HH:mm:sss"
        const val DATE_PATTERN_10 = "dd.MM.yyyy HH:mm:sss"
        const val TIME_ZONE_GREENWICH = "GMT"
        const val TIME_ZONE_MOSCOW = "Europe/Moscow"
    }

    object Logger {
        const val SENDING_STATUS_STORED = 0
        const val SENDING_STATUS_SENDING = 1
    }

    object Messenger {
        const val USER_ID = "user_id"
        const val CHAT_ID = "chat_id"
        const val CHAT_TYPE = "chat_type"
        const val MESSAGE_TEXT = 0
        const val MESSAGE_IMAGE = -1
        const val MESSAGE_FILE = -2
        const val VIEW_TYPE_TIME_BUBBLE = 1
        const val VIEW_TYPE_MESSAGE_NEW_DECORATOR = 2
        const val DIRECTION_UP = 0
        const val DIRECTION_DOWN = 1
        const val MESSAGE_STATUS_PENDING = 0
        const val MESSAGE_STATUS_SENT = 1
        const val MESSAGE_STATUS_DELIVERED = 2
        const val MESSAGE_STATUS_READ = 3
        const val MESSAGE_STATUS_UPDATING = 4
        const val TAKE_PHOTO_INTENT_CODE = 200
        const val CHOOSE_FILE_INTENT_CODE = 201
        const val ATTACHMENTS_LIST = "attachments_list"
        const val SELECTED_ITEM = "selected_position"
        const val CHAT_ENCRYPTION = true
    }

    object UI {
        const val FRAGMENT_TO_BACKSTACK = "fragment_to_back_stack"
    }


    object Blur {
        const val STRONG = 0
        const val LIGHT = 1
    }

    object Log {
        const val LOG_IMAGE = "LOG_IMAGE"
        const val LOG_MENU = "LOG_MENU"
        const val LOG_HOME = "LOG_HOME"
        const val LOG_PROFILE = "LOG_PROFILE"
        const val LOG_MAIN = "LOG_MAIN"
        const val LOG_SURVEY = "LOG_SURVEY"
        const val LOG_TEST = "LOG_TEST"
        const val LOG_ALFA_TV = "LOG_ALFA_TV"
        const val LOG_THREAD = "LOG_THREAD"
        const val LOG_REQUESTS = "LOG_REQUESTS"
        const val LOG_IMAGE_SIZE = "LOG_IMAGE_SIZE"
        const val LOG_RESPONSE = "LOG_RESPONSE"
        const val LOG_HTTP_SIZE = "LOG_HTTP_SIZE"
        const val LOG_HTTP_RESPONSE = "LOG_HTTP_RESPONSE"
        const val LOG_HTTP_REQUEST = "LOG_HTTP_REQUEST"
        const val LOG_CHAT = "LOG_CHAT"
        const val LOG_CHAT_SCROLL = "LOG_CHAT_SCROLL"
        const val LOG_JS_INJECT = "LOG_JS_INJECT"
        const val LOG_CONNECTION_ERROR = "LOG_CONNECTION_ERROR"
        const val LOG_DI = "LOG_DI"
        const val LOG_INITIALIZATION = "LOG_INITIALIZATION"
    }

    object RequestCode {
        const val CAMERA = 0
        const val CALL_HH = 5
        const val CALL_HD = 6
    }
}
