package ru.alfabank.alfamir.search.data.source.remote;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.search.data.dto.SearchResultRaw;
import ru.alfabank.alfamir.search.data.source.repository.SearchDataSource;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class SearchRemoteDataSource implements SearchDataSource {

    WebService mWebService;

    @Inject
    SearchRemoteDataSource(WebService webService){
        mWebService = webService;
    }

    @Override
    public Observable<SearchResultRaw> search(String keyword, int peopleAmount, int pageAmount) {
        String request = RequestFactory.INSTANCE.formSearchRequest(keyword, peopleAmount, pageAmount);
        return mWebService.requestX(request).map(JsonWrapper::getSearchResultRaw);
    }
}
