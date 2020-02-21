package ru.alfabank.alfamir.data.dto.old_trash.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U_M0WY5 on 26.09.2017.
 */

public class ResponseSearch {

    private List<MSearchedPerson> peoplesearchdata;
    private List<MSearchedPage> pagesearchdata;

    public ResponseSearch(List<MSearchedPerson> peoplesearchdata, List<MSearchedPage> pagesearchdata){
        this.peoplesearchdata = peoplesearchdata;
        this.pagesearchdata = pagesearchdata;
    }

    public List<MSearchedPerson> getPeoplesearchdata() {
        if(peoplesearchdata!=null){
            return peoplesearchdata;
        } else {
            return new ArrayList<>();
        }
    }

    public List<MSearchedPage> getPagesearchdata() {
        if(pagesearchdata!=null){
            return pagesearchdata;
        } else {
            return new ArrayList<>();
        }
    }

    public void setPeoplesearchdata(List<MSearchedPerson> peoplesearchdata) {
        this.peoplesearchdata = peoplesearchdata;
    }

    public void setPagesearchdata(List<MSearchedPage> pagesearchdata) {
        this.pagesearchdata = pagesearchdata;
    }
}
