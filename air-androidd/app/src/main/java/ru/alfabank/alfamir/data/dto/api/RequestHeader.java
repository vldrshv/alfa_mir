package ru.alfabank.alfamir.data.dto.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ru.alfabank.alfamir.Constants;

/**
 * Created by U_M0WY5 on 15.08.2017.
 */
@Root(name = "header", strict = false)
public class RequestHeader {

    @Element(name = "reqid")
    private final String reqid;

    @Element(name = "timestamp")
    private final String timestamp;

    @Element(name = "system")
    private final String system;

    @Element(name = "method")
    private final String method;

    @Element(name = "os")
    private final String os;

    @Element(name = "session_key")
    private final String sessionKey;

    public static class Builder {
        private String reqid = "";
        private String timestamp = "";
        private String os = "android";
        private String sessionKey = "";
        private String method = Constants.Companion.getREQUEST_HEADER();
        private String system = "ALFAMIR";

        public Builder(){}

        public Builder reqid(String val) {
            reqid = val;
            return this;
        }

        public Builder timestamp(String val) {
            timestamp = val;
            return this;
        }

        public Builder os(String val) {
            os = val;
            return this;
        }

        public Builder sessionKey(String val) {
            sessionKey = val;
            return this;
        }

        public RequestHeader build() {
            return new RequestHeader(this);
        }
    }

    private RequestHeader(Builder builder) {
        system = builder.system;
        method = builder.method;
        reqid = builder.reqid;
        timestamp = builder.timestamp;
        os = builder.os;
        sessionKey = builder.sessionKey;
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "reqid='" + reqid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", system='" + system + '\'' +
                ", method='" + method + '\'' +
                ", os='" + os + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                '}';
    }

}
