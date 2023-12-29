package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommandOutput {
    protected String command;
    protected String user;
    protected int timestamp;
    protected String message;

    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", this.command);
        objectNode.put("user", this.user);
        objectNode.put("timestamp", this.timestamp);
        objectNode.put("message", this.message);
        return objectNode;
    }

    public CommandOutput(final Command command, final String message) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.message = message;
    }

    public CommandOutput() {
    }
}
