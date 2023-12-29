package commands.monetization;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class BuyPremiumCommand extends Command {
    public BuyPremiumCommand(CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user == null) {
            return new CommandOutput(this, "The username " + username + " doesn't exist").convertToJSON();
        }

        if (user.isPremium()) {
            return new CommandOutput(this, username + " is already a premium user.").convertToJSON();
        }

        user.setPremium(true);
        return new CommandOutput(this, username + " bought the subscription successfully.").convertToJSON();
    }
}
