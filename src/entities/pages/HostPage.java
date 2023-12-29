package entities.pages;

import entities.files.Episode;
import entities.files.Podcast;
import entities.users.User;
import entities.users.specifics.Announcement;

import java.util.List;

public class HostPage extends Page {

    private List<Podcast> podcasts;

    public HostPage(final User user) {
        this.user = user;
        update();
    }

    /**
     * Updates the list of podcasts.
     */
    public void update() {
        podcasts = user.getPodcasts();
    }

    /**
     * Prints the current page to the user
     * @return the string representing the page
     */
    @Override
    public String toString() {
        update();
        StringBuilder stringBuilder = new StringBuilder();
        boolean toReplace = false;

        stringBuilder.append("Podcasts:\n\t[");
        for (Podcast podcast: podcasts) {
            toReplace = true;
            stringBuilder.append(podcast.getName()).append(":\n\t[");

            boolean toReplaceEpisodes = false;
            for (Episode episode: podcast.getEpisodes()) {
                stringBuilder.append(episode.getName()).append(" - ")
                        .append(episode.getDescription()).append(", ");
                toReplaceEpisodes = true;
            }

            if (toReplaceEpisodes) {
                stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]\n, ");
            } else {
                stringBuilder.append("]\n, ");
            }
        }

        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]\n\n");
        } else {
            stringBuilder.append("]\n\n");
        }

        stringBuilder.append("Announcements:\n\t[");
        toReplace = false;

        for (Announcement announcement: user.getAnnouncements()) {
            toReplace = true;
            stringBuilder.append(announcement.getName()).append(":\n\t")
                    .append(announcement.getDescription()).append(", ");
        }

        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "\n]");
        } else {
            stringBuilder.append("\n]");
        }

        return stringBuilder.toString();
    }
}
