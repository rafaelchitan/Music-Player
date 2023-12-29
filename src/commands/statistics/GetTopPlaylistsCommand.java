package commands.statistics;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import constants.Constants;
import entities.Library;
import entities.files.Playlist;
import fileio.input.CommandInput;
import fileio.output.StatisticsOutput;

import java.util.ArrayList;

public class GetTopPlaylistsCommand extends Command {
    public GetTopPlaylistsCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the command to get the top 5 playlists for the given user.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        ArrayList<Playlist> playlists = Library.getInstance().getPublicPlaylists();
        ArrayList<Playlist> topPlaylists = new ArrayList<>(playlists);
        topPlaylists.sort((o1, o2) -> {
            if (o1.getFollowers() == o2.getFollowers()) {
                return o1.getCreationTimestamp() - o2.getCreationTimestamp();
            }
            return o2.getFollowers() - o1.getFollowers();
        });

        if (topPlaylists.size() > Constants.MAX_SEARCH_RESULTS) {
            ArrayList<Entity> topResults = new ArrayList<>(topPlaylists
                    .subList(0, Constants.MAX_SEARCH_RESULTS));
            return new StatisticsOutput(this, topResults).convertToJSON();
        }
        return new StatisticsOutput(this, new ArrayList<>(topPlaylists)).convertToJSON();
    }
}
