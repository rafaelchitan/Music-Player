package commands.statistics;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import constants.Constants;
import entities.Library;
import entities.files.Song;
import fileio.input.CommandInput;
import fileio.output.StatisticsOutput;

import java.util.ArrayList;

public class GetTopSongsCommand extends Command {
    public GetTopSongsCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the command to get the top 5 songs for the given user.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        ArrayList<Song> songs = Library.getInstance().getSongs();
        ArrayList<Song> topSongs = new ArrayList<>(songs);
        topSongs.sort((o1, o2) -> o2.getLikeNumber() - o1.getLikeNumber());

        if (topSongs.size() > Constants.MAX_SEARCH_RESULTS) {
            ArrayList<Entity> topResults = new ArrayList<>(topSongs
                    .subList(0, Constants.MAX_SEARCH_RESULTS));
            return new StatisticsOutput(this, topResults).convertToJSON();
        }
        return new StatisticsOutput(this, new ArrayList<>(topSongs)).convertToJSON();
    }
}
