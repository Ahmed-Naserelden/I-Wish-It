package connection;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * NotificationPayload class to represent the data for a notification.
 * Implements Serializable for object serialization.
 */
public class NotificationPayload implements Serializable {

    private final int id; // ID of the notification
    private final String desc; // Description of the notification
    private final Timestamp timestamp; // Timestamp of the notification

    /**
     * Constructs a new NotificationPayload with the specified details.
     *
     * @param id ID of the notification.
     * @param desc Description of the notification.
     * @param timestamp Timestamp of the notification.
     */
    public NotificationPayload(int id, String desc, Timestamp timestamp) {
        this.id = id;
        this.desc = desc;
        this.timestamp = timestamp;
    }

    /**
     * Gets the ID of the notification.
     *
     * @return The ID of the notification.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the description of the notification.
     *
     * @return The description of the notification.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the timestamp of the notification.
     *
     * @return The timestamp of the notification.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
