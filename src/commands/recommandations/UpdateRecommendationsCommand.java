package commands.recommandations;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.files.Playlist;
import entities.files.Song;
import entities.users.Artist;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

import java.util.*;

public class UpdateRecommendationsCommand extends Command {
    private String recommendationType;
    public UpdateRecommendationsCommand(CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.recommendationType = commandInput.getRecommendationType();
    }

    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        user.getPlayer().update(user, timestamp);

        if (recommendationType.equals("random_song")) {
            if (user.getPlayer().getStats().getRemainedTime() > 0) {
                int seed = user.getPlayer().getActiveFile().getDuration()
                        - user.getPlayer().getStats().getRemainedTime();
                Song activeSong = (Song) user.getPlayer().getActiveFile();
                List<Song> songs = Library.getInstance().getSongs().stream()
                        .filter(s -> s.getGenre().equals((activeSong.getGenre())))
                        .toList();
                Song song = songs.get(new Random(seed).nextInt(songs.size()));
                user.updateSongsReccomandations(song);
            }
        } else if (user.getPlayer().getActiveFile() != null){
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
            sortedFans = new ArrayList<>(sortedFans.subList(0, Math.min(5, sortedFans.size())));
            HashSet<Song> playlistSongs = new HashSet<>();

            for (User fan: sortedFans) {
                List<Song> likedSongs = new ArrayList<>(fan.getLikedSongs().stream()
                        .sorted((s1, s2) -> {
                    return s2.getLikeNumber() - s1.getLikeNumber();
                }).toList().subList(0, Math.min(5, fan.getLikedSongs().size())));
                playlistSongs.addAll(likedSongs);
            }

            Playlist playlist = new Playlist(artist.getName() + " Fan Club recommendations", user, timestamp);
            playlist.getSongs().addAll(playlistSongs);
            user.updatePlaylistReccomandations(playlist);
        }
        return new CommandOutput(this,
                "The recommendations for user " + username + " have been updated successfully.")
                .convertToJSON();
    }
}
