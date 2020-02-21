package ru.alfabank.alfamir.main.home_fragment.data.source.remote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.main.home_fragment.data.dto.TopNewsRaw;
import ru.alfabank.alfamir.main.home_fragment.data.source.repository.TopNewsDataSource;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class TopNewsRemoteDataSource implements TopNewsDataSource {

    private WebService mWebService;

    @Inject
    TopNewsRemoteDataSource(WebService webService){
        mWebService = webService;
    }

    @Override
    public Observable<List<TopNewsRaw>> getTopNews(RequestValues requestValues) {
        int amount = requestValues.getEventAmount();
        String request = RequestFactory.INSTANCE.formTopNewsRequest(amount);
        return mWebService.requestX(request).map(JsonWrapper::topNews);
    }
}