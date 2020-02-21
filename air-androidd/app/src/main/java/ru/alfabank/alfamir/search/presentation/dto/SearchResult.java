package ru.alfabank.alfamir.search.presentation.dto;

import java.util.List;

public class SearchResult {
    private List<Person> mPersonList;
    private List<Page> mPageList;

    private SearchResult(List<Person> personList,
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

    public static class Builder {
        private List<Person> mPersonList;
        private List<Page> mPageList;

        public Builder personList(List<Person> personList){
            mPersonList = personList;
            return this;
        }

        public Builder pageList(List<Page> pageList){
            mPageList = pageList;
            return this;
        }

        public SearchResult build(){
            return new SearchResult(mPersonList, mPageList);
        }
    }

}
