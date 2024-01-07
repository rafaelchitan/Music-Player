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
    protected HashSet<Pair<User, Integer>> premiumTimesListened = new HashSet<>();

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

    public void premiumAddListened(User user, int timestamp) {
        premiumTimesListened.add(new Pair<>(user, timestamp));
    }

    public int getListenByUser(User user) {
        int times = 0;
        for (Pair<User, Integer> pair : timesListened) {
            if (pair.a.equals(user)) {
                times++;
            }
        }
        return times;
    }

    public int getPremiumListenByUser(User user) {
        int times = 0;
        for (Pair<User, Integer> pair : premiumTimesListened) {
            if (pair.a.equals(user)) {
                times++;
            }
        }
        return times;
    }
}
