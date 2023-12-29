package commands.audiofile.podcast;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.files.Podcast;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class RemovePodcastCommand extends Command {
    private String name;
    public RemovePodcastCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.name = commandInput.getName();
    }

    /**
     * Removes a podcast from the user's list of podcasts
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);

        if (!user.getType().equals("host")) {
            return new CommandOutput(this, username
                    + " is not a host.").convertToJSON();
        }

        if (user.getPodcastByName(name) == null) {
            return new CommandOutput(this, username
                    + " doesn't have a podcast with the given name.").convertToJSON();
        }

        if (testActive()) {
            return new CommandOutput(this, username
                    + " can't delete this podcast.").convertToJSON();
        }

        Library.getInstance().getPodcasts().remove(user.getPodcastByName(name));
        user.getPodcasts().remove(user.getPodcastByName(name));
        return new CommandOutput(this, username
                + " deleted the podcast successfully.").convertToJSON();
    }

    /**
     * Checks if the podcast is active
     * @return true if the podcast is active, false otherwise
     */
    private boolean testActive() {

        for (User user: Library.getInstance().getUsers()) {
            if (user.getName().equals(username)) {
                continue;
            }

            user.getPlayer().update(user, timestamp);
            Entity currentFile = user.getPlayer().getCurrentFile();

            if (currentFile == null) {
                continue;
            }

            if (currentFile.objType().equals("podcast")
                    && ((Podcast) currentFile).getOwner().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
