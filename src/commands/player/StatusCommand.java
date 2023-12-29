package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.StatusOutput;

public class StatusCommand extends Command {
    public StatusCommand(final CommandInput commandInput) {
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();

    }

    /**
     * Executes the status command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        user.getPlayer().update(user, timestamp);
        return new StatusOutput(this, user.getPlayer().getStats()).convertToJSON();
    }
}
