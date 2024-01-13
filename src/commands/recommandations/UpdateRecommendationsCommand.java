package commands.recommandations;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.Playlist;
import entities.files.Song;
import entities.users.Artist;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;
import utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class UpdateRecommendationsCommand extends Command {
    private final String recommendationType;
    public UpdateRecommendationsCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.recommendationType = commandInput.getRecommendationType();
    }

    /**
     * Executes the UpdateRecommendations Command and returns the result.
     * @return JSON ObjectNode containing the result
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        user.getPlayer().update(user, timestamp);

        if (recommendationType.equals("random_song")) {
            if (user.getPlayer().getStats().getRemainedTime() > 0) {
                int seed = user.getPlayer().getActiveFile().getDuration(user)
                        - user.getPlayer().getStats().getRemainedTime();
                Song activeSong = (Song) user.getPlayer().getActiveFile();
                List<Song> songs = Library.getInstance().getSongs().stream()
                        .filter(s -> s.getGenre().equals((activeSong.getGenre())))
                        .toList();
                Song song = songs.get(new Random(seed).nextInt(songs.size()));
                if (song != null) {
                    user.updateSongsReccomandations(song);
                    user.setLastRecommendation(song);
                } else {
                    return new CommandOutput(this,
                            "No new recommendations were found")
                            .convertToJSON();
                }
            }
        } else if (recommendationType.equals("fans_playlist")
                && user.getPlayer().getActiveFile() != null) {
            Artist artist = (Artist) Library.getInstance()
                    .getUserByName(((Song) user.getPlayer().getActiveFile()).getArtist());

            HashMap<User, Integer> fans = new HashMap<>();
            for (User u : Library.getInstance().getUsers()) {
                if (u.getType().equals("user") && artist.getListenByUser(u) != 0) {
                    fans.put(u, artist.getListenByUser(u));
                }
            }

            ArrayList<User> sortedFans = new ArrayList<>(fans.keySet());
            sortedFans.sort((s1, s2) -> {
                if (fans.get(s1).equals(fans.get(s2))) {
                    return s1.getName().compareTo(s2.getName());
                }
                return fans.get(s2) - fans.get(s1);
            });
            sortedFans = new ArrayList<>(sortedFans.subList(0,
                    Math.min(Constants.MAX_COUNT, sortedFans.size())));
            HashSet<Song> playlistSongs = new HashSet<>();

            for (User fan: sortedFans) {
                List<Song> likedSongs = new ArrayList<>(fan.getLikedSongs().stream()
                        .sorted((s1, s2) -> s2.getLikeNumber() - s1.getLikeNumber())
                        .toList()
                        .subList(0, Math.min(Constants.MAX_COUNT, fan.getLikedSongs().size())));
                playlistSongs.addAll(likedSongs);
            }

            if (playlistSongs.isEmpty()) {
                return new CommandOutput(this,
                        "No new recommendations were found")
                        .convertToJSON();
            }

            Playlist playlist = new Playlist(artist.getName()
                    + " Fan Club recommendations", user, timestamp);
            playlist.getSongs().addAll(playlistSongs);
            user.updatePlaylistReccomandations(playlist);
            user.setLastRecommendation(playlist);
        } else if (recommendationType.equals("random_playlist")) {
            HashSet<Song> songs = new HashSet<>();
            songs.addAll(user.getLikedSongs());

            for (Playlist playlist : user.getPlaylists()) {
                songs.addAll(playlist.getSongs());
            }

            for (Playlist playlist : user.getFollowedPlaylists()) {
                songs.addAll(playlist.getSongs());
            }
            songs = new HashSet<>(songs.stream()
                    .sorted((s1, s2) -> s2.getLikeNumber() - s1.getLikeNumber())
                    .toList());

            HashMap<String, Integer> genres = new HashMap<>();
            for (Song song : songs) {
                genres.put(song.getGenre(), genres.getOrDefault(song.getGenre(), 0) + 1);
            }

            ArrayList<String> sortedGenres = new ArrayList<>(genres.keySet());
            sortedGenres.sort((s1, s2) -> {
                if (genres.get(s1).equals(genres.get(s2))) {
                    return s1.compareTo(s2);
                }
                return genres.get(s2) - genres.get(s1);
            });
            sortedGenres = new ArrayList<>(sortedGenres.subList(0,
                    Math.min(Constants.GENRE_COUNT, sortedGenres.size())));

            HashSet<Song> playlistSongs = new HashSet<>();

            if (sortedGenres.size() > 0) {
                String genre = sortedGenres.get(0);
                playlistSongs.addAll(songs.stream().filter(s -> s.getGenre().equals(genre))
                        .toList().subList(0, Math.min(Constants.MAX_COUNT, songs.size())));
            }

            if (sortedGenres.size() > 1) {
                String genre = sortedGenres.get(1);
                playlistSongs.addAll(songs.stream().filter(s -> s.getGenre().equals(genre))
                        .toList().subList(0, Math.min(2, songs.size())));
            }

            if (playlistSongs.isEmpty()) {
                return new CommandOutput(this,
                        "No new recommendations were found")
                        .convertToJSON();
            }

            Playlist playlist = new Playlist(username + "'s recommendations", user, timestamp);
            playlist.getSongs().addAll(playlistSongs);
            user.updatePlaylistReccomandations(playlist);
            user.setLastRecommendation(playlist);
        }
        return new CommandOutput(this,
                "The recommendations for user " + username + " have been updated successfully.")
                .convertToJSON();
    }
}
