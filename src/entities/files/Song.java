package entities.files;

import entities.Entity;
import entities.Library;
import entities.users.User;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
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
    protected HashSet<Pair<User, Integer>> timesListened = new HashSet<>();

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

    public int getListenByUser(User user) {
        if (user == null)
            return timesListened.size();

        return (int) timesListened.stream()
                .filter(pair -> pair.a.equals(user))
                .count();
    }
}
