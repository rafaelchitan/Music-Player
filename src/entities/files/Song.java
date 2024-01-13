package entities.files;

import entities.Entity;
import entities.Library;
import entities.users.User;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
import utils.Constants;
import utils.ListenedEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Getter @Setter
public class Song extends AudioFile implements Entity {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private int releaseYear;
    private String artist;
    private int likeNumber = 0;
    private Double money = 0.0;
    private HashSet<Pair<User, Integer>> timesListened = new HashSet<>();
    private HashSet<Pair<User, Integer>> premiumTimesListened = new HashSet<>();
    private HashSet<User> hasAdBreak = new HashSet<>();
    private HashSet<User> markedAdBreaks = new HashSet<>();
    private HashMap<User, Integer> adPrice = new HashMap<>();
    private HashMap<User, Integer> adTimestamp = new HashMap<>();

    public Song(final SongInput songInput) {
        this.name = songInput.getName();
        this.duration = songInput.getDuration();
        this.album = songInput.getAlbum();
        this.tags = songInput.getTags();
        this.lyrics = songInput.getLyrics();
        this.genre = songInput.getGenre();
        this.releaseYear = songInput.getReleaseYear();
        this.artist = songInput.getArtist();
    }

    /**
     * Gets the type of the object.
     * @return the type of the object.
     */
    public String objType() {
        return "song";
    }

    /**
     * Adds a new listen to the song.
     * @param user the user that listened to the song
     * @param timestamp the timestamp of the listen
     */
    public void addListened(final User user, final int timestamp) {
        timesListened.add(new Pair<>(user, timestamp));
    }

    /**
     * Adds a new listen to the song, monetized as premium.
     * @param user the user that listened to the song
     * @param timestamp the timestamp of the listen
     */
    public void premiumAddListened(final User user, final int timestamp) {
        premiumTimesListened.add(new Pair<>(user, timestamp));
    }

    /**
     * Gets the number of times the song was listened by the given user.
     * @param user the user to check the number of listens for
     * @return the number of times the song was listened by the given user
     */
    public int getListenByUser(final User user) {
        if (user == null) {
            return timesListened.size();
        }

        return (int) timesListened.stream()
                .filter(pair -> pair.a.equals(user))
                .count();
    }

    /**
     * Gets the number of times the song was listened by the given user, monetized as premium.
     * @param user the user to check the number of listens for
     * @return the number of times the song was listened by the given user, monetized as premium
     */
    public int getPremiumListenByUser(final User user) {
        if (user == null) {
            return premiumTimesListened.size();
        }

        return (int) premiumTimesListened.stream()
                .filter(pair -> pair.a.equals(user))
                .count();
    }

    /**
     * Gets the duration of the song, with the ad break if the user has it.
     * @param user the user to check the ad break for
     * @return the duration of the song, with the ad break if the user has it
     */
    @Override
    public int getDuration(final User user) {
        if (hasAdBreak.contains(user)) {
            return duration + Constants.AD_BREAK_DURATION;
        }
        return duration;
    }

    /**
     * Check if the song contains an ad break for the given user and monetize
     * all the songs from the last ad break
     * @param user the user to check the ad break for
     */
    @Override
    public void pass(final User user) {
        if (hasAdBreak.contains(user) && !markedAdBreaks.contains(user)) {
            markedAdBreaks.add(user);
            HashMap<Song, Integer> listenedSongs = new HashMap<>();
            ArrayList<ListenedEntry> listenedEntries = Library.getInstance().getListenedEntries();
            int totalListened = 0;
            for (Song song : Library.getInstance().getSongs()) {
                for (Pair<User, Integer> pair: song.getTimesListened()) {
                    if (pair.a.equals(user)
                            && pair.b <= adTimestamp.get(user)
                            && !listenedEntries.contains(new ListenedEntry(song, user, pair.b))) {
                        listenedSongs.put(song, listenedSongs.getOrDefault(song, 0) + 1);
                        totalListened++;
                        listenedEntries.add(new ListenedEntry(song, user, pair.b));
                    }
                }
            }

            for (Song song : Library.getInstance().getRemovedSongs()) {
                for (Pair<User, Integer> pair: song.getTimesListened()) {
                    if (pair.a.equals(user)
                            && pair.b <= adTimestamp.get(user)
                            && !listenedEntries.contains(new ListenedEntry(song, user, pair.b))) {
                        listenedSongs.put(song, listenedSongs.getOrDefault(song, 0) + 1);
                        totalListened++;
                        listenedEntries.add(new ListenedEntry(song, user, pair.b));
                    }
                }
            }

            if (listenedSongs.isEmpty()) {
                return;
            }

            int price = adPrice.get(user);
            for (Song song : listenedSongs.keySet()) {
                song.setMoney(song.getMoney()
                        + (double) price / totalListened * listenedSongs.get(song));

                User monetizedArtist = Library.getInstance().getUserByName(song.getArtist());
                monetizedArtist.setSongMoney(monetizedArtist.getSongMoney()
                        + (double) price / totalListened * listenedSongs.get(song));
            }

        }
    }
}
