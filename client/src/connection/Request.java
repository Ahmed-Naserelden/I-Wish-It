package connection;

import java.io.Serializable;

/**
 * Request class to represent an HTTP request.
 * Implements Serializable for object serialization.
 */
public class Request implements Serializable {

    private final String method;  // HTTP method (e.g., "GET", "POST", ...)
    private final String url;     // URL endpoint (e.g., "/auth/login")
    private final Object payload; // Request payload

    /**
     * Constructs a new Request with the specified method, URL, and payload.
     *
     * @param method HTTP method (e.g., "GET", "POST").
     * @param url URL endpoint (e.g., "/auth/login").
     * @param payload Request payload.
     */
    public Request(String method, String url, Object payload) {
        this.method = method;
        this.url = url;
        this.payload = payload;
    }

    /**
     * Constructs a new Request with the specified method and URL.
     * The payload is set to null.
     *
     * @param method HTTP method (e.g., "GET", "POST").
     * @param url URL endpoint (e.g., "/auth/login").
     */
    public Request(String method, String url) {
        this.method = method;
        this.url = url;
        this.payload = null;
    }

    /**
     * Gets the HTTP method of the request.
     *
     * @return The HTTP method of the request.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Gets the URL endpoint of the request.
     *
     * @return The URL endpoint of the request.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the payload of the request.
     *
     * @return The payload of the request.
     */
    public Object getPayload() {
        return payload;
    }
}
