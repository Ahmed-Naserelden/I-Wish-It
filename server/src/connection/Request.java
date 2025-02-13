package connection;

import java.io.Serializable;

public class Request implements Serializable {

    private final String method;  // e.g. "GET", "POST", ...
    private final String url;     // "/auth/login"
    private final Object payload; // Request payload

    public Request(String method, String url, Object payload) {
        this.method = method;
        this.url = url;
        this.payload = payload;
    }

    public Request(String method, String url) {
        this.method = method;
        this.url = url;
        this.payload = null;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Object getPayload() {
        return payload;
    }
}
