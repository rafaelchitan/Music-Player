package commands.pages;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class ChangePageCommand extends Command {
    private String nextPage;

    public ChangePageCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.nextPage = commandInput.getNextPage();

    }

    /**
     * Changes the current page of the user
     * @return the JSON representation of the command result
     */
    public ObjectNode execute() {

        User user = Library.getInstance().getUserByName(username);

        switch (nextPage) {
            case "Home" -> user.setCurrentPage(user.getPublicPage());
            case "LikedContent" -> user.setCurrentPage(user.getLikedContentPage());
            default -> {
                return new CommandOutput(this,  username
                    + " is trying to access a non-existent page.").convertToJSON(); }
        }

        return new CommandOutput(this, username
                + " accessed " + nextPage
                + " successfully.").convertToJSON();
    }
}
