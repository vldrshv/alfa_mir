package ru.alfabank.alfamir.data.source.repositories.old_trash;

import java.util.List;

import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.utility.callbacks.SearchResultRequester;
import ru.alfabank.alfamir.utility.callbacks.Searcher;
import ru.alfabank.alfamir.data.dto.old_trash.models.MSearchedPage;
import ru.alfabank.alfamir.data.dto.old_trash.models.MSearchedPerson;
import ru.alfabank.alfamir.data.dto.old_trash.models.ResponseSearch;
import ru.alfabank.alfamir.ui.adapters.data_wrappers.SearchResultWrapper;

/**
 * Created by U_M0WY5 on 27.09.2017.
 */

public class SearchResultRepository implements SearchResultRequester{

    private App app;
    private String keyWord;
    private ResponseSearch lastResult;
    private boolean morePersons;
    private boolean morePages;
    private boolean noPersons;

    public SearchResultRepository (App app) {
        this.app = app;
    }

    public boolean isMorePersons() {
        return morePersons;
    }

    public boolean isMorePages() {
        return morePages;
    }

    public void search(Searcher callback, String keyWord, boolean morePersons, boolean morePages){
        if(!morePersons&&!morePages){
            this.morePersons = morePersons;
            this.morePages = morePages;
            app.getDProvider().search(this, callback, keyWord, 4, 4);
        } else if (morePersons) {
            this.morePersons = morePersons;
            app.getDProvider().search(this, callback, keyWord, 50, 0);
        } else if (morePages) {
            this.morePages = morePages;
            app.getDProvider().search(this, callback, keyWord, 0, 50);
        }
    }

    public void searchCommunities(Searcher callback, String keyWord, boolean noPersons, boolean morePages){
        if(!morePages){
            this.noPersons = noPersons;
            this.morePages = morePages;
            app.getDProvider().searchCommunities(this, callback, keyWord, 0, 7);
        } else {
            this.noPersons = noPersons;
            this.morePages = morePages;
            app.getDProvider().searchCommunities(this, callback, keyWord, 0, 50);
        }
    }

//    public void advancedSearch(AdvancedSearcher callback, String keyWord){
//        app.getDProvider().search(this, callback, keyWord);
//        this.keyWord = keyWord;
//    }


    @Override
    public void onResultReceived(Searcher callBack, ResponseSearch searchResults, String keyWord) {
        if(!noPersons){
            SearchResultWrapper result;
            if(lastResult!=null&&this.keyWord == keyWord){
                ResponseSearch merged = mergeResults(lastResult, searchResults);
                result = new SearchResultWrapper(merged, morePersons, morePages);
            } else {
                result = new SearchResultWrapper(searchResults, morePersons, morePages);
            }

            this.keyWord = keyWord;
            lastResult = searchResults;

            if(!morePersons&&!morePages){
                callBack.onSearched(result, false);
            } else {
                callBack.onSearched(result, true);
            }
        } else {
            SearchResultWrapper result;
            if(lastResult!=null&&this.keyWord == keyWord){
                ResponseSearch merged = mergeResults(lastResult, searchResults);
                result = new SearchResultWrapper(noPersons, merged, morePages);
            } else {
                result = new SearchResultWrapper(noPersons, searchResults, morePages);
            }

            this.keyWord = keyWord;
            lastResult = searchResults;

            if(!morePersons&&!morePages){
                callBack.onSearched(result, false);
            } else {
                callBack.onSearched(result, true);
            }
        }


    }

    public String getKeyWord(){
        return keyWord;
    }

    private ResponseSearch mergeResults(ResponseSearch mOld, ResponseSearch mNew){
        List<MSearchedPerson> persons;
        List<MSearchedPage> pages;
//        if(mNew.getPeoplesearchdata()==null){
//            mNew.peoplesearchdata = new ArrayList<>();
//        }
//        if(mNew.getPagesearchdata()==null){
//            mNew.pagesearchdata = new ArrayList<>();
//        }

        if(mOld.getPeoplesearchdata().size() <= mNew.getPeoplesearchdata().size()){
            persons = mNew.getPeoplesearchdata();
        } else {
            persons = mOld.getPeoplesearchdata();
        }

        if(mOld.getPagesearchdata().size() <= mNew.getPagesearchdata().size()){
            pages = mNew.getPagesearchdata();
//            for (int i = pages.size(); i>4;i--){
//                pages.remove(i-1);
//            }
        } else {
            pages = mOld.getPagesearchdata();
        }

        ResponseSearch result = new ResponseSearch(persons, pages);
        return result;
    }

}
