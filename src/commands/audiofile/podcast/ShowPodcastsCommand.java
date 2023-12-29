package commands.audiofile.podcast;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.ShowPodcastsOutput;

public class ShowPodcastsCommand extends Command {
    public ShowPodcastsCommand(final CommandInput commandInput) {

        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();
    }

    /**
     * Gets the list of podcasts of the current user
     * @return the list of podcasts
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        return new ShowPodcastsOutput(this, user.getPodcasts()).convertToJSON();
    }
}
