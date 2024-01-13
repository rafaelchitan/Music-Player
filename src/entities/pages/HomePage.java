package entities.pages;

import utils.Constants;
import entities.files.Playlist;
import entities.files.Song;
import entities.users.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HomePage extends Page {

    private List<Song> likedSongs;
    private List<Playlist> followedPlaylists;

    @Getter
    private List<Song> songReccomandations = new ArrayList<>();

    @Getter
    private List<Playlist> playlistReccomandations = new ArrayList<>();

    public HomePage(final User user) {
        this.user = user;
        update();
    }

    /**
     * Updates the lists of liked songs and followed playlists.
     */
    public void update() {
        likedSongs = new ArrayList<>(user.getLikedSongs());
        likedSongs.sort(Comparator.comparingInt(Song::getLikeNumber).reversed());
        likedSongs = new ArrayList<>(likedSongs
                .subList(0, Math.min(Constants.MAX_SEARCH_RESULTS, likedSongs.size())));

        followedPlaylists = user.getFollowedPlaylists();
        followedPlaylists.sort((p1, p2) -> {
            int p1Likes = 0, p2Likes = 0;
            for (Song song : p1.getSongs()) {
                p1Likes += song.getLikeNumber();
            }

            for (Song song : p2.getSongs()) {
                p2Likes += song.getLikeNumber();
            }

            return p2Likes - p1Likes;
        });
        followedPlaylists = new ArrayList<>(followedPlaylists
                .subList(0, Math.min(Constants.MAX_SEARCH_RESULTS, followedPlaylists.size())));
    }

    /**
     * Prints the current page to the user
     * @return the string representing the page
     */
    @Override
    public String toString() {
        update();
        StringBuilder stringBuilder = new StringBuilder();
        boolean toReplace = false;

        stringBuilder.append("Liked songs:\n\t[");
        for (Song song : likedSongs) {
            stringBuilder.append(song.getName()).append(", ");
            toReplace = true;
        }
        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]\n\n");
        } else {
            stringBuilder.append("]\n\n");
        }

        toReplace = false;
        stringBuilder.append("Followed playlists:\n\t[");
        for (Playlist playlist : followedPlaylists) {
            toReplace = true;
            stringBuilder.append(playlist.getName()).append(", ");
        }
        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]");
        } else {
            stringBuilder.append("]\n\n");
        }

        toReplace = false;
        stringBuilder.append("Song recommendations:\n\t[");
        for (Song song : songReccomandations) {
            toReplace = true;
            stringBuilder.append(song.getName()).append(", ");
        }
        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]\n\n");
        } else {
            stringBuilder.append("]\n\n");
        }

        toReplace = false;
        stringBuilder.append("Playlists recommendations:\n\t[");
        for (Playlist playlist : playlistReccomandations) {
            toReplace = true;
            stringBuilder.append(playlist.getName()).append(", ");
        }
        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]");
        } else {
            stringBuilder.append("]");
        }

        return stringBuilder.toString();
    }
}
