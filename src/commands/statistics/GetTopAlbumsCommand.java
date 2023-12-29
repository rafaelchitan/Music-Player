package commands.statistics;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import constants.Constants;
import entities.files.Album;
import entities.Library;
import entities.files.Song;
import fileio.input.CommandInput;
import fileio.output.StatisticsOutput;

import java.util.ArrayList;
import java.util.List;

public class GetTopAlbumsCommand extends Command {
    public GetTopAlbumsCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Gets the top 5 albums in the library
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        List<Album> albums = Library.getInstance().getAlbums().stream().sorted((a1, a2) -> {
            int likes1 = 0, likes2 = 0;
            for (Song song: a1.getSongs()) {
                likes1 += song.getLikeNumber();
            }

            for (Song song: a2.getSongs()) {
                likes2 += song.getLikeNumber();
            }

            if (likes1 == likes2) {
                return a1.getName().compareTo(a2.getName());
            }

            return likes2 - likes1;
        }).toList();

        return new StatisticsOutput(this, new ArrayList<>(albums.subList(0,
                Math.min(albums.size(), Constants.MAX_SEARCH_RESULTS)))).convertToJSON();
    }
}
