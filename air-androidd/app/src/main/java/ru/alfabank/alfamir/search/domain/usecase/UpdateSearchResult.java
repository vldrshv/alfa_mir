package ru.alfabank.alfamir.search.domain.usecase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.search.presentation.dto.DisplayableSearchItem;
import ru.alfabank.alfamir.search.presentation.dto.HeaderPerson;
import ru.alfabank.alfamir.search.presentation.dto.Page;
import ru.alfabank.alfamir.search.presentation.dto.Person;

import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PAGE;
import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PAGE_HEADER;

public class UpdateSearchResult extends UseCase<UpdateSearchResult.RequestValues, UpdateSearchResult.ResponseValue> {

    @Inject
    UpdateSearchResult() {}

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        List<DisplayableSearchItem> displayableSearchItemList = requestValues.getDisplayableSearchItemList();
        List<Person> newPersonList = requestValues.getNewPersonList();
        List<Page> newPageList = requestValues.getNewPageList();
        List<DisplayableSearchItem> resultDisplayableSearchItemList = new ArrayList<>();
        if(!newPageList.isEmpty()){
            resultDisplayableSearchItemList = addPages(displayableSearchItemList, newPageList);
        } else if(!newPersonList.isEmpty()){
            resultDisplayableSearchItemList = addPerson(displayableSearchItemList, newPersonList);
        }
        return Observable.just(new ResponseValue(resultDisplayableSearchItemList));
    }

    private List<DisplayableSearchItem> addPages(List<DisplayableSearchItem> displayableSearchItemList, List<Page> newPageList){
        List<DisplayableSearchItem> resultDisplayableSearchItemList = new ArrayList<>();
        for (DisplayableSearchItem item : displayableSearchItemList){
            if (item.getType() != VIEW_TYPE_PAGE){
                resultDisplayableSearchItemList.add(item);
            } else {
                break;
            }
        }
        for (Page page : newPageList){
            resultDisplayableSearchItemList.add(page);
        }
        return resultDisplayableSearchItemList;
    }

    private List<DisplayableSearchItem> addPerson(List<DisplayableSearchItem> displayableSearchItemList, List<Person> newPersonList){
        List<DisplayableSearchItem> resultDisplayableSearchItemList = new ArrayList<>();
        resultDisplayableSearchItemList.add(new HeaderPerson());
        for (Person person : newPersonList){
            resultDisplayableSearchItemList.add(person);
        }

        boolean isHeaderRound = false;
        for (DisplayableSearchItem item : displayableSearchItemList){
            if (item.getType() == VIEW_TYPE_PAGE_HEADER){
                isHeaderRound = true;
            }
            if(isHeaderRound){
                resultDisplayableSearchItemList.add(item);
            }
        }
        return resultDisplayableSearchItemList;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private List<DisplayableSearchItem> mDisplayableSearchItemList;
        private List<Person> mNewPersonList;
        private List<Page> mNewPageList;
        public RequestValues(List<DisplayableSearchItem> displayableSearchItemList,
                             List<Person> newPersonList,
                             List<Page> newPageList){
            mDisplayableSearchItemList = displayableSearchItemList;
            mNewPersonList = newPersonList;
            mNewPageList = newPageList;
        }
        List<DisplayableSearchItem> getDisplayableSearchItemList() {
            return mDisplayableSearchItemList;
        }
        public List<Person> getNewPersonList() {
            return mNewPersonList;
        }
        public List<Page> getNewPageList() {
            return mNewPageList;
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