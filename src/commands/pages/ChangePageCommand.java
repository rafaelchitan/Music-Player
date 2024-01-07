package commands.pages;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.Podcast;
import entities.files.Song;
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
        user.getPlayer().update(user, timestamp);

        switch (nextPage) {
            case "Home" -> user.addPage(user.getPublicPage());
            case "LikedContent" -> user.addPage(user.getLikedContentPage());
            case "Artist" -> user.addPage(Library.getInstance()
                    .getUserByName(((Song) user.getPlayer().getActiveFile()).getArtist())
                    .getPublicPage());
            case "Host" -> user.addPage(Library.getInstance()
                    .getUserByName(((Podcast) user.getPlayer().getCurrentFile()).getOwner())
                    .getPublicPage());
            default -> {
                return new CommandOutput(this,  username
                    + " is trying to access a non-existent page.").convertToJSON(); }
        }

        return new CommandOutput(this, username
                + " accessed " + nextPage
                + " successfully.").convertToJSON();
    }
}
