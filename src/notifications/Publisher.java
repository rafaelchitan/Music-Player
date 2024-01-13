package notifications;

import java.util.ArrayList;

public class Publisher {
    private final ArrayList<Subscriber> subscribers = new ArrayList<>();
    private NotificationTemplate notification;

    /**
     * Adds a subscriber to the list.
     * @param subscriber the subscriber to be added
     */
    public void subscribe(final Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Removes a subscriber from the list.
     * @param subscriber the subscriber to be removed
     */
    public void unsubscribe(final Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Notifies all subscribers with a new notification.
     * @param notification the notification to be set to publisher
     */
    public void notify(final NotificationTemplate notification) {
        this.notification = notification;
        notifySubscribers();
    }

    /**
     * Notifies all subscribers with the current notification.
     */
    public void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(notification);
        }
    }

    public boolean isSubscribed(Subscriber subscriber) {
        return subscribers.contains(subscriber);
    }
}
