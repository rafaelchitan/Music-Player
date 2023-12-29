package entities.pages;

import entities.files.Playlist;
import entities.files.Song;
import entities.users.User;

import java.util.ArrayList;
import java.util.List;

public class LikedContentPage extends Page {

    private List<Song> likedSongs;
    private List<Playlist> followedPlaylists;

    public LikedContentPage(final User user) {
        this.user = user;
        update();
    }

    /**
     * Updates the lists of liked songs and followed playlists.
     */
    public void update() {
        likedSongs = new ArrayList<>(user.getLikedSongs());

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
            stringBuilder.append(song.getName()).append(" - ")
                    .append(song.getArtist()).append(", ");
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
            stringBuilder.append(playlist.getName()).append(" - ")
                    .append(playlist.getOwner().getName()).append(", ");
        }
        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]");
        } else {
            stringBuilder.append("]");
        }

        return stringBuilder.toString();
    }
}
