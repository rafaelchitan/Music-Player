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

    public List<Album> getAlbums() {
        return new ArrayList<>();
    }

    public List<Podcast> getPodcasts() {
        return null;
    }

    public Album getAlbumByName(final String albumName) {
        return null;
    }

    public List<Event> getEvents() {
        return null;
    }

    public Event getEventByName(final String eventName) {
        return null;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String objType() {
        return "user";
    }

    @Override
    public int getDuration() {
        return 0;
    }

    /**
     * Gets the list of merch, if the user is a host
     * @return the list of host's merch, null otherwise
     */
    public List<Merch> getMerch() {
        return null;
    }

    public Merch getMerchByName(final String merchName) {
        return null;
    }

    public Podcast getPodcastByName(final String podcastName) {
        return null;
    }

    public List<Announcement> getAnnouncements() {
        return null;
    }

    public Announcement getAnnouncementByName(final String podcastName) {
        return null;
    }

    public void update(final NotificationTemplate notification) {
        notifications.add(notification);
    }

    public Publisher getPublisher() {
        return null;
    }

    public void addPage(Page nextPage) {
        int index = pageHistory.indexOf(currentPage);
        pageHistory = pageHistory.subList(0, index + 1);
        pageHistory.add(nextPage);
        currentPage = nextPage;
    }

    public boolean goNextPage() {
        int index = pageHistory.indexOf(currentPage);
        if (index == pageHistory.size() - 1) {
            return false;
        }

        currentPage = pageHistory.get(index + 1);
        return true;
    }

    public boolean goPreviousPage() {
        int index = pageHistory.indexOf(currentPage);
        if (index == 0) {
            return false;
        }

        currentPage = pageHistory.get(index - 1);
        return true;
    }

    public void updateSongsReccomandations(final Song song) {
        ((HomePage) publicPage).getSongReccomandations().add(song);
    }

    public void updatePlaylistReccomandations(Playlist playlist) {
        ((HomePage) publicPage).getPlaylistReccomandations().add(playlist);
    }
}
