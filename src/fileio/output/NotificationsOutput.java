package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import lombok.Getter;
import lombok.Setter;
import notifications.NotificationTemplate;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class NotificationsOutput extends CommandOutput {
    private List<NotificationTemplate> results;

    public NotificationsOutput(final Command command, final ArrayList<NotificationTemplate> results) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.results = results;
    }

    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", command);
        objectNode.put("user", user);
        objectNode.put("timestamp", timestamp);

        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (NotificationTemplate result: results) {
            arrayNode.add(result.toJSON());
        }
        objectNode.set("notifications", arrayNode);
        return objectNode;
    }
}
