package ru.alfabank.alfamir.search.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.search.data.source.repository.SearchRepository;
import ru.alfabank.alfamir.search.domain.mapper.SearchResultMapper;
import ru.alfabank.alfamir.search.presentation.dto.DisplayableSearchItem;
import ru.alfabank.alfamir.search.presentation.dto.HeaderPage;
import ru.alfabank.alfamir.search.presentation.dto.HeaderPerson;
import ru.alfabank.alfamir.search.presentation.dto.Page;
import ru.alfabank.alfamir.search.presentation.dto.PageMore;
import ru.alfabank.alfamir.search.presentation.dto.Person;
import ru.alfabank.alfamir.search.presentation.dto.PersonMore;

public class Search extends UseCase<Search.RequestValues, Search.ResponseValue> {

    private SearchRepository mSearchRepository;
    private SearchResultMapper mSearchResultMapper;

    @Inject
    Search(SearchRepository searchRepository,
           SearchResultMapper searchResultMapper) {
        mSearchRepository = searchRepository;
        mSearchResultMapper = searchResultMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        String keyword = requestValues.getKeyWord();
        int peopleAmount = 4;
        int pageAmount  = 4;
        return mSearchRepository.search(keyword, peopleAmount, pageAmount)
                .map(mSearchResultMapper)
                .flatMap(searchResult -> {
                    List<Person> personList = searchResult.getPersonList();
                    List<Page> pageList = searchResult.getPageList();
                    List<DisplayableSearchItem> displayableSearchItemList = formDisplayableItemList(personList, pageList);
                    return Observable.just(new ResponseValue(displayableSearchItemList));
                });
    }

    private List<DisplayableSearchItem> formDisplayableItemList(List<Person> personList, List<Page> pageList){
        List<DisplayableSearchItem> displayableSearchItemList = new ArrayList<>();
        displayableSearchItemList.add(new HeaderPerson());

        if(personList.size() > 3){
            for (int i = 0; i < 3; i++){
                displayableSearchItemList.add(personList.get(i));
            }
            displayableSearchItemList.add(new PersonMore());
        } else {
            displayableSearchItemList.addAll(personList);
        }

        displayableSearchItemList.add(new HeaderPage());

        if(pageList.size() > 3){
            for (int i = 0; i < 3; i++){
                displayableSearchItemList.add(pageList.get(i));
            }
            displayableSearchItemList.add(new PageMore());
        } else {
            displayableSearchItemList.addAll(pageList);
        }
        return displayableSearchItemList;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mKeyWord;
        public RequestValues(String keyword){
            mKeyWord = keyword;
        }
        String getKeyWord() {
            return mKeyWord;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<DisplayableSearchItem> mDisplayableSearchItemList;
        public ResponseValue(List<DisplayableSearchItem> displayableSearchItemList){
            mDisplayableSearchItemList = displayableSearchItemList;
        }
        public List<DisplayableSearchItem> getDisplayableSearchItemList() {
            return mDisplayableSearchItemList;
        }
    }
}