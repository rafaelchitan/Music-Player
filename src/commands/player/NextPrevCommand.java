package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.files.AudioFile;
import entities.Library;
import entities.player.Player;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class NextPrevCommand extends Command {
    private final User user;
    public NextPrevCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        user = Library.getInstance().getUserByName(username);
    }

    /**
     * Executes the next/prev command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        Player player = Library.getInstance().getUserByName(username).getPlayer();
        player.update(Library.getInstance().getUserByName(username), timestamp);
        String message;
        if (player.getCurrentFile() == null) {
            if (command.equals("next")) {
                message = "Please load a source before skipping to the next track.";
                return new CommandOutput(this, message).convertToJSON();
            }
            message = "Please load a source before returning to the previous track.";
            return new CommandOutput(this, message).convertToJSON();
        }

        if (command.equals("next")) {
            return next();
        }

        return prev();
    }

    /**
     * Skips to the next track.
     * @return the JSON representation of the command output
     */
    private ObjectNode next() {
        Player player = Library.getInstance().getUserByName(username).getPlayer();
        if (player.getRepeatThis() > 0) {
            player.setRepeatThis(player.getRepeatThis() - 1);
            prev();
            player.setPlayStatus("playing");
            player.getStats().setPaused(false);
            return new CommandOutput(this,
                    "Skipped to next track successfully. The current track is "
                    + player.getActiveFile().getName() + ".").convertToJSON();
        }
        if (player.getQueueIndex() == player.getQueue().size() - 1) {
            if (player.getRepeatAllCount() > 0) {
                player.setRepeatAllCount(player.getRepeatAllCount() - 1);
                player.setQueueIndex(0);
                AudioFile nextFile = player.getQueue().get(player.getQueueIndexes().get(0));
                player.setCurrentTimestamp(0);
                player.setActiveFile(nextFile);
                player.setPlayStatus("playing");
                player.getStats().setPaused(false);
                return new CommandOutput(this,
                        "Skipped to next track successfully. The current track is "
                                + player.getActiveFile().getName() + ".").convertToJSON();
            }
            player.reset();
            return new CommandOutput(this,
                    "Please load a source before skipping to the next track.").convertToJSON();
        }
        player.setPlayStatus("playing");
        player.getStats().setPaused(false);
        int index = player.getQueueIndex() + 1;
        AudioFile nextFile = player.getQueue().get(player.getQueueIndexes().get(index));
        player.setCurrentTimestamp(player.getCurrentTimestamp()
                + player.getActiveFile().getDuration(user)
                - player.getCurrentElapsedTime(user));
        player.setActiveFile(nextFile);
        player.setQueueIndex(player.getQueueIndex() + 1);
        return new CommandOutput(this,
                "Skipped to next track successfully. The current track is "
                        + player.getActiveFile().getName() + ".").convertToJSON();
    }

    /**
     * Returns to the previous track.
     * @return the JSON representation of the command output
     */
    private ObjectNode prev() {
        Player player = Library.getInstance().getUserByName(username).getPlayer();
        player.setPlayStatus("playing");
        player.getStats().setPaused(false);
        if (player.getCurrentElapsedTime(user) >= 1) {
            player.setCurrentTimestamp(player.getCurrentTimestamp()
                    - player.getCurrentElapsedTime(user));
            player.setStartTimestamp(timestamp);
        } else {
            if (player.getQueueIndex() == 0) {
                player.setCurrentTimestamp(player.getCurrentTimestamp()
                        - player.getCurrentElapsedTime(user));
                return new CommandOutput(this,
                        "Returned to previous track successfully. The current track is "
                                + player.getActiveFile().getName() + ".").convertToJSON();
            }
            player.setQueueIndex(player.getQueueIndex() - 1);
            int index = player.getQueueIndex();
            AudioFile prevFile = player.getQueue().get(player.getQueueIndexes().get(index));
            player.setCurrentTimestamp(player.getCurrentTimestamp() - prevFile.getDuration(user));
            player.setActiveFile(prevFile);
        }
        return new CommandOutput(this,
                "Returned to previous track successfully. The current track is "
                        + player.getActiveFile().getName() + ".").convertToJSON();
    }

}
