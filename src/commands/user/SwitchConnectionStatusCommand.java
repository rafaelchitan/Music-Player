package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class SwitchConnectionStatusCommand extends Command {

    public SwitchConnectionStatusCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
    }

    /**
     * Changes the status of a user - online/offline
     * @return the JSON representation of the output message
     */
    public ObjectNode execute() {
        String message;
        User user = Library.getInstance().getUserByName(username);
        if (user == null) {
            message = "The username " + username + " does not exist.";
            return new CommandOutput(this, message).convertToJSON();
        }

        if (!user.getType().equals("user")) {
            message = username + " is not a normal user.";
            return new CommandOutput(this, message).convertToJSON();
        }

        message = username + " has changed status successfully.";
        if (user.getStatus().equals("offline")) {
            user.getPlayer().update(user, timestamp);
            user.getPlayer().getStats().setOffline(false);
            user.setOnline(true);
            user.setStatus("online");
        } else if (Library.getInstance().getUserByName(username).getStatus().equals("online")) {
            user.getPlayer().update(user, timestamp);
            user.getPlayer().getStats().setOffline(true);
            user.setOnline(false);
            user.setStatus("offline");
        }
        return new CommandOutput(this, message).convertToJSON();
    }
}
