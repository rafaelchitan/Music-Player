package entities.files;

import entities.users.User;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashSet;

@Getter @Setter
public abstract class AudioFile {
    protected String name;
    protected int duration;

    /**
     * @return the type of the object.
     */
    public abstract String objType();

    public abstract void addListened(User user, int timestamp);

    public void premiumAddListened(User user, int timestamp) { }

    public abstract HashSet<?> getTimesListened();

    public HashSet<?> getPremiumTimesListened() {
        return null;
    }

    public abstract int getListenByUser(User user);

    public int getPremiumListenByUser(User user) {
        return 0;
    }
}
