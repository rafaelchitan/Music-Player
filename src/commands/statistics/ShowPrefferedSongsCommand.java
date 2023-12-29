package commands.statistics;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.StatisticsOutput;

import java.util.ArrayList;

public class ShowPrefferedSongsCommand extends Command {
    public ShowPrefferedSongsCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the command to get the top liked songs for the given user.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        return new StatisticsOutput(this, new ArrayList<>(user.getLikedSongs())).convertToJSON();
    }
}
