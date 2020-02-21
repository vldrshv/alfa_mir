package ru.alfabank.alfamir.data.dto.api;

import androidx.annotation.NonNull;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by U_M0WY5 on 15.08.2017.
 */
@Root(name = "response", strict = false)
public class Response {

    @Element(name = "header")
    public ResponseHeader header;

    @Element(name = "body")
    public ResponseBody body;

    public ResponseHeader getHeader() {
        return header;
    }

    public ResponseBody getBody() {
        return body;
    }

    @NonNull
    @Override
    public String toString() {

        String header = this.header == null ? "Empty header" : this.header.toString();
        String body = this.body == null ? "Empty body" : this.body.toString();

        return String.format("Response: header:'%s', body:'%s'", header, body);
    }
}
