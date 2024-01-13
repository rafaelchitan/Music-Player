package commands.monetization;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class AdBreakCommand extends Command {
    private final int price;
    public AdBreakCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.price = commandInput.getPrice();
    }

    /**
     * Executes the AdBreak Command and returns the result.
     * @return JSON ObjectNode containing the result
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user == null) {
            return new CommandOutput(this,
                    "The username " + username + " doesn't exist.").convertToJSON();
        }

        user.getPlayer().update(user, timestamp);
        if (user.getPlayer().getPlayStatus() == null
                || !user.getPlayer().getPlayStatus().equals("playing")) {
            return new CommandOutput(this,
                    username + " is not playing any music.").convertToJSON();
        }

        user.getPlayer().setAdBreak(user, price, timestamp);
        return new CommandOutput(this, "Ad inserted successfully.").convertToJSON();
    }
}
