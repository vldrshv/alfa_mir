package ru.alfabank.alfamir.ui.adapters.data_wrappers;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.dto.old_trash.models.MSearchedPage;
import ru.alfabank.alfamir.data.dto.old_trash.models.MSearchedPerson;
import ru.alfabank.alfamir.data.dto.old_trash.models.ResponseSearch;

/**
 * Created by U_M0WY5 on 27.09.2017.
 */

public class SearchResultWrapper {

    private ResponseSearch searchResults;
    private List<MSearchedPerson> personsList;
    private List<MSearchedPage> pagesList;
    private int personListSize;
    private int pagesListSize;
    private List<ResultElement> list;
    private int listSize = 0;
    private String request;
    private boolean morePersons;
    private boolean morePages;
    private boolean noPersons;


    public SearchResultWrapper(ResponseSearch searchResults, boolean morePersons, boolean morePages) {
        this.searchResults = searchResults;
        personsList = searchResults.getPeoplesearchdata();
        pagesList = searchResults.getPagesearchdata();
        list = new ArrayList<>();
        this.morePersons = morePersons;
        this.morePages = morePages;
        prepareList();
    }

    public SearchResultWrapper(boolean noPersons, ResponseSearch searchResults, boolean morePages) {
        this.searchResults = searchResults;
        personsList = searchResults.getPeoplesearchdata();
        pagesList = searchResults.getPagesearchdata();
        list = new ArrayList<>();
        this.noPersons = noPersons;
        this.morePages = morePages;
        prepareList();
    }

    public List<MSearchedPerson> getPersonsList() {
        return personsList;
    }

    public List<MSearchedPage> getPagesList() {
        return pagesList;
    }

    private void prepareList() {
        if (!noPersons) {
            list.add(listSize, new ResultElement(null, null, "КОЛЛЕГИ", Constants.Search.VIEW_TYPE_HEADER));
            listSize++;


            if (searchResults.getPeoplesearchdata() != null && searchResults.getPeoplesearchdata().size() != 0) {
                for (int i = 0; i < searchResults.getPeoplesearchdata().size(); i++) {
                    list.add(listSize, new ResultElement(searchResults.getPeoplesearchdata().get(i), null, null, Constants.Search.VIEW_TYPE_PERSON));
                    listSize++;
                }

                if (searchResults.getPeoplesearchdata().size() == 4 && !morePersons) {
                    list.remove(list.size() - 1);
                    listSize--;
                    list.add(listSize, new ResultElement(null, null, "Ещё коллеги ...", Constants.Search.VIEW_TYPE_FOOTER));
                    listSize++;
                }

            } else {

                list.add(listSize, new ResultElement(null, null, null, Constants.Search.VIEW_TYPE_EMPTY));
                listSize++;

            }
        }

        list.add(listSize, new ResultElement(null, null, "СТАТЬИ", Constants.Search.VIEW_TYPE_HEADER));
        listSize++;

        int limit;
        if (!noPersons) {
            limit = 4;
        } else {
            limit = 7;
        }

        if (searchResults.getPagesearchdata() != null && searchResults.getPagesearchdata().size() != 0) {
            for (int i = 0; i < searchResults.getPagesearchdata().size(); i++) {
                list.add(listSize, new ResultElement(null, searchResults.getPagesearchdata().get(i), null, Constants.Search.VIEW_TYPE_PAGE));
                listSize++;
            }
            if (searchResults.getPagesearchdata().size() == limit && !morePages) {
                list.remove(list.size() - 1);
                listSize--;
                list.add(listSize, new ResultElement(null, null, "Ещё результаты ...", Constants.Search.VIEW_TYPE_FOOTER));
                listSize++;
            }
        } else {

            list.add(listSize, new ResultElement(null, null, null, Constants.Search.VIEW_TYPE_EMPTY));
            listSize++;

        }

    }

    public int size() {
        return list.size();
    }

    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    public MSearchedPerson getPerson(int position) {
        return list.get(position).person;
    }

    public MSearchedPage getPage(int position) {
        return list.get(position).page;
    }

    public String getText(int position) {
        return list.get(position).text;
    }

    private class ResultElement {
        MSearchedPerson person;
        MSearchedPage page;
        String text;
        int type;

        ResultElement(MSearchedPerson person, MSearchedPage page, String text, int type) {
            this.person = person;
            this.page = page;
            this.type = type;
            this.text = text;
        }
    }

}
