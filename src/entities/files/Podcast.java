package entities.files;

import entities.Entity;
import entities.Library;
import entities.users.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;

@Getter @Setter
public class Podcast implements Entity {
    private String name;
    private String owner;
    private ArrayList<Episode> episodes;
    private int duration;

    public Podcast(final PodcastInput podcastInput) {
        name = podcastInput.getName();
        owner = podcastInput.getOwner();
        episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput));
        }
        duration = 0;
        for (Episode episode : episodes) {
            duration += episode.getDuration(null);
        }
    }

    public Podcast(final String name, final String owner,
                   final ArrayList<EpisodeInput> episodeInputs) {
        this.name = name;
        this.owner = owner;
        episodes = new ArrayList<>();

        for (EpisodeInput episodeInput : episodeInputs) {
            Episode newEpisode = new Episode(episodeInput);
            episodes.add(newEpisode);
        }

        duration = 0;
        for (Episode episode : episodes) {
            duration += episode.getDuration(null);
        }

        Library.getInstance().getPodcasts().add(this);
    }

    /**
     * Gets the current filetype
     * @return the type of the object.
     */
    public String objType() {
        return "podcast";
    }

    /**
     * Gets the number of times the user has listened to the podcast.
     * @param user the user to check the number of listens for
     * @return the number of times the user has listened to the podcast
     */
    public int getListenByUser(final User user) {
        return episodes.stream()
                .min(Comparator.comparingInt(e -> e.getListenByUser(user)))
                .get()
                .getListenByUser(user);
    }

    /**
     * Gets the duration of the podcast.
     * @param user the user to check the duration for
     * @return the duration of the podcast
     */
    public int getDuration(final User user) {
        return duration;
    }

}
