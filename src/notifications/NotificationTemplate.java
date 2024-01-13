package notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NotificationTemplate {
    private final String name;
    private final String description;

    public NotificationTemplate(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Converts the notification to a JSON ObjectNode.
     * @return JSON ObjectNode containing the notification
     */
    public ObjectNode toJSON() {
        ObjectNode notification = new ObjectMapper().createObjectNode();
        notification.put("name", name);
        notification.put("description", description);
        return notification;
    }
}
