package ru.alfabank.alfamir.data.dto.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by U_M0WY5 on 18.12.2017.
 */

@Root(name = "header", strict = false)
public class RequestHeaderForPush {

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

    @Element(name = "client")
    private final String client;




    public static class Builder {
        private String reqid = "2";
        private String timestamp = "";
        private String os = "android";
        private String sessionKey = "";
        private String method = "SubscribePushTest"; // test
        //        private String method = "SubscribePush"; // real
        private String system = "ALFAMIR";
        private final String client = "AWorld 1.4.4";
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

        public RequestHeaderForPush build() {
            return new RequestHeaderForPush(this);
        }
    }

    private RequestHeaderForPush(Builder builder) {
        system = builder.system;
        method = builder.method;
        reqid = builder.reqid;
        timestamp = builder.timestamp;
        os = builder.os;
        client = builder.client;
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