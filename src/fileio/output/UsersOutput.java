package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.users.User;

import java.util.ArrayList;

public class UsersOutput extends CommandOutput {
    private final ArrayList<User> onlineUsers;
    public UsersOutput(final Command command, final ArrayList<User> onlineUsers) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.onlineUsers = onlineUsers;
    }

    /**
     * Converts the command result to JSON format
     * @return the JSON result
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", this.command);
        objectNode.put("timestamp", this.timestamp);

        ArrayNode onlineUsersArrayNode = new ObjectMapper().createArrayNode();
        for (User user: onlineUsers) {
            onlineUsersArrayNode.add(user.getUsername());
        }

        objectNode.set("result", onlineUsersArrayNode);
        return objectNode;
    }

}
