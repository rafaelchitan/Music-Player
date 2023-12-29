package entities.users;

import entities.users.specifics.Announcement;
import entities.files.Podcast;
import entities.pages.HostPage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
public class Host extends User {
    private List<Podcast> podcasts = new ArrayList<>();
    private List<Announcement> announcements = new ArrayList<>();

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
}
