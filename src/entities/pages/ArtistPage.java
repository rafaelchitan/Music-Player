package entities.pages;

import entities.files.Album;
import entities.users.User;
import entities.users.specifics.Event;
import entities.users.specifics.Merch;

import java.util.List;

public class ArtistPage extends Page {

    private List<Album> albums;
    private List<Merch> merch;
    private List<Event> events;

    public ArtistPage(final User user) {
        this.user = user;
        update();
    }

    /**
     * Updates the lists of albums, merch and events.
     */
    public void update() {
        albums = user.getAlbums();
        merch = user.getMerch();
        events = user.getEvents();
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

        stringBuilder.append("Albums:\n\t[");
        for (Album album : albums) {
            stringBuilder.append(album.getName()).append(", ");
            toReplace = true;
        }

        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]\n\n");
        } else {
            stringBuilder.append("]\n\n");
        }

        toReplace = false;
        stringBuilder.append("Merch:\n\t[");
        for (Merch currentMerch : merch) {
            stringBuilder.append(currentMerch.getName()).append(" - ")
                    .append(currentMerch.getPrice()).append(":\n\t")
                    .append(currentMerch.getDescription()).append(", ");
            toReplace = true;
        }

        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]\n\n");
        } else {
            stringBuilder.append("]\n\n");
        }

        toReplace = false;
        stringBuilder.append("Events:\n\t[");
        for (Event event : events) {
            stringBuilder.append(event.getName()).append(" - ")
                    .append(event.getDate()).append(":\n\t")
                    .append(event.getDescription()).append(", ");
            toReplace = true;
        }

        if (toReplace) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]");
        } else {
            stringBuilder.append("]");
        }

        return stringBuilder.toString();
    }
}
