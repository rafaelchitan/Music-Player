package commands.notifications;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class SubscribeCommand extends Command {
    public SubscribeCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the Subscribe Command and returns the result.
     * @return JSON ObjectNode containing the result
     */
    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user == null) {
            return new CommandOutput(this,
                    "The username " + username + " doesn't exist").convertToJSON();
        }

        User pageUser = user.getCurrentPage().getUser();
        if (pageUser == null || pageUser.getType().equals("user")) {
            return new CommandOutput(this,
                    "To subscribe you need to be on the page of an artist or host.")
                    .convertToJSON();
        }

        if (pageUser.getPublisher().isSubscribed(user)) {
            pageUser.getPublisher().unsubscribe(user);
            return new CommandOutput(this,
                    username + " unsubscribed from " + pageUser.getUsername() + " successfully.")
                    .convertToJSON();
        }

        pageUser.getPublisher().subscribe(user);
        return new CommandOutput(this,
                    username + " subscribed to " + pageUser.getUsername() + " successfully.")
                    .convertToJSON();
    }
}
