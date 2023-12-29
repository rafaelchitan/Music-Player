package commands.statistics.listen;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.files.*;
import entities.users.Artist;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.WrappedOutput;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;

public class WrappedCommand extends Command {

    public WrappedCommand(CommandInput command) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.username = command.getUsername();
    }

    public ObjectNode executeUser(User user) {
        user.getPlayer().update(user, timestamp);
        WrappedOutput output = new WrappedOutput(this);

        HashMap<String, Integer> listenedArtists = new HashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedArtists.put(song.getArtist(), listenedArtists.getOrDefault(song.getArtist(), 0) + song.getListenByUser(user));
            }
        }
        output.addString("topArtists", listenedArtists);

        HashMap<String, Integer> listenedGenres = new HashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedGenres.put(song.getGenre(), listenedGenres.getOrDefault(song.getGenre(), 0) + song.getListenByUser(user));
            }
        }
        output.addString("topGenres", listenedGenres);

        HashMap<String, Integer> listenedSongs = new HashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedSongs.put(song.getName(), listenedSongs.getOrDefault(song.getName(), 0) + song.getListenByUser(user));
            }
        }
        output.addString("topSongs", listenedSongs);

        HashMap<String, Integer> listenedAlbums = new HashMap<>();
        for (Album album : Library.getInstance().getAlbums()) {
            if (album.getListenByUser(user) != 0) {
                listenedAlbums.put(album.getName(), listenedAlbums.getOrDefault(album.getName(), 0) + album.getListenByUser(user));
            }
        }
        output.addString("topAlbums", listenedAlbums);

        HashMap<Entity, Integer> listenedEpisodes = new HashMap<>();
        for (Podcast podcast : Library.getInstance().getPodcasts()) {
            for (Episode episode : podcast.getEpisodes()) {
                if (episode.getListenByUser(user) != 0) {
                    listenedEpisodes.put(episode, episode.getListenByUser(user));
                }
            }
        }
        output.addObject("topEpisodes", listenedEpisodes);

        return output.convertToJSON();
    }

    public ObjectNode executeArtist(Artist artist) {
        for (User user : Library.getInstance().getUsers().stream()
                .filter(u -> u.getType().equals("user"))
                .toArray(User[]::new)) {
            user.getPlayer().update(user, timestamp);
        }
        WrappedOutput output = new WrappedOutput(this);

        HashMap<String, Integer> listenedAlbums = new HashMap<>();
        for (Album album : Library.getInstance().getAlbums()) {
            if (album.getArtist().equals(artist.getName()) && album.getListenByUser(null) != 0) {

                listenedAlbums.put(album.getName(), listenedAlbums.getOrDefault(album.getName(), 0) + album.getListenByUser(null));
            }
        }
        output.addString("topAlbums", listenedAlbums);

        HashMap<String, Integer> listenedSongs = new HashMap<>();
        for (Album album : Library.getInstance().getAlbums()) {
            if (album.getArtist().equals(artist.getName())) {
                for (Song song : album.getSongs()) {
                    if (song.getListenByUser(null) != 0) {
                        listenedSongs.put(song.getName(), listenedSongs.getOrDefault(song.getName(), 0) + song.getListenByUser(null));
                    }
                }
            }
        }
        output.addString("topSongs", listenedSongs);

        HashMap<Entity, Integer> fans = new HashMap<>();
        for (User user : Library.getInstance().getUsers()) {
            if (user.getType().equals("user") && artist.getListenByUser(user) != 0) {
                fans.put(user, artist.getListenByUser(user));
            }
        }
        output.addFans("topFans", fans);

        output.addField("listeners", fans.size());

        return output.convertToJSON();
    }

    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);

        if (user.getType().equals("user")) {
            return executeUser(user);
        } else if (user.getType().equals("artist")) {
            return executeArtist((Artist) user);
        } else {
            return null;
        }
    }
}
