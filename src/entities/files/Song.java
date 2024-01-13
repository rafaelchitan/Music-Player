package entities.files;

import entities.Entity;
import entities.Library;
import entities.users.User;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
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
    private double money = 0;
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
     * @return the type of the object.
     */
    public String objType() {
        return "song";
    }

    public void addListened(User user, int timestamp) {
        timesListened.add(new Pair<>(user, timestamp));
    }

    public void premiumAddListened(User user, int timestamp) {
        premiumTimesListened.add(new Pair<>(user, timestamp));
    }

    public int getListenByUser(User user) {
        if (user == null)
            return timesListened.size();

        return (int) timesListened.stream()
                .filter(pair -> pair.a.equals(user))
                .count();
    }

    public int getPremiumListenByUser(User user) {
        if (user == null)
            return premiumTimesListened.size();

        return (int) premiumTimesListened.stream()
                .filter(pair -> pair.a.equals(user))
                .count();
    }

    @Override
    public int getDuration(User user) {
        if (hasAdBreak.contains(user))
            return duration + 10;
        return duration;
    }

    @Override
    public void pass(User user) {
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
            if (listenedSongs.isEmpty())
                return;

            int price = adPrice.get(user);
            for (Song song : listenedSongs.keySet()) {
                song.setMoney(song.getMoney() + (double) price / totalListened * listenedSongs.get(song));
                User artist = Library.getInstance().getUserByName(song.getArtist());
                artist.setSongMoney(artist.getSongMoney() + (double) price / totalListened * listenedSongs.get(song));
            }


        }
    }
}
