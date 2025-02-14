package connection;

import java.io.Serializable;

/**
 * SignInPayload class to represent the data for a sign-in action.
 * Implements Serializable for object serialization.
 */
public class SignInPayload implements Serializable {

    private final String email; // Email of the user
    private final String password; // Password of the user

    /**
     * Constructs a new SignInPayload with the specified email and password.
     *
     * @param email Email of the user.
     * @param password Password of the user.
     */
    public SignInPayload(String email, String password) {
        this.email = email;
        this.password = password;
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
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }
}
