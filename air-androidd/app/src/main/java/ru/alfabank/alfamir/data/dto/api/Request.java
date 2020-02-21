package ru.alfabank.alfamir.data.dto.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by U_M0WY5 on 15.08.2017.
 */

@Root(name = "request", strict = false)
public class Request {
    @Element(name = "header")
    RequestHeader header;

    @Element(name = "body")
    RequestBody body;

    int requestType;

    public Request (RequestBody body){
        this.header = new RequestHeader.Builder().build();
        this.body = body;
    }

    public Request (RequestBody body, int requestType){
        this.header = new RequestHeader.Builder().build();
        this.body = body;
        this.requestType = requestType;
    }

    public int getRequestType(){
        return requestType;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public RequestBody getBody() {
        return body;
    }
}
