package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.player.PlayerStats;

public class StatusOutput extends CommandOutput {
    private final PlayerStats stats;
    public StatusOutput(final Command command, final PlayerStats stats) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.stats = stats;
    }

    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", this.command);
        objectNode.put("user", this.user);
        objectNode.put("timestamp", this.timestamp);
        objectNode.set("stats", stats.convertToJSON());
        return objectNode;
    }
}
