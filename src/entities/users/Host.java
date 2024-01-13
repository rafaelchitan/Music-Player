package entities.users;

import entities.Library;
import entities.files.Episode;
import entities.users.specifics.Announcement;
import entities.files.Podcast;
import entities.pages.HostPage;
import lombok.Getter;
import lombok.Setter;
import notifications.Publisher;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
public class Host extends User {
    private List<Podcast> podcasts = new ArrayList<>();
    private List<Announcement> announcements = new ArrayList<>();

    private Publisher publisher = new Publisher();

    public Host(final String username, final int age, final String city, final String type) {
        super(username, age, city, type);
        publicPage = new HostPage(this);
    }

    /**
     * Gets the podcast with the given name, if it exists.
     * @param podcastName the name of the podcast
     * @return the podcast with the given name
     */
    @Override
    public Podcast getPodcastByName(final String podcastName) {
        return podcasts.stream()
                .filter(podcast -> podcast.getName().equals(podcastName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the announcement with the given name, if it exists.
     * @param announcementName the name of the announcement
     * @return the announcement with the given name
     */
    public Announcement getAnnouncementByName(final String announcementName) {
        return announcements.stream()
                .filter(podcast -> podcast.getName().equals(announcementName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the number of times the user has listened to the host.
     * @param user the user to check the number of listens for
     * @return the number of times the user has listened to the host
     */
    public int getListenByUser(final User user) {
        int times = 0;
        for (Podcast podcast : Library.getInstance().getPodcasts()) {
            if (podcast.getOwner().equals(this.getName())) {
                for (Episode episode : podcast.getEpisodes()) {
                    times += episode.getListenByUser(user);
                }
            }
        }
        return times;
    }
}
