package connection;

import java.io.Serializable;

/**
 * FriendActionPayload class to represent the data for a friend action.
 * Implements Serializable for object serialization.
 */
public class FriendActionPayload implements Serializable {

    private final int memberId; // ID of the member performing the action
    private int friendId; // ID of the friend involved in the action
    private String friendEmail; // Email of the friend involved in the action

    /**
     * Constructs a new FriendActionPayload with the specified member ID and friend ID.
     *
     * @param memberId ID of the member performing the action.
     * @param friendId ID of the friend involved in the action.
     */
    public FriendActionPayload(int memberId, int friendId) {
        this.memberId = memberId;
        this.friendId = friendId;
    }

    /**
     * Constructs a new FriendActionPayload with the specified member ID and friend email.
     *
     * @param memberId ID of the member performing the action.
     * @param friendEmail Email of the friend involved in the action.
     */
    public FriendActionPayload(int memberId, String friendEmail) {
        this.memberId = memberId;
        this.friendEmail = friendEmail;
    }

    /**
     * Gets the ID of the member performing the action.
     *
     * @return The ID of the member performing the action.
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Gets the ID of the friend involved in the action.
     *
     * @return The ID of the friend involved in the action.
     */
    public int getFriendId() {
        return friendId;
    }

    /**
     * Gets the email of the friend involved in the action.
     *
     * @return The email of the friend involved in the action.
     */
    public String getFriendEmail() {
        return friendEmail;
    }
}
