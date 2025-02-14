package service;

import connection.NotificationPayload;
import connection.Response;
import dal.NotificationRepository;
import java.util.ArrayList;

/**
 * NotificationService class to handle notification-related operations.
 * Provides methods for managing notifications.
 */
public class NotificationService {

    /**
     * Retrieves the list of notifications for a given member.
     *
     * @param memberId The ID of the member.
     * @return A Response object containing the list of notifications.
     */
    public static Response getNotifications(int memberId) {
        ArrayList<NotificationPayload> notifications = NotificationRepository.get(memberId);
        return new Response(true, notifications);
    }

    /**
     * Deletes a notification by its ID.
     *
     * @param notificationId The ID of the notification to delete.
     * @return A Response object containing the status of the deletion.
     */
    public static Response deleteNotification(int notificationId) {
        boolean status = NotificationRepository.delete(notificationId);
        return new Response(status);
    }
}
