package commands.audiofile.podcast;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.Podcast;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.output.CommandOutput;

import java.util.ArrayList;

public class AddPodcastCommand extends Command {
    private String name;
    private String host;
    private ArrayList<EpisodeInput> episodeInputs;

    public AddPodcastCommand(final CommandInput commandInput) {
        this.name = commandInput.getName();
        this.host = commandInput.getUsername();
        this.username = commandInput.getUsername();
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.episodeInputs = commandInput.getEpisodes();
    }

    /**
     * Adds a new podcast to the user's list of podcasts
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {

        if (!Library.getInstance().getUserByName(username).getType().equals("host")) {
            return new CommandOutput(this, username
                    + " is not a host.").convertToJSON();
        }

        if (Library.getInstance().getUserByName(username).getPodcastByName(name) != null) {
            return new CommandOutput(this, username
                    + " has another podcast with the same name.").convertToJSON();
        }

        for (EpisodeInput episodeInput: episodeInputs) {
            if (episodeInputs.stream()
                    .filter(episode -> episode.getName().equals(episodeInput.getName()))
                    .count() != 1) {
                return new CommandOutput(this, username
                        + " has the same episode in this podcast.").convertToJSON();
            }
        }

        Podcast podcast = new Podcast(name, host, episodeInputs);

        Library.getInstance().getUserByName(username).getPodcasts().add(podcast);
        return new CommandOutput(this, username
                + " has added new podcast successfully.").convertToJSON();
    }
}
