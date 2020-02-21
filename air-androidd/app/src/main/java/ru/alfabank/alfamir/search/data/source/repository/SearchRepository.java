package ru.alfabank.alfamir.search.data.source.repository;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.search.data.dto.SearchResultRaw;

public class SearchRepository implements SearchDataSource {

    private SearchDataSource mRemoteDataSource;

    @Inject
    SearchRepository(@Remote SearchDataSource searchDataSource){
        mRemoteDataSource = searchDataSource;
    }

    @Override
    public Observable<SearchResultRaw> search(String keyword, int peopleAmount, int pageAmount) {
        return mRemoteDataSource.search(keyword, peopleAmount, pageAmount);
    }
}
