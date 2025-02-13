package connection;

import java.io.Serializable;

public class SignUpPayload implements Serializable {

    private final String email;
    private final String username;
    private final String password;
    private final String dob;

    public SignUpPayload(String email, String username, String password, String dob) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }
}
