package connection;

import java.io.Serializable;

/**
 * Response class to represent the response from a server.
 * Implements Serializable for object serialization.
 */
public class Response implements Serializable {

    private final boolean status; // true or false
    private final Object payload; // Result or error message

    /**
     * Constructs a new Response with the specified status.
     * The payload is set to null.
     *
     * @param status The status of the response (true or false).
     */
    public Response(boolean status) {
        this.status = status;
        this.payload = null;
    }

    /**
     * Constructs a new Response with the specified status and payload.
     *
     * @param status The status of the response (true or false).
     * @param payload The result or error message.
     */
    public Response(boolean status, Object payload) {
        this.status = status;
        this.payload = payload;
    }

    /**
     * Checks if the response status is passed (true).
     *
     * @return True if the status is passed, false otherwise.
     */
    public boolean isPassed() {
        return status;
    }

    /**
     * Gets the payload of the response.
     *
     * @return The payload of the response.
     */
    public Object getPayload() {
        return payload;
    }
}
