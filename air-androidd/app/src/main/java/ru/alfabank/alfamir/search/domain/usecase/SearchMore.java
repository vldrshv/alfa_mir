package ru.alfabank.alfamir.search.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.search.data.source.repository.SearchRepository;
import ru.alfabank.alfamir.search.domain.mapper.SearchResultMapper;
import ru.alfabank.alfamir.search.presentation.dto.Page;
import ru.alfabank.alfamir.search.presentation.dto.Person;

public class SearchMore extends UseCase<SearchMore.RequestValues, SearchMore.ResponseValue> {

    private SearchRepository mSearchRepository;
    private SearchResultMapper mSearchResultMapper;

    @Inject
    SearchMore(SearchRepository searchRepository,
           SearchResultMapper searchResultMapper) {
        mSearchRepository = searchRepository;
        mSearchResultMapper = searchResultMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        boolean mMorePeople = requestValues.isMorePeople();
        boolean mMorePages = requestValues.isMorePages();
        String keyword = requestValues.getKeyword();
        int peopleAmount = 0;
        int pageAmount = 0;
        if(mMorePeople) peopleAmount = 50;
        if(mMorePages) pageAmount = 50;
        return mSearchRepository.search(keyword, peopleAmount, pageAmount)
                .map(mSearchResultMapper)
                .flatMap(searchResult -> {
                    List<Person> personList = searchResult.getPersonList();
                    List<Page> pageList = searchResult.getPageList();
                    return Observable.just(new ResponseValue(personList, pageList));
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private boolean mMorePeople;
        private boolean mMorePages;
        private String mKeyword;
        public RequestValues(boolean morePeople, boolean morePages, String keyword){
            mMorePeople = morePeople;
            mMorePages = morePages;
            mKeyword = keyword;
        }
        boolean isMorePeople() {
            return mMorePeople;
        }
        boolean isMorePages() {
            return mMorePages;
        }
        public String getKeyword() {
            return mKeyword;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<Person> mPersonList;
        private List<Page> mPageList;
        public ResponseValue(List<Person> personList,
                             List<Page> pageList){
            mPersonList = personList;
            mPageList = pageList;
        }
        public List<Person> getPersonList() {
            return mPersonList;
        }
        public List<Page> getPageList() {
            return mPageList;
        }
    }
}