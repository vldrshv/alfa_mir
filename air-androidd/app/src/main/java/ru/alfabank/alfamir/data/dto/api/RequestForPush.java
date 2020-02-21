package ru.alfabank.alfamir.data.dto.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by U_M0WY5 on 18.12.2017.
 */

@Root(name = "request", strict = false)
public class RequestForPush {
    @Element(name = "header")
    RequestHeaderForPush header;

    @Element(name = "body")
    RequestBodyForPush body;

    public RequestForPush (RequestBodyForPush body){
        this.header = new RequestHeaderForPush.Builder().build();
        this.body = body;
    }

    public RequestHeaderForPush getHeader() {
        return header;
    }

    public RequestBodyForPush getBody() {
        return body;
    }
}
