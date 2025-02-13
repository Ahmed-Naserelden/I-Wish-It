package connection;

import java.io.Serializable;

public class MemberPayload implements Serializable {

    private final int id;
    private final String email;
    private final String username;
    private final String password;
    private final String dob;
    private final String image;

    public MemberPayload(int id, String email, String username, String password, String dob, String image) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.image = image;
    }

    public int getId() {
        return id;
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

    public String getImage() {
        return image;
    }
}
