package entities.files;

import entities.Entity;
import entities.Library;
import entities.users.User;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter@Setter
public class Album implements Entity {
    private String name;
    private String artist;
    private String description;
    private ArrayList<Song> songs;
    private int releaseYear;

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

    /**
     * Gets the number of times the user has listened to the album.
     * @param user the user to check the number of listens for
     * @return the number of times the user has listened to the album
     */
    public int getListenByUser(final User user) {
        int times = 0;
        for (Song song : songs) {
            times += song.getListenByUser(user);
        }

        return times;
    }

    /**
     * Gets the duration of the album.
     * @param user the user to check the duration for
     * @return the duration of the album
     */
    public int getDuration(final User user) {
        int duration = 0;
        for (Song song: songs) {
            duration += song.getDuration(user);
        }

        return duration;
    }
}
