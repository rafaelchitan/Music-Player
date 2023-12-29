package commands.audiofile.playlist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.files.Playlist;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class CreatePlaylistCommand extends PlaylistCommand {

    public CreatePlaylistCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.playlistName = commandInput.getPlaylistName();
    }

    /**
     * Executes the create playlist command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user == null) {
            return null;
        }

        if (user.getPlaylistByName(playlistName) != null) {
            String message = "A playlist with the same name already exists.";
            return new CommandOutput(this, message).convertToJSON();
        }

        Playlist playlist = new Playlist(playlistName, user, timestamp);
        user.getPlaylists().add(playlist);
        Library.getInstance().getPublicPlaylists().add(playlist);
        String message = "Playlist created successfully.";
        return new CommandOutput(this, message).convertToJSON();
    }

}
