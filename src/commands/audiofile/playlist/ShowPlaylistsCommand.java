package commands.audiofile.playlist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.ShowPlaylistsOutput;

public class ShowPlaylistsCommand extends PlaylistCommand {
    public ShowPlaylistsCommand(final CommandInput commandInput) {

        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();
    }

    /**
     * Executes the showplaylists command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        return new ShowPlaylistsOutput(this, user.getPlaylists()).convertToJSON();
    }
}
