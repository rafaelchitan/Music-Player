package commands.pages;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class PreviousPageCommand extends Command {
    private String nextPage;

    public PreviousPageCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
    }

    /**
     * Changes the current page of the user
     * @return the JSON representation of the command result
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (!user.goPreviousPage()) {
            return new CommandOutput(this,
                    "There are no pages left to go back.").convertToJSON();
        } else {
            return new CommandOutput(this,
                    "The user " + username + " has navigated successfully to the previous page.")
                    .convertToJSON();
        }
    }
}
