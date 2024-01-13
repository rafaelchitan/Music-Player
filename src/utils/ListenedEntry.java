package utils;

import entities.files.Song;
import entities.users.User;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ListenedEntry {
    private final Song song;
    private final User user;
    private final int timestamp;

    public ListenedEntry(final Song song, final User user, final int timestamp) {
        this.song = song;
        this.user = user;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListenedEntry that = (ListenedEntry) o;
        return timestamp == that.timestamp
                && Objects.equals(song, that.song)
                && Objects.equals(user, that.user);
    }
}
