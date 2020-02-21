package ru.alfabank.alfamir.search.data.source.repository;

import io.reactivex.Observable;
import ru.alfabank.alfamir.search.data.dto.SearchResultRaw;

public interface SearchDataSource {

    Observable<SearchResultRaw> search(String keyword, int peopleAmount, int pageAmount);

}
