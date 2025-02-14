package connection;

import java.io.Serializable;

/**
 * FriendPayload class to represent the data for a friend.
 * Implements Serializable for object serialization.
 */
public class FriendPayload implements Serializable {

    private final int id; // ID of the friend
    private final String name; // Name of the friend
    private final String dob; // Date of birth of the friend
    private final String image; // Image URL of the friend

    /**
     * Constructs a new FriendPayload with the specified details.
     *
     * @param id ID of the friend.
     * @param name Name of the friend.
     * @param dob Date of birth of the friend.
     * @param image Image URL of the friend.
     */
    public FriendPayload(int id, String name, String dob, String image) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.image = image;
    }

    /**
     * Gets the ID of the friend.
     *
     * @return The ID of the friend.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the friend.
     *
     * @return The name of the friend.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the date of birth of the friend.
     *
     * @return The date of birth of the friend.
     */
    public String getDob() {
        return dob;
    }

    /**
     * Gets the image URL of the friend.
     *
     * @return The image URL of the friend.
     */
    public String getImage() {
        return image;
    }
}
