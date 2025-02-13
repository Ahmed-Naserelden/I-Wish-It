package service;

import connection.Response;
import dal.NotificationRepository;

public class NotificationService {

    public static Response get(int memberId) {
        return new Response(true, NotificationRepository.get(memberId));
    }

    public static Response delete(int id) {
        return new Response(true, NotificationRepository.delete(id));
    }
}
