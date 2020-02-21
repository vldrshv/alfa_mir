package ru.alfabank.alfamir.main.menu_fragment.data.source.remote;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.main.menu_fragment.data.dto.Version;
import ru.alfabank.alfamir.main.menu_fragment.data.source.repository.AppInfoDataSource;
import ru.alfabank.alfamir.utility.static_utilities.JsonWrapper;
import ru.alfabank.alfamir.utility.static_utilities.RequestFactory;

public class AppInfoRemoteDataSorce implements AppInfoDataSource {

    private WebService mWebService;

    @Inject
    AppInfoRemoteDataSorce(WebService webService){
        mWebService = webService;
    }

    @Override
    public Observable<Version> getVersionInfo() {
        String request = RequestFactory.INSTANCE.formCheckVersionRequest();
        return mWebService.requestX(request).map(JsonWrapper::getVersion);
    }
}
