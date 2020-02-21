package ru.alfabank.alfamir.data.source.remote.data_source;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.source.remote.api.WebService;
import ru.alfabank.alfamir.data.source.repositories.favorite_pages.FavoritePagesDataSource;

public class FavoritePagesRemoteDataSource implements FavoritePagesDataSource {

    private final WebService mService;

    @Inject
    FavoritePagesRemoteDataSource(WebService service) {
        mService = service;
    }

    @Override
    public Observable<String> addPage(String type, String postId, String postUrl) {
        String request = "{'type':'favorite'," + "'parameters':{'obj':'addpage','user':'" + Constants.Initialization.INSTANCE.getUSER_ID() + "','posturl':'" + postUrl + "',type: '" + type + "', postid: '" + postId + "' }}";

        return mService.requestX(request);
    }

    @Override
    public Observable<String> removePage(String postId) {
        String request = "{'type':'favorite'," + "'parameters':{'obj':'deletepage','user':'" + Constants.Initialization.INSTANCE.getUSER_ID() + "', 'postid': '" + postId + "' }}";

        return mService.requestX(request);
    }
}
