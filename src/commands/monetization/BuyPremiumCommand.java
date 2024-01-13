package commands.monetization;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class BuyPremiumCommand extends Command {
    public BuyPremiumCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the BuyPremium Command and returns the result.
     * @return JSON ObjectNode containing the result
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user == null) {
            return new CommandOutput(this,
                    "The username "
                            + username
                            + " doesn't exist").convertToJSON();
        }

        if (user.isPremium()) {
            return new CommandOutput(this,
                    username + " is already a premium user.").convertToJSON();
        }

        user.setPremium(true);
        user.setPremiumStart(timestamp);
        return new CommandOutput(this,
                username + " bought the subscription successfully.").convertToJSON();
    }
}
