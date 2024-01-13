package entities.files;

import entities.Entity;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
@Getter @Setter
public class Playlist implements Entity {
    private ArrayList<Song> songs = new ArrayList<>();
    private String visibility = "public"; // private or public
    private User owner;
    private String name;
    private int followers;
    private int creationTimestamp;
    public Playlist(final String name, final User owner, final int creationTimestamp) {
        this.name = name;
        this.owner = owner;
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Gets the current filetype
     * @return the type of the object.
     */
    public String objType() {
        return "playlist";
    }

    /**
     * Gets the duration of the playlist.
     * @return the total duration of the playlist.
     */
    public int getDuration(final User user) {
        int duration = 0;
        for (Song song: songs) {
            duration += song.getDuration(user);
        }
        return duration;
    }
}
