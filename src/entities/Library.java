package entities;

import entities.files.Album;
import entities.files.Playlist;
import entities.files.Podcast;
import entities.files.Song;
import entities.users.Artist;
import entities.users.User;
import entities.users.specifics.Event;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import utils.ListenedEntry;
import java.util.ArrayList;

@Getter @Setter
public final class Library {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Podcast> podcasts = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Playlist> publicPlaylists = new ArrayList<>();
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<ListenedEntry> listenedEntries = new ArrayList<>();
    private ArrayList<Song> removedSongs = new ArrayList<>();

    @Getter
    private static Library instance = new Library();

    private Library() { }

    /**
     * Updates the library with the given input.
     * @param libraryInput the input to update the library with
     */
    public void updateLibrary(final LibraryInput libraryInput) {
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        users = new ArrayList<>();
        publicPlaylists = new ArrayList<>();
        albums = new ArrayList<>();
        events = new ArrayList<>();

        for (SongInput songInput : libraryInput.getSongs()) {
            Album album = albums.stream()
                    .filter(a -> a.getName().equals(songInput.getAlbum()))
                    .filter(a -> a.getArtist().equals(songInput.getArtist()))
                    .findFirst()
                    .orElse(null);
            if (album == null) {
                album = new Album(songInput.getAlbum(), songInput.getArtist(),
                        songInput.getReleaseYear(), "", new ArrayList<>());
                albums.add(album);
            }

            Song song = new Song(songInput);
            songs.add(song);
            album.getSongs().add(song);
            if (Library.getInstance().getUserByName(songInput.getArtist()) == null) {
                users.add(new Artist(songInput.getArtist()));
            }
        }

        for (PodcastInput podcastInput : libraryInput.getPodcasts()) {
            podcasts.add(new Podcast(podcastInput));
        }

        for (UserInput userInput : libraryInput.getUsers()) {
            users.add(new User(userInput));
        }
    }

    /**
     * Gets the user object by its username.
     * @param username the username of the user
     * @return the user object if found, null otherwise
     */
    public User getUserByName(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets the song object by its name.
     * @param name the name of the song
     * @return the song object if found, null otherwise
     */
    public Song getSongByName(final String name) {
        return songs.stream()
                .filter(song -> song.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
