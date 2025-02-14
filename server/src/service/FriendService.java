package service;

import connection.FriendActionPayload;
import connection.FriendPayload;
import connection.Response;
import dal.FriendRepository;
import java.util.ArrayList;

/**
 * FriendService class to handle friend-related operations.
 * Provides methods for managing friends and friend requests.
 */
public class FriendService {

    /**
     * Retrieves the list of friends for a given member.
     *
     * @param memberId The ID of the member.
     * @return A Response object containing the list of friends.
     */
    public static Response getFriends(int memberId) {
        ArrayList<FriendPayload> friends = FriendRepository.getFriends(memberId);
        return new Response(true, friends);
    }

    /**
     * Sends a friend request from one member to another.
     *
     * @param memberId The ID of the member sending the request.
     * @param friendId The ID of the member receiving the request.
     * @return A Response object containing the status of the friend request.
     */
    public static Response sendFriendRequest(int memberId, int friendId) {
        boolean status = FriendRepository.sendFriendRequest(memberId, friendId);
        return new Response(status);
    }

    /**
     * Accepts a friend request between two members.
     *
     * @param memberId The ID of the member accepting the request.
     * @param friendId The ID of the member whose request is being accepted.
     * @return A Response object containing the status of the acceptance.
     */
    public static Response acceptFriendRequest(int memberId, int friendId) {
        boolean status = FriendRepository.acceptFriendRequest(memberId, friendId);
        return new Response(status);
    }

    /**
     * Declines a friend request between two members.
     *
     * @param memberId The ID of the member declining the request.
     * @param friendId The ID of the member whose request is being declined.
     * @return A Response object containing the status of the decline.
     */
    public static Response declineFriendRequest(int memberId, int friendId) {
        boolean status = FriendRepository.declineFriendRequest(memberId, friendId);
        return new Response(status);
    }

    /**
     * Removes a friend relationship between two members.
     *
     * @param memberId The ID of the member removing the friend.
     * @param friendId The ID of the friend being removed.
     * @return A Response object containing the status of the removal.
     */
    public static Response removeFriend(int memberId, int friendId) {
        boolean status = FriendRepository.removeFriend(memberId, friendId);
        return new Response(status);
    }

    /**
     * Sends a friend request from one member to another using email.
     *
     * @param payload The payload containing member ID and friend email.
     * @return A Response object containing the status of the friend request.
     */
    public static Response sendFriendRequest(FriendActionPayload payload) {
        int friendId = FriendRepository.getMemberIdByEmail(payload.getFriendEmail());

        if (friendId == 0) {
            return new Response(false, "Cannot find email address!");
        }
        if (FriendRepository.sendFriendRequest(payload.getMemberId(), friendId)) {
            return new Response(true, "Friend request sent!");
        }
        return new Response(false, "Friend request already sent!");
    }

    /**
     * Retrieves the list of friend requests for a given member.
     *
     * @param memberId The ID of the member.
     * @return A Response object containing the list of friend requests.
     */
    public static Response getFriendRequests(int memberId) {
        return new Response(true, FriendRepository.getFriendRequests(memberId));
    }

    /**
     * Accepts a friend request between two members using payload.
     *
     * @param payload The payload containing member ID and friend ID.
     * @return A Response object containing the status of the acceptance.
     */
    public static Response acceptFriendRequest(FriendActionPayload payload) {
        boolean status = FriendRepository.acceptFriendRequest(
                payload.getMemberId(),
                payload.getFriendId()
        );
        return new Response(status);
    }

    /**
     * Declines a friend request between two members using payload.
     *
     * @param payload The payload containing member ID and friend ID.
     * @return A Response object containing the status of the decline.
     */
    public static Response declineFriendRequest(FriendActionPayload payload) {
        boolean status = FriendRepository.declineFriendRequest(
                payload.getMemberId(),
                payload.getFriendId()
        );
        return new Response(status);
    }
}
