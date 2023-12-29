package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import java.util.ArrayList;

public class StatisticsOutput extends CommandOutput {
    private final ArrayList<Entity> top;

    public StatisticsOutput(final Command command, final ArrayList<Entity> top) {
        this.top = top;
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
    }

    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", this.command);
        if (user != null) {
            objectNode.put("user", this.user);
        }
        objectNode.put("timestamp", this.timestamp);

        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (Entity result: top) {
            arrayNode.add(result.getName());
        }
        objectNode.set("result", arrayNode);
        return objectNode;
    }
}
