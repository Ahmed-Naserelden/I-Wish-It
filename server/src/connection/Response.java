package connection;

import java.io.Serializable;

public class Response implements Serializable {

    private final boolean status; // true or false
    private final Object payload; // Result or error message

    public Response(boolean status) {
        this.status = status;
        this.payload = null;
    }

    public Response(boolean status, Object payload) {
        this.status = status;
        this.payload = payload;
    }

    public boolean isPassed() {
        return status;
    }

    public Object getPayload() {
        return payload;
    }
}
