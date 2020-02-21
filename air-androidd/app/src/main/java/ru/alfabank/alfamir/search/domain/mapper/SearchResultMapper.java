package ru.alfabank.alfamir.search.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.search.data.dto.PageRaw;
import ru.alfabank.alfamir.search.data.dto.PersonRaw;
import ru.alfabank.alfamir.search.data.dto.SearchResultRaw;
import ru.alfabank.alfamir.search.presentation.dto.Page;
import ru.alfabank.alfamir.search.presentation.dto.Person;
import ru.alfabank.alfamir.search.presentation.dto.SearchResult;

public class SearchResultMapper implements Function<SearchResultRaw, SearchResult> {

    private PageMapper mPageMapper;
    private PersonMapper mPersonMapper;

    @Inject
    public SearchResultMapper(PageMapper pageMapper,
                              PersonMapper personMapper){
        mPageMapper = pageMapper;
        mPersonMapper = personMapper;
    }

    @Override
    public SearchResult apply(SearchResultRaw searchResultRaw) throws Exception {
        PageRaw[] pageRaw = searchResultRaw.getPages();
        PersonRaw[] personRaw = searchResultRaw.getPersons();
        List<Page> pageList =  new ArrayList<>();
        List<Person> personList = new ArrayList<>();
        if(pageRaw!=null){
            if(pageRaw.length > 0){
                for (int i = 0; i < pageRaw.length; i++){
                    Page page = mPageMapper.apply(pageRaw[i]);
                    pageList.add(page);
                }
            }
        }
        if(personRaw!=null){
            if(personRaw.length > 0){
                for (int i = 0; i < personRaw.length; i++){
                    Person page = mPersonMapper.apply(personRaw[i]);
                    personList.add(page);
                }
            }
        }
        SearchResult searchResult = new SearchResult.Builder()
                .pageList(pageList)
                .personList(personList)
                .build();
        return searchResult;
    }

}