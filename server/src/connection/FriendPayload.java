package connection;

import java.io.Serializable;

public class FriendPayload implements Serializable {

    private final int id;
    private final String name;
    private final String dob;
    private final String image;

    public FriendPayload(int id, String name, String dob, String image) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getImage() {
        return image;
    }
}
