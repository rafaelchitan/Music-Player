package notifications;

import java.util.ArrayList;

public class Publisher {
    private final ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
    private NotificationTemplate notification;

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notify(final NotificationTemplate notification) {
        this.notification = notification;
        notifySubscribers();
    }

    public void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(notification);
        }
    }

    public boolean isSubscribed(Subscriber subscriber) {
        return subscribers.contains(subscriber);
    }
}
