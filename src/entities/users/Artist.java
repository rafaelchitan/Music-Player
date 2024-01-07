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

    @Override
    public Album getAlbumByName(final String albumName) {
        return albums.stream()
                .filter(album -> album.getName().equals(albumName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Event getEventByName(final String eventName) {
        return events.stream()
                .filter(event -> event.getName().equals(eventName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Merch getMerchByName(final String merchName) {
        return merch.stream()
                .filter(currentMerch -> currentMerch.getName().equals(merchName))
                .findFirst()
                .orElse(null);
    }

    public int getListenByUser(User user) {
        int times = 0;
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getArtist().equals(this.getName())) {
                times += song.getListenByUser(user);
            }
        }
        return times;
    }
}
