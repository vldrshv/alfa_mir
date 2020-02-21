package ru.alfabank.alfamir.data.dto.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by U_M0WY5 on 18.12.2017.
 */

@Root(name = "body", strict = false)
public class RequestBodyForPush {
    @Element
    private String token;

    public RequestBodyForPush(String data){
        this.token = data;
    }

    public String getData() {
        return token;
    }

    public void setData(String data) {
        this.token = data;
    }
}