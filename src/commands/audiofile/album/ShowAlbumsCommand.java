package commands.audiofile.album;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.ShowAlbumsOutput;

public class ShowAlbumsCommand extends Command {
    public ShowAlbumsCommand(final CommandInput commandInput) {

        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();
    }

    /**
     * Gets the list of albums of the current user
     * @return the list of albums
     */
    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        return new ShowAlbumsOutput(this, user.getAlbums()).convertToJSON();
    }
}
