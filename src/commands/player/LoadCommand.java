package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.Podcast;
import entities.users.User;
import entities.player.Player;
import fileio.input.CommandInput;
import fileio.output.LoadOutput;

public class LoadCommand extends Command {
    public LoadCommand(final CommandInput commandInput) {
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
        if (user.getSelectedFile() == null) {
            return new LoadOutput(this,
                    "Please select a source before attempting to load.").convertToJSON();
        }

        if (user.getSelectedFile().objType().equals("podcast")) {
            if (!user.getPodcastHistory().containsKey((Podcast) user.getSelectedFile())) {
                user.getPodcastHistory().put((Podcast) user.getSelectedFile(), 0);
                user.getPlayer().update(user, timestamp);
            }
            user.setPlayer(new Player(timestamp, user.getSelectedFile(),
                    user.getPodcastHistory().get((Podcast) user.getSelectedFile())));
            user.getPlayer().update(user, timestamp);
        } else {
            user.setPlayer(new Player(timestamp, user.getSelectedFile(), 0));
            user.getPlayer().update(user, timestamp);
        }
        user.getPlayer().getStats().setName(user.getSelectedFile().getName());
        user.getPlayer().getStats().setRemainedTime(user.getSelectedFile().getDuration(user));

        user.setSelectedFile(null);
        return new LoadOutput(this, "Playback loaded successfully.").convertToJSON();
    }
}
