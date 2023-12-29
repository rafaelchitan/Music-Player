package commands.audiofile.playlist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Library;
import entities.files.Playlist;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class FollowCommand extends PlaylistCommand {
    public FollowCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the follow command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user.getSelectedFile() == null) {
            return new CommandOutput(this,
                    "Please select a source before following or unfollowing.").convertToJSON();
        }
        if (!user.getSelectedFile().objType().equals("playlist")) {
            return new CommandOutput(this,
                    "The selected source is not a playlist.").convertToJSON();
        }
        Playlist playlist = (Playlist) user.getSelectedFile();
        if (user.getPlaylists().contains(playlist)) {
            return new CommandOutput(this,
                    "You cannot follow or unfollow your own playlist.").convertToJSON();
        }
        if (user.getFollowedPlaylists().contains(playlist)) {
            user.getFollowedPlaylists().remove(playlist);
            playlist.setFollowers(playlist.getFollowers() - 1);
            return new CommandOutput(this,
                    "Playlist unfollowed successfully.").convertToJSON();
        }
        user.getFollowedPlaylists().add(playlist);
        playlist.setFollowers(playlist.getFollowers() + 1);
        return new CommandOutput(this,
                "Playlist followed successfully.").convertToJSON();
    }
}
