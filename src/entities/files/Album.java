package entities.files;

import entities.Entity;
import entities.Library;
import entities.users.User;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;

@Getter@Setter
public class Album implements Entity {
    private String name;
    private String artist;
    private String description;
    private ArrayList<Song> songs;
    private int releaseYear;
    private int duration;

    public Album(final String name, final String artist, final int releaseYear,
                 final String description, final ArrayList<SongInput> songInputs) {
        this.name = name;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.songs = new ArrayList<>();
        this.description = description;

        for (SongInput songInput : songInputs) {
            Song newSong = new Song(songInput);
            songs.add(newSong);
            Library.getInstance().getSongs().add(newSong);
        }
    }

    /**
     * Gets the current filetype
     * @return the type of the object.
     */
    @Override
    public String objType() {
        return "album";
    }

    public int getListenByUser(User user) {
        int times = 0;

//        if (user == null) {
//            for (Song song : songs) {
//                for (Song librarySong : Library.getInstance().getSongs().stream()
//                        .filter(s -> s.getName().equals(song.getName()))
//                        .filter(s -> s.getArtist().equals(song.getArtist()))
//                        .toList()) {
//                    times += librarySong.getListenByUser(null);
//                }
//            }
//            return times;
//        }

        for (Song song : songs) {
//            for (Song librarySong : Library.getInstance().getSongs().stream()
//                    .filter(s -> s.getName().equals(song.getName()))
//                    .filter(s -> s.getArtist().equals(song.getArtist()))
//                    .toList()) {
//                times += librarySong.getListenByUser(user);
//            }
            times += song.getListenByUser(user);
        }

        return times;
    }
}
