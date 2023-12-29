package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import entities.player.Player;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ShuffleCommand extends Command {
    private final int seed;

    public ShuffleCommand(final CommandInput commandInput) {
        this.seed = commandInput.getSeed();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();

    }

    /**
     * Shuffles the array of indexes based on a seed.
     * @param array the array to be shuffled
     */
    private void shuffleArray(final ArrayList<Integer> array) {
        Collections.shuffle(array, new Random(seed));
    }

    /**
     * Removes the shuffle from the array of indexes - reinitializes it.
     * @param array the array to be unshuffled
     */
    private void unshuffleArray(final ArrayList<Integer> array) {
        for (int i = 0; i < array.size(); i++) {
            array.set(i, i);
        }
    }

    /**
     * Executes the shuffle command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        user.getPlayer().update(user, timestamp);
        if (user.getPlayer().getCurrentFile() == null) {
            String message = "Please load a source before using the shuffle function.";
            return new CommandOutput(this, message).convertToJSON();
        }
        if (!user.getPlayer().getCurrentFile().objType().equals("playlist")
                && !user.getPlayer().getCurrentFile().objType().equals("album")) {
            String message = "The loaded source is not a playlist or an album.";
            return new CommandOutput(this, message).convertToJSON();
        }

        ObjectNode returnMessage;
        Player player = user.getPlayer();
        int songTimestamp = player.getCurrentElapsedTime();

        if (!user.getPlayer().getStats().isShuffle()) {
            shuffleArray(player.getQueueIndexes());
            for (int i = 0; i < player.getQueueIndexes().size(); i++) {
                if (player.getQueueIndexes().get(i) == player.getQueueIndex()) {
                    player.setQueueIndex(i);
                    break;
                }
            }
            user.getPlayer().getStats().setShuffle(true);
            returnMessage = new CommandOutput(this,
                    "Shuffle function activated successfully.").convertToJSON();


        } else {
            player.setQueueIndex(player.getQueueIndexes().get(player.getQueueIndex()));
            unshuffleArray(player.getQueueIndexes());
            user.getPlayer().getStats().setShuffle(false);
            returnMessage = new CommandOutput(this,
                    "Shuffle function deactivated successfully.").convertToJSON();

        }

        int currentTimestamp = 0;
        for (int i = 0; i < player.getQueueIndex(); i++) {
            int index = player.getQueueIndexes().get(i);
            currentTimestamp += player.getQueue().get(index).getDuration();
        }
        currentTimestamp += songTimestamp;
        player.setCurrentTimestamp(currentTimestamp);

        return returnMessage;
    }
}
