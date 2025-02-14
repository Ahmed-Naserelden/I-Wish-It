package connection;

import java.io.Serializable;

/**
 * MemberPayload class to represent the data for a member.
 * Implements Serializable for object serialization.
 */
public class MemberPayload implements Serializable {

    private final int id; // ID of the member
    private final String email; // Email of the member
    private final String username; // Username of the member
    private final String password; // Password of the member
    private final String dob; // Date of birth of the member
    private final String image; // Image URL of the member

    /**
     * Constructs a new MemberPayload with the specified details.
     *
     * @param id ID of the member.
     * @param email Email of the member.
     * @param username Username of the member.
     * @param password Password of the member.
     * @param dob Date of birth of the member.
     * @param image Image URL of the member.
     */
    public MemberPayload(int id, String email, String username, String password, String dob, String image) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.image = image;
    }

    /**
     * Gets the ID of the member.
     *
     * @return The ID of the member.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the email of the member.
     *
     * @return The email of the member.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the username of the member.
     *
     * @return The username of the member.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the member.
     *
     * @return The password of the member.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the date of birth of the member.
     *
     * @return The date of birth of the member.
     */
    public String getDob() {
        return dob;
    }

    /**
     * Gets the image URL of the member.
     *
     * @return The image URL of the member.
     */
    public String getImage() {
        return image;
    }
}
