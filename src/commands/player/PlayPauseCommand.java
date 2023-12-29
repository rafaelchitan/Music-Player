package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.PlayPauseOutput;

public class PlayPauseCommand extends Command {
    public PlayPauseCommand(final CommandInput commandInput) {
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();

    }

    /**
     * Executes the play/pause command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        user.getPlayer().update(user, timestamp);
        if (user.getPlayer().getCurrentFile() == null) {
            String message = "Please load a source before attempting to pause or resume playback.";
            return new PlayPauseOutput(this, message).convertToJSON();
        }

        if (user.getPlayer().getPlayStatus().equals("paused")) {
            user.getPlayer().setPlayStatus("playing");
            user.getPlayer().getStats().setPaused(false);
            user.getPlayer().setStartTimestamp(timestamp);
            return new PlayPauseOutput(this, "Playback resumed successfully.").convertToJSON();
        } else {
            user.getPlayer().setPlayStatus("paused");
            user.getPlayer().getStats().setPaused(true);
            return new PlayPauseOutput(this, "Playback paused successfully.").convertToJSON();
        }
    }
}
