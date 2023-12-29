package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.Song;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.LikeOutput;

public class LikeCommand extends Command {
    public LikeCommand(final CommandInput commandInput) {
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();

    }

    /**
     * Executes the like/unlike command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        if (!Library.getInstance().getUserByName(username).isOnline()) {
            String message = username + " is offline.";
            return new LikeOutput(this, message).convertToJSON();
        }

        User user = Library.getInstance().getUserByName(username);
        user.getPlayer().update(user, timestamp);
        if (user.getPlayer().getCurrentFile() == null) {
            return new LikeOutput(this,
                    "Please load a source before liking or unliking.").convertToJSON();
        }
        if (!user.getPlayer().getActiveFile().objType().equals("song")) {
            return new LikeOutput(this,
                    "Loaded source is not a song.").convertToJSON();
        }
        if (user.getLikedSongs().contains((Song) user.getPlayer().getActiveFile())) {
            user.getLikedSongs().remove((Song) user.getPlayer().getActiveFile());
            ((Song) user.getPlayer().getActiveFile())
                    .setLikeNumber(((Song) user.getPlayer().getActiveFile()).getLikeNumber() - 1);
            return new LikeOutput(this, "Unlike registered successfully.").convertToJSON();
        }
        user.getLikedSongs().add((Song) user.getPlayer().getActiveFile());
        ((Song) user.getPlayer().getActiveFile())
                .setLikeNumber(((Song) user.getPlayer().getActiveFile()).getLikeNumber() + 1);
        return new LikeOutput(this,
                "Like registered successfully.").convertToJSON();
    }
}
