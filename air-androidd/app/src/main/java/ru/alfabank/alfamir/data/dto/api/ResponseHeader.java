package ru.alfabank.alfamir.data.dto.api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "header", strict = false)
public class ResponseHeader {
    @Element(name = "reqid", required = false)
    public String reqid;

    @Element(name = "timestamp", required = false)
    public String timestamp;

    @Element(name = "client", required = false)
    public String client;

    @Element(name = "os", required = false)
    public String os;

    @Element(name = "system")
    public String system;

    @Element(name = "ip", required = false)
    public String ip;

    @Element(name = "model", required = false)
    public String model;

    @Element(name = "method")
    public String method;

    @Element(name = "securedSessionKey", required = false)
    public String securedSessionKey;

    @Element(name = "result", required = false)
    public int result;

    @Element(name = "description", required = false)
    public String description;

    @Override
    public String toString() {
        return "ResponseHeader:" + "reqid:'" + reqid + "', timestamp:'" + timestamp
                + "', client:'" + client + "', os:'" + os + "', system:'" + system
                + "', ip:'" + ip +  "', model:'" + model + "', method:'" + method
                +  "', securedSessionKey:'" + securedSessionKey + "', result:'" + result
                + "', description:'" + description + "'";
    }
}
