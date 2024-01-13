package entities.files;

import entities.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

public abstract class AudioFile {
    @Getter @Setter
    protected String name;
    protected int duration;

    /**
     * Gets the type of the object.
     * @return the type of the object.
     */
    public abstract String objType();

    /**
     * Adds a new listen to the audiofile.
     * @param user the user that listened to the file
     * @param timestamp the timestamp of the listen
     */
    public abstract void addListened(User user, int timestamp);

    /**
     * Adds a new listen to the audiofile, monetized as premium.
     * @param user the user that listened to the file
     * @param timestamp the timestamp of the listen
     */
    public void premiumAddListened(final User user, int timestamp) { }

    public abstract HashSet<?> getTimesListened();

    public HashSet<?> getPremiumTimesListened() {
        return null;
    }

    /**
     * Gets the number of times the audiofile was listened by the given user.
     * @param user the user to check the number of listens for
     * @return the number of times the audiofile was listened by the given user
     */
    public abstract int getListenByUser(User user);

    public abstract int getDuration(User user);

    public void pass(final User user) { }
}
