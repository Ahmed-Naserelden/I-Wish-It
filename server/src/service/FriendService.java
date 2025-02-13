package service;

import connection.FriendActionPayload;
import connection.FriendPayload;
import connection.Response;
import dal.FriendRepository;
import java.util.ArrayList;

public class FriendService {

    public static Response getFriends(int memberId) {
        ArrayList<FriendPayload> friends = FriendRepository.getFriends(memberId);
        return new Response(true, friends);
    }

    public static Response removeFriend(FriendActionPayload payload) {
        boolean status = FriendRepository.removeFriend(
                payload.getMemberId(),
                payload.getFriendId()
        );
        return new Response(status);
    }

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

    public static Response getFriendRequests(int memberId) {
        return new Response(true, FriendRepository.getFriendRequests(memberId));
    }

    public static Response acceptFriendRequest(FriendActionPayload payload) {
        boolean status = FriendRepository.acceptFriendRequest(
                payload.getMemberId(),
                payload.getFriendId()
        );
        return new Response(status);
    }

    public static Response declineFriendRequest(FriendActionPayload payload) {
        boolean status = FriendRepository.declineFriendRequest(
                payload.getMemberId(),
                payload.getFriendId()
        );
        return new Response(status);
    }
}
