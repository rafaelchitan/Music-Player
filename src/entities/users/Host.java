package entities.users;

import entities.Library;
import entities.files.Episode;
import entities.files.Song;
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

    @Override
    public Podcast getPodcastByName(final String podcastName) {
        return podcasts.stream()
                .filter(podcast -> podcast.getName().equals(podcastName))
                .findFirst()
                .orElse(null);
    }

    public Announcement getAnnouncementByName(final String announcementName) {
        return announcements.stream()
                .filter(podcast -> podcast.getName().equals(announcementName))
                .findFirst()
                .orElse(null);
    }

    public int getListenByUser(User user) {
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
