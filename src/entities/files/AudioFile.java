package entities.files;

import entities.users.User;
import lombok.Getter;
import lombok.Setter;

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

    public abstract HashSet<?> getTimesListened();

    public abstract int getListenByUser(User user);
}
