package notifications;

public interface Subscriber {

    /**
     * Updates the subscriber with a new notification.
     * @param notification the notification to be added to the list
     */
    void update(NotificationTemplate notification);
}
