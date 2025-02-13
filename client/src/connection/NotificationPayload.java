package connection;

import java.io.Serializable;
import java.sql.Timestamp;

public class NotificationPayload implements Serializable {

    private final int id;
    private final String desc;
    private final Timestamp timestamp;

    public NotificationPayload(int id, String desc, Timestamp timestamp) {
        this.id = id;
        this.desc = desc;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
