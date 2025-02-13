package connection;

import java.io.Serializable;

public class SignInPayload implements Serializable {

    private final String email;
    private final String password;

    public SignInPayload(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
