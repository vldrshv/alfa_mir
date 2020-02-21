package ru.alfabank.alfamir.alfa_tv.data.source.remote;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowCurrentState;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowRaw;
import ru.alfabank.alfamir.alfa_tv.data.dto.ShowUriRaw;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowDataSource;
import ru.alfabank.alfamir.data.dto.video.VideoUrlRaw;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

import static ru.alfabank.alfamir.Constants.Log.LOG_REQUESTS;
import static ru.alfabank.alfamir.Constants.Log.LOG_RESPONSE;

public class ShowRemoteDataSource implements ShowDataSource {

    private final WebService service;

    private FakeDataSource mMockDs;
    private LogWrapper mLog;
    private boolean isTest = false;

    @Inject
    ShowRemoteDataSource(WebService service,
                         FakeDataSource mockDs,
                         LogWrapper logWrapper) {
        this.service = service;
        mMockDs = mockDs;
        mLog = logWrapper;
    }

    @Override
    public Observable<List<ShowRaw>> getShowList(RequestValues requestValues) {
        if (isTest) {
            String json = mMockDs.getJson();
            List<ShowRaw> showList = JsonWrapper.getShows(json);
            return Observable.just(showList);
        }

        String request = RequestFactory.formGetAlfaTvShowsRequest();
        mLog.debug(LOG_REQUESTS, this.toString() + " getShowList request" + request);
        return service.requestX(request).map(response -> {
            mLog.debug(LOG_RESPONSE, this.toString() + " getShowList response" + response);
            return JsonWrapper.getShows(response);
        });
    }

    @Override
    public Observable<Boolean> getCurrentVideoState(int videoId) {
        String request = RequestFactory.formGetShowCurrentStateRequest(videoId);
        mLog.debug(LOG_REQUESTS, this.toString() + " getCurrentVideoState request" + request);
        return service.requestX(request).map(response -> {
            mLog.debug(LOG_RESPONSE, this.toString() + " getCurrentVideoState response" + response);
            ShowCurrentState showCurrentState = JsonWrapper.getVideoCurrentState(response);
            return showCurrentState.isOnAir();
        });
    }

    @Override
    public Observable<String> getVideoUrl(String videoId, String phoneIp, String password) {
        String request = RequestFactory.INSTANCE.formGetTokenRequest(videoId, phoneIp, password);
        return service.requestX(request).map(response -> {
            VideoUrlRaw videoUrlRaw = JsonWrapper.getVideoUrlDeserialize(response);
            String url = videoUrlRaw.getUrl();
            String token = videoUrlRaw.getToken();
            String lineSeparator = "?token=";
            return url + lineSeparator + token;
        });
    }

    @Override
    public Observable<ShowUriRaw> getVideoUri(String videoId, String phoneIp, String password) {
        String request = RequestFactory.INSTANCE.formGetTokenRequest(videoId, phoneIp, password);
        return service.requestX(request).map(JsonWrapper::getShowUrlDeserialize);
    }

    @Override
    public Observable<ShowRaw> getShow(int id) {
        return null;
    }

}
