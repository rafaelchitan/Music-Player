package commands.audiofile.playlist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class SwitchVisibilityCommand extends PlaylistCommand {

    public SwitchVisibilityCommand(final CommandInput command) {
        this.playlistId = command.getPlaylistId();

        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.username = command.getUsername();
        this.playlistName = command.getPlaylistName();
    }

    /**
     * Executes the switchvisibility command - changes the visibility of a playlist(public/private).
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user.getPlaylists().size() < playlistId) {
            return new CommandOutput(this,
                    "The specified playlist ID is too high.").convertToJSON();
        }

        if (user.getPlaylists().get(playlistId - 1).getVisibility().equals("private")) {
            user.getPlaylists().get(playlistId - 1).setVisibility("public");
            return new CommandOutput(this,
                    "Visibility status updated successfully to public.").convertToJSON();
        }

        user.getPlaylists().get(playlistId - 1).setVisibility("private");
        return new CommandOutput(this,
                "Visibility status updated successfully to private.").convertToJSON();
    }
}
