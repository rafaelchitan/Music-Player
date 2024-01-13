package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.Podcast;
import entities.player.Player;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.LoadOutput;

public class LoadRecommendationsCommand extends Command {
    public LoadRecommendationsCommand(final CommandInput commandInput) {
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();
    }

    /**
     * Executes the load command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user.getLastRecommendation() == null) {
            return new LoadOutput(this,
                    "No recommendations available.").convertToJSON();
        }
        user.setPlayer(new Player(timestamp, user.getLastRecommendation(), 0));

        user.getPlayer().update(user, timestamp);
        user.getPlayer().getStats().setName(user.getLastRecommendation().getName());
        user.getPlayer().getStats().setRemainedTime(user.getLastRecommendation().getDuration(user));

        user.setSelectedFile(null);
        return new LoadOutput(this, "Playback loaded successfully.").convertToJSON();
    }
}
