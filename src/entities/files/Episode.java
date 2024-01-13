package entities.files;

import entities.Entity;
import entities.users.User;
import fileio.input.EpisodeInput;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashSet;

@Getter @Setter
public class Episode extends AudioFile implements Entity {
    private String description;
    protected HashSet<Pair<User, Integer>> timesListened = new HashSet<>();

    public Episode(final EpisodeInput episodeInput) {
        this.name = episodeInput.getName();
        this.duration = episodeInput.getDuration();
        this.description = episodeInput.getDescription();
    }

    /**
     * @return the type of the object.
     */
    public String objType() {
        return "episode";
    }

    public void addListened(User user, int timestamp) {
        timesListened.add(new Pair<>(user, timestamp));
    }

    public int getListenByUser(User user) {
        int times = 0;
        for (Pair<User, Integer> pair : timesListened) {
            if (pair.a.equals(user) || user == null) {
                times++;
            }
        }
        return times;
    }

    public int getDuration(User user) {
        return duration;
    }
}
