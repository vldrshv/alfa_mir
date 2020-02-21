package ru.alfabank.alfamir.utility.logging.remote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.utility.logging.remote.events.BackgroundEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.CreateMediaEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.CreatePostEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.DataReceivedEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.EndSessionEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.ErrorEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.Event;
import ru.alfabank.alfamir.utility.logging.remote.events.ExitSurveyEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.FeedSubscribeEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.FinishSurveyEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.ForegroundEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.HideSurveyEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.LikeEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.LoadMoreEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenAlfaTvEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenCreateMediaEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenCreatePostEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenFavPostsEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenFeedEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenMenuEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenPeopleEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenPostEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenProfileEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenSurveyEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.OpenTvShowEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.PostFavoriteEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.PostSubscribeEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.RefreshEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.SearchEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.SendEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.SendFeedbackEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.ShowCommentsEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.ShowSurveyQuestionEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.ShowSurveyResultEvent;
import ru.alfabank.alfamir.utility.logging.remote.events.StartSessionEvent;


/**
 * Created by U_M0WY5 on 01.03.2018.
 */
@Singleton
public class Logger implements LoggerContract.Provider {

    private final List<Event> log;
    private WebService mService;
    private AtomicBoolean isIdle;
    private int amount = Constants.Companion.getLOG_ITEMS_COUNT();

    @Inject
    public Logger(WebService service) {
        log = new ArrayList<>();
        mService = service;
        isIdle = new AtomicBoolean(true);
    }

    private void sendLog(int amount) {
        isIdle.set(false);
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            log.get(i).setState(Constants.Logger.SENDING_STATUS_SENDING);
            events.add(log.get(i));
        }
        mService.sendLog(events, new LogEventsCallback() {
            @Override
            public void onEventsLogged() {
                Iterator<Event> iterator = log.iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    if (event.getState() == Constants.Logger.SENDING_STATUS_SENDING) {
                        iterator.remove();
                    }
                }

                /* job's done */
                isIdle.set(true);
                checkLog();
            }

            @Override
            public void onDataNotAvailable() {
                /* job's done */
                isIdle.set(true);
            }
        });
    }

    private void saveEvent(Event event) {
        synchronized (log) {
            log.add(event);
            if (event instanceof BackgroundEvent || event instanceof EndSessionEvent) {
                sendLog(log.size());
            } else checkLog();
        }
    }

    private void checkLog() {
        if (isIdle.get() && (log.size() / amount) >= 1) {
            sendLog(amount);
        }
    }

    @Override
    public void openFeed(String id, String name) {
        Event event = new OpenFeedEvent(id, name);
        saveEvent(event);
    }

    @Override
    public void refresh(String id, String name) {
        Event event = new RefreshEvent(id, name);
        saveEvent(event);
    }

    @Override
    public void loadMore(int requested, int current) {
        Event event = new LoadMoreEvent(requested, current);
        saveEvent(event);
    }

    @Override
    public void search(String searchPhrase) {
        Event event = new SearchEvent(searchPhrase);
        saveEvent(event);
    }

    @Override
    public void like(String id, String name, boolean isLiked) {
        Event event = new LikeEvent(id, name, isLiked);
        saveEvent(event);
    }

    @Override
    public void feedSubscribe(String id, String name, boolean isSubscribed) {
        Event event = new FeedSubscribeEvent(id, name, isSubscribed);
        saveEvent(event);
    }

    @Override
    public void postSubscribe(String id, String name, boolean isFavoured) {
        Event event = new PostSubscribeEvent(id, name, isFavoured);
        saveEvent(event);
    }

    @Override
    public void postFavorite(String id, String name, boolean isFavoured) {
        Event event = new PostFavoriteEvent(id, name, isFavoured);
        saveEvent(event);
    }

    @Override
    public void dataReceived(String dataType) {
        Event event = new DataReceivedEvent(dataType);
        saveEvent(event);
    }

    @Override
    public void openPost(String id, String name) {
        Event event = new OpenPostEvent(id, name);
        saveEvent(event);
    }

    @Override
    public void showComments(String id, String name) {
        Event event = new ShowCommentsEvent(id, name);
        saveEvent(event);
    }

    @Override
    public void send(String id, String name) {
        Event event = new SendEvent(id, name);
        saveEvent(event);
    }

    @Override
    public void sendFeedback() {
        Event event = new SendFeedbackEvent();
        saveEvent(event);
    }

    @Override
    public void openMenu() {
        Event event = new OpenMenuEvent();
        saveEvent(event);
    }

    @Override
    public void openPeople(String name) {
        Event event = new OpenPeopleEvent(name);
        saveEvent(event);
    }

    @Override
    public void openFavoritePosts() {
        Event event = new OpenFavPostsEvent();
        saveEvent(event);
    }

    @Override
    public void openAlfaTv() {
        Event event = new OpenAlfaTvEvent();
        saveEvent(event);
    }

    @Override
    public void openTvShow(String id, String name) {
        Event event = new OpenTvShowEvent(id, name);
        saveEvent(event);
    }

    @Override
    public void openProfile(String id) {
        Event event = new OpenProfileEvent(id);
        saveEvent(event);
    }

    @Override
    public void openCreatePost(String feedId) {
        Event event = new OpenCreatePostEvent(feedId);
        saveEvent(event);
    }

    @Override
    public void onOpenSurvey(String type, int surveyId) {
        Event event = new OpenSurveyEvent(type, surveyId);
        saveEvent(event);
    }

    @Override
    public void onFinishSurvey(String type, int surveyId) {
        Event event = new FinishSurveyEvent(type, surveyId);
        saveEvent(event);
    }

    @Override
    public void onExitSurvey(String type, int surveyId) {
        Event event = new ExitSurveyEvent(type, surveyId);
        saveEvent(event);
    }

    @Override
    public void onShowSurveyQuestion(String type, int surveyId, int questionId) {
        Event event = new ShowSurveyQuestionEvent(type, surveyId, questionId);
        saveEvent(event);
    }

    @Override
    public void onShowSurveyResult(String type, int surveyId) {
        Event event = new ShowSurveyResultEvent(type, surveyId);
        saveEvent(event);
    }

    @Override
    public void onHideSurvey(String type, int surveyId) {
        Event event = new HideSurveyEvent(type, surveyId);
        saveEvent(event);
    }

    @Override
    public void createPost(String feedId, String name, String title) {
        Event event = new CreatePostEvent(feedId, name, title);
        saveEvent(event);
    }

    @Override
    public void openCreateMedia() {
        Event event = new OpenCreateMediaEvent();
        saveEvent(event);
    }

    @Override
    public void createMedia(String name) {
        Event event = new CreateMediaEvent(name);
        saveEvent(event);
    }

    @Override
    public void startSession(String applicationid, String appversion, String model,
                             String certUserName, String login, String platform,
                             String timezone) {
        Event event = new StartSessionEvent(applicationid, appversion, model,
                certUserName, login, platform,
                timezone);
        saveEvent(event);
    }

    @Override
    public void endSession() {
        Event event = new EndSessionEvent();
        saveEvent(event);
    }

    @Override
    public void background() {
        Event event = new BackgroundEvent();
        saveEvent(event);
    }

    @Override
    public void foreground() {
        Event event = new ForegroundEvent();
        saveEvent(event);
    }

    @Override
    public void error(String message, String stackTrace) {
        Event event = new ErrorEvent(message, stackTrace);
        saveEvent(event);
    }

    public interface LogEventsCallback {
        void onEventsLogged();

        void onDataNotAvailable();
    }

}
