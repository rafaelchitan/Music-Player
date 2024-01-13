package entities.users;

import entities.Entity;
import entities.files.Album;
import entities.files.Playlist;
import entities.files.Podcast;
import entities.files.Song;
import entities.pages.HomePage;
import entities.pages.LikedContentPage;
import entities.pages.Page;
import entities.player.Player;
import entities.users.specifics.Announcement;
import entities.users.specifics.Event;
import entities.users.specifics.Merch;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import notifications.NotificationTemplate;
import notifications.Publisher;
import notifications.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter @Getter
public class User implements Entity, Subscriber {
    protected String username;
    protected int age;
    protected String city;

    protected List<Entity> searchbarResults;
    protected Entity selectedFile;
    protected Entity lastRecommendation;
    protected Player player = new Player();

    protected List<Song> likedSongs = new ArrayList<>();
    protected List<Playlist> playlists = new ArrayList<>();
    protected List<Playlist> followedPlaylists = new ArrayList<>();
    protected List<Album> albums = new ArrayList<>();

    protected List<Merch> boughtMerch = new ArrayList<>();

    protected HashMap<Podcast, Integer> podcastHistory = new HashMap<>();

    protected String status = "online";
    protected String type = "user";

    protected boolean isOnline = true;
    protected boolean isPremium = false;
    protected boolean isFake = false;

    protected int premiumStart = 0;

    protected Page publicPage = new HomePage(this);
    protected Page likedContentPage = new LikedContentPage(this);
    protected Page currentPage = publicPage;
    protected List<Page> pageHistory = new ArrayList<>();
    private int historyIndex = -1;

    protected double songMoney = 0.0;
    protected double merchMoney = 0.0;
    protected ArrayList<NotificationTemplate> notifications = new ArrayList<>();

    public User(final String username) {
        this.username = username;
        pageHistory.add(currentPage);
    }

    public User(final UserInput userInput) {
        username = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
        pageHistory.add(currentPage);
    }

    public User(final String username, final int age, final String city, final String type) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.type = type;
        pageHistory.add(currentPage);
    }

    /**
     * Adds the searchbar results to the user who searched.
     * @param results top 5 searchbar results
     */
    public void addSearchResults(final List<Entity> results) {
        searchbarResults = results;
    }

    /**
     * Adds the selected file from the search results to the user.
     * @param selectedFile the selected file from the search results
     */
    public void setSelectedFile(final Entity selectedFile) {
        this.selectedFile = selectedFile;
        this.searchbarResults = null;
    }

    /**
     * Gets the playlist by its name, if it exists.
     * @param playlistName the name of the playlist
     * @return the playlist object if found, null otherwise
     */
    public Playlist getPlaylistByName(final String playlistName) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(playlistName)) {
                return playlist;
            }
        }
        return null;
    }

    /**
     * Gets the list of albums, if the user is an artist
     * @return the list of artist's albums, null otherwise
     */
    public List<Album> getAlbums() {
        return new ArrayList<>();
    }

    /**
     * Gets the list of podcasts, if the user is a host
     * @return the list of host's podcasts, null otherwise
     */
    public List<Podcast> getPodcasts() {
        return null;
    }

    /**
     * Gets an album by its name, if it exists.
     * @param albumName the name of the album
     * @return the album object if found, null otherwise
     */
    public Album getAlbumByName(final String albumName) {
        return null;
    }

    /**
     * Gets the list of events, if the user is an artist
     * @return the list of artist's events, null otherwise
     */
    public List<Event> getEvents() {
        return null;
    }

    /**
     * Gets the event by its name, if it exists.
     * @param eventName the name of the event
     * @return the event object if found, null otherwise
     */
    public Event getEventByName(final String eventName) {
        return null;
    }

    /**
     * Gets the user's name.
     * @return the user's name
     */
    @Override
    public String getName() {
        return username;
    }

    /**
     * Gets the user's type.
     * @return the user's type
     */
    @Override
    public String objType() {
        return "user";
    }
    @Override
    public int getDuration(final User user) {
        return 0;
    }

    /**
     * Gets the list of merch, if the user is a host
     * @return the list of host's merch, null otherwise
     */
    public List<Merch> getMerch() {
        return null;
    }

    /**
     * Gets the merch by its name, if it exists.
     * @param merchName the name of the merch
     * @return the merch object if found, null otherwise
     */
    public Merch getMerchByName(final String merchName) {
        return null;
    }

    /**
     * Gets the list of podcasts, if the user is a host
     * @return the list of host's podcasts, null otherwise
     */
    public Podcast getPodcastByName(final String podcastName) {
        return null;
    }

    /**
     * Gets the list of announcements, if the user is a host
     * @return the list of host's announcements, null otherwise
     */
    public List<Announcement> getAnnouncements() {
        return null;
    }

    /**
     * Gets the announcement by its name, if it exists.
     * @param podcastName the name of the announcement
     * @return the announcement object if found, null otherwise
     */
    public Announcement getAnnouncementByName(final String podcastName) {
        return null;
    }

    /**
     * Add the notification to the user's notifications list.
     */
    public void update(final NotificationTemplate notification) {
        notifications.add(notification);
    }

    /**
     * Gets the publisher for the current user
     * @return current user's publisher
     */
    public Publisher getPublisher() {
        return null;
    }

    /**
     * Adds a new page to the history.
     * @param nextPage the page to be added
     */
    public void addPage(final Page nextPage) {
        pageHistory.add(nextPage);
        currentPage = nextPage;
        historyIndex = pageHistory.size() - 1;
    }

    /**
     * Goes to the next page.
     * @return true if the next page exists, false otherwise
     */
    public boolean goNextPage() {
        if (historyIndex == pageHistory.size() - 1) {
            return false;
        }

        currentPage = pageHistory.get(++historyIndex);
        return true;
    }

    /**
     * Goes to the previous page.
     * @return true if the previous page exists, false otherwise
     */
    public boolean goPreviousPage() {
        if (historyIndex == 0) {
            return false;
        }

        currentPage = pageHistory.get(--historyIndex);
        return true;
    }

    /**
     * Updates the song reccomandations.
     * @param song the song to be added to the reccomandations
     */
    public void updateSongsReccomandations(final Song song) {
        ((HomePage) publicPage).getSongReccomandations().add(song);
    }

    /**
     * Updates the playlist reccomandations.
     * @param playlist the playlist to be added to the reccomandations
     */
    public void updatePlaylistReccomandations(final Playlist playlist) {
        ((HomePage) publicPage).getPlaylistReccomandations().add(playlist);
    }
}
