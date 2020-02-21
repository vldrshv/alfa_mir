package ru.alfabank.alfamir.utility.logging.remote;

public interface LoggerContract {

    interface Provider {
        void openFeed(String id, String name); // event name: openfeed
        void refresh(String id, String name); // event name: refreshitems
        void loadMore(int requested, int current); // event name: nextitems ??? should change next / from to requested / current
        void search(String searchPhrase); // event name: search
        void like(String id, String name, boolean isLiked); // event name: taplike
        void feedSubscribe(String id, String name, boolean isSubscribed); // event name: ??? feedsubscribe
        void postSubscribe(String id, String name, boolean isFavoured); // event name: ??? postsubscribe
//        void personFavorite(String id, String name, boolean isFavoured); // event name: ??? personfavorite
        void postFavorite(String id, String name, boolean isFavoured); // event name: ??? postfavorite
        void dataReceived(String dataType); // event name: gotdata ??? should change name / getData to dataType
        void openPost(String id, String name); // event name: openarticle
        void showComments(String id, String name); // event name: ??? showcomments
        void send(String id, String name);  // event name: send
        void sendFeedback(); // event name: openfeedback
        void openMenu(); // event name: openmenu
        void openPeople(String name); // event name: openpeoplelist ??? should change title to name
        void openFavoritePosts(); // event name: openfavarticlesnextitems
        void openAlfaTv(); // event name: openalfatvlist ??? should change itemname, itemid to id / name
        void openTvShow(String id, String name); // event name: openalfatvitem
        void openProfile(String id); // event name: openprofile ??? should change userid to id
        void openCreatePost(String feedId); // event name: opensendform

        void onOpenSurvey(String type, int surveyId); // event name: opensurvey
        void onFinishSurvey(String type, int surveyId); // event name: finishsurvey
        void onExitSurvey(String type, int surveyId); // event name: exitsurvey
        void onShowSurveyQuestion(String type, int surveyId, int questionId); // event name: showsurveyquestion
        void onShowSurveyResult(String type, int surveyId); // event name: showsurveyresult
        void onHideSurvey(String type, int surveyId); // event name: hidesurvey

        void createPost(String feedId, String name, String title);  // event name: send
        void openCreateMedia(); // event name: opensendmediaform
        void createMedia(String name);  // event name: send
        void startSession(String applicationid, String appversion, String model, // session id?
                          String certUserName, String login, String platform,
                          String timezone); // event name: startsession
        void endSession(); // event name: endsession
        void background(); // event name: background
        void foreground(); // event name: foreground

        void error(String message, String stackTrace); // event name: error
    }

    interface Client {

        interface Feed {
            void logOpen(String id, String name); // event name: openfeed
            void logRefresh(String id, String name); // event name: refreshitems
            void logLoadMore(int amount, int current); // event name: nextitems
            void logSearch(String phrase); // event name: search
            void logLike(String id, String name, boolean isLiked); // event name: taplike
            void logFeedSubscribe(String id, String name, boolean isSubscribed); // event name: ??? feedsubscribe
            void logPostSubscribe(String id, String name, boolean isSubscribed); // event name: ??? postsubscribe
            void logPostFavorite(String id, String name, boolean isFavoured); // event name: ??? postfavorite
            void logDataReceived(String dataType); // event name: gotdata /* kinda stupid */
        }

        interface MainFeed {
            void logRefresh(String id, String name); // event name: refreshitems
            void logLoadMore(int amount, int current); // event name: nextitems

            void logOpen(String id, String name); // event name: openfeed
            void logLike(String id, String name, boolean isLiked); // event name: taplike
            void logPostSubscribe(String id, String name, boolean isSubscribed); // event name: ??? postsubscribe
            void logPostFavorite(String id, String name, boolean isFavoured); // event name: ??? postfavorite

            void logDataReceived(String dataType); // event name: gotdata /* kinda stupid */
//            void logHideSurvey(String type, int surveyId); // event name: hidesurvey
        }

        interface NewsFeedAdapter {
            void logPostOpen(String postId, String postTitle); // event name: openfeed
            void logPostLike(String postId, String postTitle, boolean isLiked); // event name: taplike
            void logPostSubscribe(String postId, String postTitle, boolean isSubscribed); // event name: ??? postsubscribe
            void logPostFavorite(String postId, String postTitle, boolean isFavoured); // event name: ??? postfavorite
            void logHideSurvey(String type, int surveyId); // event name: hidesurvey
        }

        interface NewsFeed {
            void logFeedRefresh(String feedId, String feedName); // event name: refreshitems
            void logLoadMore(int amount, int current); // event name: nextitems
        }

        interface Survey {
            void logOpen(String type, int surveyId); // event name: opensurvey
            void logFinish(String type, int surveyId); // event name: finishsurvey
            void logExit(String type, int surveyId); // event name: exitsurvey
            void logShowQuestion(String type, int surveyId, int questionId); // event name: showsurveyquestion
            void logResultScreen(String type, int surveyId); // event name: showsurveyresult
        }

        interface Post {
            void logOpen(String id, String name); // event name: openarticle
            void logShowComments(String id, String name); // event name: ??? showcomments
            void logSendComment(String id, String name);  // event name: send
            void logLike(String id, String name, boolean isLiked); // event name: taplike
            void logRefresh(String id, String name); // event name: refreshitems
            void logPostSubscribe(String id, String name, boolean isSubscribed); // event name: ??? postsubscribe
            void logPostFavorite(String id, String name, boolean isFavoured);
            void logDataReceived(String dataType); // event name: gotdata
        }

        interface MainActivity {
            void logSendFeedback(); // event name: openfeedback
            void logOpenMenu(); // event name: openmenu
            void logSearch(String phrase); // event name: search
            void logOpenFeed(String id, String name); // event name: openfeed
            void logOpenFavPeople(String name); // event name: openfavpeople
            void logOpenAlfaTv(); // event name: openalfatvlist
            void logError(String message, String stackTrace); // event name error
        }

        interface People {
            void logOpen(String name); // event name: openpeoplelist
        }

        interface FavouritePosts {
            void logOpen(); // event name: openfavarticles
        }

        interface AlfaTv {
            void logOpen(); // event name: openalfatvlist
        }

        interface TvShow {
            void logOpen(String id, String name); // event name: openalfatvitem
        }

        interface Profile {
            void logOpen(String id); // event name: openprofile
            void logError(String message, String stackTrace); // event name error
        }

        interface NewPost {
            void logOpen(String feedId); // event name: opensendform
            void logCreate(String feedId, String name, String title);  // event name: send
        }

        interface NewMedia {
            void logOpen(); // event name: opensendmediaform
            void logSend(String name);  // event name: send
        }

        interface LifecycleTracker {
            void logBackground(); // event name: background
            void logForeground(); // event name: foreground
        }

        interface PersonalInfo {
            void logError(String message, String stackTrace); // event name error
        }

        interface AppDestroyedTracker {
            void logEndSession(); // event name: endsession
        }

        interface AppStartedTracker {
            void logStartSession(); // event name: startsession
        }

    }
}