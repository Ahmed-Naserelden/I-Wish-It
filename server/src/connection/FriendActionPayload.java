package connection;

import java.io.Serializable;

public class FriendActionPayload implements Serializable {

    private final int memberId;
    private int friendId;
    private String friendEmail;

    public FriendActionPayload(int memberId, int friendId) {
        this.memberId = memberId;
        this.friendId = friendId;
    }

    public FriendActionPayload(int memberId, String friendEmail) {
        this.memberId = memberId;
        this.friendEmail = friendEmail;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getFriendId() {
        return friendId;
    }

    public String getFriendEmail() {
        return friendEmail;
    }
}
