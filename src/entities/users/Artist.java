package entities.users;

import entities.Library;
import entities.files.Album;
import entities.files.Song;
import entities.users.specifics.Event;
import entities.users.specifics.Merch;
import entities.pages.ArtistPage;
import lombok.Getter;
import lombok.Setter;
import notifications.Publisher;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
public class Artist extends User {
    private List<Album> albums = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Merch> merch = new ArrayList<>();

    private Publisher publisher = new Publisher();

    public Artist(final String username, final int age, final String city, final String type) {
        super(username, age, city, type);
        publicPage = new ArtistPage(this);
    }

    public Artist(final String username) {
        super(username);
        isFake = true;
    }

    /**
     * Gets the album with the given name, if it exists.
     * @param albumName the name of the album
     * @return the album with the given name
     */
    @Override
    public Album getAlbumByName(final String albumName) {
        return albums.stream()
                .filter(album -> album.getName().equals(albumName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the event with the given name, if it exists.
     * @param eventName the name of the event
     * @return the event with the given name
     */
    @Override
    public Event getEventByName(final String eventName) {
        return events.stream()
                .filter(event -> event.getName().equals(eventName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the merchandise with the given name, if it exists.
     * @param merchName the name of the merchandise
     * @return the merchandise with the given name
     */
    @Override
    public Merch getMerchByName(final String merchName) {
        return merch.stream()
                .filter(currentMerch -> currentMerch.getName().equals(merchName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Calculates the number of times the artist's songs were listened by the given user.
     * @param user the user to calculate the number of listens for
     * @return the number of times the artist's songs were listened by the given user
     */
    public int getListenByUser(final User user) {
        int times = 0;
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getArtist().equals(this.getName())) {
                times += song.getListenByUser(user);
            }
        }

        for (Song song : Library.getInstance().getRemovedSongs()) {
            if (song.getArtist().equals(this.getName())) {
                times += song.getListenByUser(user);
            }
        }
        return times;
    }
}
