package ru.alfabank.alfamir.data.dto.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by U_M0WY5 on 15.08.2017.
 */

@Root(name = "body", strict = false)
public class ResponseBody {
    @Element(name = "getData", required = false)
    private String data;

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return " getData:'" + data + "'";
    }
}
