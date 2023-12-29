package commands.pages;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.pages.Page;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class PrintCurrentPageCommand extends Command {
    private Page currentPage;

    public PrintCurrentPageCommand(final CommandInput commandInput) {

        this.username = commandInput.getUsername();
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        currentPage = Library.getInstance().getUserByName(username).getCurrentPage();
    }

    /**
     * Prints the current page of the user
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        if (!Library.getInstance().getUserByName(username).isOnline()) {
            String message = username + " is offline.";
            return new CommandOutput(this, message).convertToJSON();
        }

        return new CommandOutput(this, currentPage.toString()).convertToJSON();
    }
}
