package commands.audiofile.playlist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.files.Playlist;
import entities.files.Song;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class AddRemoveInPlaylistCommand extends PlaylistCommand {

    public AddRemoveInPlaylistCommand(final CommandInput command) {
        this.playlistId = command.getPlaylistId();

        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.username = command.getUsername();
        this.playlistName = command.getPlaylistName();
    }

    /**
     * Executes the add/remove command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        String message;
        if (playlistId < 1 || playlistId > user.getPlaylists().size())  {
            message = "The specified playlist does not exist.";
            return new CommandOutput(this, message).convertToJSON();
        }
        Playlist playlist = user.getPlaylists().get(playlistId - 1);
        if (user.getPlayer().getActiveFile() == null) {
            message = "Please load a source before adding to or removing from the playlist.";
            return new CommandOutput(this, message).convertToJSON();
        }
        if (!user.getPlayer().getActiveFile().objType().equals("song"))  {
            message = "The loaded source is not a song.";
            return new CommandOutput(this, message).convertToJSON();
        }
        if (playlist.getSongs().contains((Song) user.getPlayer().getActiveFile())) {
            playlist.getSongs().remove((Song) user.getPlayer().getActiveFile());
            message = "Successfully removed from playlist.";
            return new CommandOutput(this, message).convertToJSON();
        }
        playlist.getSongs().add((Song) user.getPlayer().getActiveFile());
        message = "Successfully added to playlist.";
        return new CommandOutput(this, message).convertToJSON();
    }
}
