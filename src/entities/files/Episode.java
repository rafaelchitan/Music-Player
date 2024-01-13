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
     * Gets the type of the object.
     * @return the type of the object.
     */
    public String objType() {
        return "episode";
    }

    /**
     * Adds a new listen to the episode.
     * @param user the user that listened to the episode
     * @param timestamp the timestamp of the listen
     */
    public void addListened(final User user, final int timestamp) {
        timesListened.add(new Pair<>(user, timestamp));
    }

    /**
     * Gets the number of times the user has listened to the episode.
     * @param user the user to check the number of listens for
     * @return the number of times the user has listened to the episode
     */
    public int getListenByUser(final User user) {
        int times = 0;
        for (Pair<User, Integer> pair : timesListened) {
            if (pair.a.equals(user) || user == null) {
                times++;
            }
        }
        return times;
    }

    /**
     * Gets the duration of the episode.
     * @param user the user to check the duration for
     * @return the duration of the episode
     */
    public int getDuration(final User user) {
        return duration;
    }
}
