package connection;

import java.io.Serializable;

/**
 * SignUpPayload class to represent the data for a sign-up action.
 * Implements Serializable for object serialization.
 */
public class SignUpPayload implements Serializable {

    private final String email; // Email of the user
    private final String username; // Username of the user
    private final String password; // Password of the user
    private final String dob; // Date of birth of the user

    /**
     * Constructs a new SignUpPayload with the specified email, username, password, and date of birth.
     *
     * @param email Email of the user.
     * @param username Username of the user.
     * @param password Password of the user.
     * @param dob Date of birth of the user.
     */
    public SignUpPayload(String email, String username, String password, String dob) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.dob = dob;
    }

    /**
     * Gets the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the date of birth of the user.
     *
     * @return The date of birth of the user.
     */
    public String getDob() {
        return dob;
    }
}
