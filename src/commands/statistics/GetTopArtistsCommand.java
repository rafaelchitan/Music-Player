package commands.statistics;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import utils.Constants;
import entities.Library;
import entities.files.Album;
import entities.files.Song;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.StatisticsOutput;

import java.util.ArrayList;
import java.util.List;

public class GetTopArtistsCommand extends Command {
    public GetTopArtistsCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Gets the top 5 artists in the library
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        List<User> artists = Library.getInstance().getUsers().stream()
                .filter(user -> user.getType().equals("artist"))
                .sorted((a1, a2) -> {
            int likes1 = 0, likes2 = 0;

            for (Album album: a1.getAlbums()) {
                for (Song song : album.getSongs()) {
                    likes1 += song.getLikeNumber();
                }
            }

            for (Album album: a2.getAlbums()) {
                for (Song song : album.getSongs()) {
                    likes2 += song.getLikeNumber();
                }
            }

            if (likes1 == likes2) {
                return a1.getName().compareTo(a2.getName());
            }

            return likes2 - likes1;
        }).toList();

        return new StatisticsOutput(this, new ArrayList<>(artists.subList(0,
                        Math.min(artists.size(), Constants.MAX_COUNT)))).convertToJSON();
    }
}
