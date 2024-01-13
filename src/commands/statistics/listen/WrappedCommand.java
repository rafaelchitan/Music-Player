package commands.statistics.listen;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.files.Album;
import entities.files.Episode;
import entities.files.Podcast;
import entities.files.Song;
import entities.users.Artist;
import entities.users.Host;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.WrappedOutput;
import java.util.HashMap;

public class WrappedCommand extends Command {

    public WrappedCommand(final CommandInput command) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.username = command.getUsername();
    }

    /**
     * Executes the wrapped command for a basic user.
     * @return the JSON representation of the output
     */
    public ObjectNode executeUser(final User user) {
        user.getPlayer().update(user, timestamp);
        WrappedOutput output = new WrappedOutput(this);

        // Get the top listened artists
        HashMap<String, Integer> listenedArtists = new HashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedArtists.put(song.getArtist(),
                        listenedArtists.getOrDefault(song.getArtist(), 0)
                                + song.getListenByUser(user));
            }
        }

        // Get the top listened genres
        for (Song song: Library.getInstance().getRemovedSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedArtists.put(song.getArtist(),
                        listenedArtists.getOrDefault(song.getArtist(), 0)
                                + song.getListenByUser(user));
            }
        }

        // Get the top listened genres
        HashMap<String, Integer> listenedGenres = new HashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedGenres.put(song.getGenre(),
                        listenedGenres.getOrDefault(song.getGenre(), 0)
                                + song.getListenByUser(user));
            }
        }

        // Get the top listened genres
        for (Song song: Library.getInstance().getRemovedSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedGenres.put(song.getGenre(),
                        listenedGenres.getOrDefault(song.getGenre(), 0)
                                + song.getListenByUser(user));
            }
        }

        // Get the top listened songs from existing songs
        HashMap<String, Integer> listenedSongs = new HashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedSongs.put(song.getName(),
                        listenedSongs.getOrDefault(song.getName(), 0)
                                + song.getListenByUser(user));
            }
        }

        // Get the top listened songs from removed songs
        for (Song song: Library.getInstance().getRemovedSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedSongs.put(song.getName(),
                        listenedSongs.getOrDefault(song.getName(), 0)
                                + song.getListenByUser(user));
            }
        }

        // Get the top listened albums from existing albums
        HashMap<String, Integer> listenedAlbums = new HashMap<>();
        for (Album album : Library.getInstance().getAlbums()) {
            if (album.getListenByUser(user) != 0) {
                listenedAlbums.put(album.getName(),
                        listenedAlbums.getOrDefault(album.getName(), 0)
                                + album.getListenByUser(user));
            }
        }

        // Get the top listened albums from removed albums
        for (Song song: Library.getInstance().getRemovedSongs()) {
            if (song.getListenByUser(user) != 0) {
                listenedAlbums.put(song.getAlbum(),
                        listenedAlbums.getOrDefault(song.getAlbum(), 0)
                                + song.getListenByUser(user));
            }
        }

        // Get the top listened episodes
        HashMap<Entity, Integer> listenedEpisodes = new HashMap<>();
        for (Podcast podcast : Library.getInstance().getPodcasts()) {
            for (Episode episode : podcast.getEpisodes()) {
                if (episode.getListenByUser(user) != 0) {
                    listenedEpisodes.put(episode, episode.getListenByUser(user));
                }
            }
        }

        return output
                .addString("topArtists", listenedArtists)
                .addString("topGenres", listenedGenres)
                .addString("topSongs", listenedSongs)
                .addString("topAlbums", listenedAlbums)
                .addObject("topEpisodes", listenedEpisodes)
                .convertToJSON();
    }

    /**
     * Executes the wrapped command for an artist.
     * @param artist the artist that executes the command
     * @return the JSON representation of the output
     */
    public ObjectNode executeArtist(final Artist artist) {
        for (User user : Library.getInstance().getUsers().stream()
                .filter(u -> u.getType().equals("user"))
                .toArray(User[]::new)) {
            user.getPlayer().update(user, timestamp);
        }
        WrappedOutput output = new WrappedOutput(this);

        // Get the top listened albums from existing albums
        HashMap<String, Integer> listenedAlbums = new HashMap<>();
        for (Album album : Library.getInstance().getAlbums()) {
            if (album.getArtist().equals(artist.getName()) && album.getListenByUser(null) != 0) {
                listenedAlbums.put(album.getName(),
                        listenedAlbums.getOrDefault(album.getName(), 0)
                                + album.getListenByUser(null));
            }
        }

        // Get the top listened albums from removed albums
        for (Song song: Library.getInstance().getRemovedSongs()) {
            if (song.getArtist().equals(artist.getName()) && song.getListenByUser(null) != 0) {
                listenedAlbums.put(song.getAlbum(),
                        listenedAlbums.getOrDefault(song.getAlbum(), 0)
                                + song.getListenByUser(null));
            }
        }

        // Get the top listened songs from existing songs
        HashMap<String, Integer> listenedSongs = new HashMap<>();
        for (Album album : Library.getInstance().getAlbums()) {
            if (album.getArtist().equals(artist.getName())) {
                for (Song song : album.getSongs()) {
                    if (song.getListenByUser(null) != 0) {
                        listenedSongs.put(song.getName(),
                                listenedSongs.getOrDefault(song.getName(), 0)
                                        + song.getListenByUser(null));
                    }
                }
            }
        }

        // Get the top listened songs from removed songs
        for (Song song: Library.getInstance().getRemovedSongs()) {
            if (song.getArtist().equals(artist.getName())) {
                if (song.getListenByUser(null) != 0) {
                    listenedSongs.put(song.getName(),
                            listenedSongs.getOrDefault(song.getName(), 0)
                                    + song.getListenByUser(null));
                }
            }
        }

        // Get the top listened fans
        HashMap<Entity, Integer> fans = new HashMap<>();
        for (User user : Library.getInstance().getUsers()) {
            if (user.getType().equals("user") && artist.getListenByUser(user) != 0) {
                fans.put(user, artist.getListenByUser(user));
            }
        }

        return output
                .addString("topAlbums", listenedAlbums)
                .addString("topSongs", listenedSongs)
                .addFans("topFans", fans)
                .addField("listeners", fans.size())
                .convertToJSON();
    }

    /**
     * Executes the wrapped command for a host.
     * @param host the host that executes the command
     * @return the JSON representation of the output
     */
    public ObjectNode executeHost(final Host host) {
        for (User user : Library.getInstance().getUsers().stream()
                .filter(u -> u.getType().equals("user"))
                .toArray(User[]::new)) {
            user.getPlayer().update(user, timestamp);
        }
        WrappedOutput output = new WrappedOutput(this);

        // Get the top listened episodes
        HashMap<String, Integer> listenedEpisodes = new HashMap<>();
        for (Podcast podcast : Library.getInstance().getPodcasts()) {
            if (podcast.getOwner().equals(host.getName())) {
                for (Episode episode : podcast.getEpisodes()) {
                    if (episode.getListenByUser(null) != 0) {
                        listenedEpisodes.put(episode.getName(),
                                listenedEpisodes.getOrDefault(episode.getName(), 0)
                                        + episode.getListenByUser(null));
                    }
                }
            }
        }

        // Get the number of listeners
        int listeners = 0;
        for (User user : Library.getInstance().getUsers()) {
            if (user.getType().equals("user") && host.getListenByUser(user) != 0) {
                listeners++;
            }
        }

        return output
                .addString("topEpisodes", listenedEpisodes)
                .addField("listeners", listeners)
                .convertToJSON();
    }

    /**
     * Executes the wrapped command.
     * @return the JSON representation of the output
     */
    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);

        return switch (user.getType()) {
            case "user" -> executeUser(user);
            case "artist" -> executeArtist((Artist) user);
            case "host" -> executeHost((Host) user);
            default -> null;
        };
    }
}
