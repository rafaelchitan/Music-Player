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
     * @return the type of the object.
     */
    public String objType() {
        return "playlist";
    }

    /**
     * @return the total duration of the playlist.
     */
    public int getDuration() {
        int duration = 0;
        for (Song song: songs) {
            duration += song.getDuration();
        }
        return duration;
    }
}
