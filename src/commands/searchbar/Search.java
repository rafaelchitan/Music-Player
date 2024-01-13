package commands.searchbar;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.files.Album;
import entities.files.Playlist;
import entities.files.Podcast;
import entities.files.Song;
import entities.users.User;
import entities.player.Player;
import fileio.input.CommandInput;
import fileio.output.SearchOutput;
import filters.Filter;

import java.util.ArrayList;
import java.util.List;

public class Search extends Command {
    private final Filter filter;

    private final String type;
    public Search(final CommandInput commandInput) {
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();
        this.type = commandInput.getType();
        this.filter = new Filter(commandInput.getFilters());
    }

    /**
     * Searches for a song by name.
     * @param name the name of the song
     * @return the list of songs that match the given name
     */
    private List<Entity> searchSongByName(final String name) {
        List<Entity> result = new ArrayList<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (song.getName().toLowerCase().startsWith(name.toLowerCase())) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Searches for a song by album name.
     * @param album the name of the album
     * @return the list of songs that match the given album name
     */
    private List<Entity> searchSongByAlbum(final String album) {
        List<Entity> result = new ArrayList<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (song.getAlbum().equals(album)) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Searches for a song by tags.
     * @param tags the list of tags
     * @return the list of songs that match the given tags
     */
    private List<Entity> searchSongByTags(final ArrayList<String> tags) {
        List<Entity> result = new ArrayList<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (song.getTags().containsAll(tags)) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Searches for a song by lyrics content.
     * @param lyrics part of the lyrics
     * @return the list of songs that match the given lyrics
     */
    private List<Entity> searchSongByLyrics(final String lyrics) {
        List<Entity> result = new ArrayList<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (song.getLyrics().toLowerCase().contains(lyrics.toLowerCase())) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Searches for a song by genre.
     * @param genre the genre of the song
     * @return the list of songs that match the given genre
     */
    private List<Entity> searchSongByGenre(final String genre) {
        List<Entity> result = new ArrayList<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Searches for a song by release year.
     * @param releaseYear condition for the release year
     * @return the list of songs that fulfills the given condition
     */
    private List<Entity> searchSongByReleaseYear(final String releaseYear) {
        List<Entity> result = new ArrayList<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (releaseYear.startsWith(">")
                    && song.getReleaseYear() > Integer.parseInt(releaseYear.substring(1))) {
                result.add(song);
            } else if (releaseYear.startsWith("<")
                    && song.getReleaseYear() < Integer.parseInt(releaseYear.substring(1))) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Searches for a song by artist.
     * @param artist the name of the artist
     * @return the list of songs that match the given artist
     */
    private List<Entity> searchSongByArtist(final String artist) {
        List<Entity> result = new ArrayList<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (song.getArtist().equals(artist)) {
                result.add(song);
            }
        }
        return result;
    }

    /**
     * Searches for a song by the given filter.
     * @param searchFilter the filter that contains all the conditions to be fulfilled
     * @return the list of songs that match the given filter
     */
    private List<Entity> searchSong(final Filter searchFilter) {
        List<Entity> result = null;
        if (searchFilter.getName() != null) {
            result = searchSongByName(searchFilter.getName());
        }
        if (searchFilter.getAlbum() != null) {
            if (result == null) {
                result = searchSongByAlbum(searchFilter.getAlbum());
            } else {
                result.retainAll(searchSongByAlbum(searchFilter.getAlbum()));
            }
        }
        if (searchFilter.getTags() != null) {
            if (result == null) {
                result = searchSongByTags(searchFilter.getTags());
            } else {
                result.retainAll(searchSongByTags(searchFilter.getTags()));
            }
        }
        if (searchFilter.getLyrics() != null) {
            if (result == null) {
                result = searchSongByLyrics(searchFilter.getLyrics());
            } else {
                result.retainAll(searchSongByLyrics(searchFilter.getLyrics()));
            }
        }
        if (searchFilter.getGenre() != null) {
            if (result == null) {
                result = searchSongByGenre(searchFilter.getGenre());
            } else {
                result.retainAll(searchSongByGenre(searchFilter.getGenre()));
            }
        }
        if (searchFilter.getReleaseYear() != null) {
            if (result == null) {
                result = searchSongByReleaseYear(searchFilter.getReleaseYear());
            } else {
                result.retainAll(searchSongByReleaseYear(searchFilter.getReleaseYear()));
            }
        }
        if (searchFilter.getArtist() != null) {
            if (result == null) {
                result = searchSongByArtist(searchFilter.getArtist());
            } else {
                result.retainAll(searchSongByArtist(searchFilter.getArtist()));
            }
        }
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * Searches for a podcast by name.
     * @param name the name of the podcast
     * @return the list of podcasts that match the given name
     */
    private List<Entity> searchPodcastByName(final String name) {
        List<Entity> result = new ArrayList<>();
        for (Podcast podcast: Library.getInstance().getPodcasts()) {
            if (podcast.getName().startsWith(name)) {
                result.add(podcast);
            }
        }
        return result;
    }

    /**
     * Searches for a podcast by its owner.
     * @param owner the name of the owner
     * @return the list of podcasts that match the given owner
     */
    private List<Entity> searchPodcastByOwner(final String owner) {
        List<Entity> result = new ArrayList<>();
        for (Podcast podcast: Library.getInstance().getPodcasts()) {
            if (podcast.getOwner().equals(owner)) {
                result.add(podcast);
            }
        }
        return result;
    }

    /**
     * Searches for a podcast by the given filter.
     * @param searchFilter the filter that contains all the conditions to be fulfilled
     * @return the list of podcasts that match the given filter
     */
    private List<Entity> searchPodcast(final Filter searchFilter) {
        List<Entity> result = null;
        if (searchFilter.getName() != null) {
            result = searchPodcastByName(searchFilter.getName());
        }
        if (searchFilter.getOwner() != null) {
            if (result == null) {
                result = searchPodcastByOwner(searchFilter.getOwner());
            } else {
                result.retainAll(searchPodcastByOwner(searchFilter.getOwner()));
            }
        }
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * Searches for a playlist by name.
     * @param name the name of the playlist
     * @param user the user that searches for the playlist
     * @return the list of playlists that match the given name
     */
    private List<Entity> searchPlaylistByName(final String name, final User user) {
        List<Entity> result = new ArrayList<>();
        for (Playlist playlist: user.getPlaylists()) {
            if (playlist.getName().startsWith(name)) {
                result.add(playlist);
            }
        }
        for (Playlist playlist: Library.getInstance().getPublicPlaylists()) {
            if (playlist.getName().startsWith(name) && playlist.getOwner() != user
                    && playlist.getVisibility().equals("public")) {
                result.add(playlist);
            }
        }
        return result;
    }

    /**
     * Searches for a playlist by its owner.
     * @param owner the name of the owner
     * @param user the user that searches for the playlist
     * @return the list of playlists that match the given owner
     */
    private List<Entity> searchPlaylistByOwner(final String owner, final User user) {
        List<Entity> result = new ArrayList<>();
        for (Playlist playlist: user.getPlaylists()) {
            if (playlist.getOwner().getUsername().equals(owner)) {
                result.add(playlist);
            }
        }
        for (Playlist playlist: Library.getInstance().getPublicPlaylists()) {
            if (playlist.getOwner().getUsername().equals(owner) && playlist.getOwner() != user
                    && playlist.getVisibility().equals("public")) {
                result.add(playlist);
            }
        }
        return result;
    }

    /**
     * Searches for a playlist by the given filter.
     * @param searchFilter the filter that contains all the conditions to be fulfilled
     * @param user the user that searches for the playlist
     * @return the list of playlists that match the given filter
     */
    private List<Entity> searchPlaylist(final Filter searchFilter, final User user) {
        List<Entity> result = null;
        if (searchFilter.getName() != null) {
            result = searchPlaylistByName(searchFilter.getName(), user);
        }
        if (searchFilter.getOwner() != null) {
            if (result == null) {
                result = searchPlaylistByOwner(searchFilter.getOwner(), user);
            } else {
                result.retainAll(searchPlaylistByOwner(searchFilter.getOwner(), user));
            }
        }
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * Executes the search command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        if (!Library.getInstance().getUserByName(username).isOnline()) {
            String message = username + " is offline.";
            return new SearchOutput(this, message).convertToJSON();
        }

        Player player = Library.getInstance().getUserByName(username).getPlayer();
        player.update(Library.getInstance().getUserByName(username), timestamp);
        player.reset();

        for (Song song: Library.getInstance().getSongs()) {
            song.getHasAdBreak().remove(Library.getInstance().getUserByName(username));
            song.getMarkedAdBreaks().remove(Library.getInstance().getUserByName(username));
        }

        List<Entity> result = switch (type) {
            case "song" -> searchSong(filter);
            case "podcast" -> searchPodcast(filter);
            case "playlist" -> searchPlaylist(filter, Library.getInstance().getUserByName(username));
            case "artist" -> searchArtist(filter, Library.getInstance().getUserByName(username));
            case "host" -> searchHost(filter, Library.getInstance().getUserByName(username));
            case "album" -> searchAlbum(filter, Library.getInstance().getUserByName(username));
            default -> new ArrayList<>();
        };

        User user = Library.getInstance().getUserByName(username);
        user.addSearchResults(result);

        return new SearchOutput(this, (ArrayList<Entity>) result).convertToJSON();
    }

    private List<Entity> searchArtist(final Filter searchFilter, final User user) {
        List<Entity> result = null;
        if (searchFilter.getName() != null) {
            result = new ArrayList<>(Library.getInstance().getUsers().stream()
                    .filter(artist -> artist.getType().equals("artist"))
                    .filter(artist -> artist.getUsername()
                            .toLowerCase().startsWith(searchFilter.getName().toLowerCase()))
                    .toList());
        }

        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    private List<Entity> searchHost(final Filter searchFilter, final User user) {
        List<Entity> result = null;
        if (searchFilter.getName() != null) {
            result = new ArrayList<>(Library.getInstance().getUsers().stream()
                    .filter(artist -> artist.getType().equals("host"))
                    .filter(artist -> artist.getUsername().startsWith(searchFilter.getName()))
                    .toList());
        }

        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    private List<Entity> searchAlbum(final Filter searchFilter, final User user) {
        List<Entity> result = null;
        ArrayList<Album> albums = new ArrayList<>();
        for (User artist: Library.getInstance().getUsers()) {
            if (artist.getType().equals("artist")) {
                albums.addAll(artist.getAlbums());
            }
        }

        if (searchFilter.getName() != null) {
            result = new ArrayList<>(albums.stream()
                    .filter(album -> album.getName().startsWith(searchFilter.getName()))
                    .toList());
        }

        if (searchFilter.getOwner() != null) {
            if (result == null) {
                result = new ArrayList<>(albums.stream()
                        .filter(album -> album.getArtist().equals(searchFilter.getOwner()))
                        .toList());
            } else {
                result.retainAll(new ArrayList<>(albums.stream()
                        .filter(album -> album.getArtist().equals(searchFilter.getOwner()))
                        .toList()));
            }
        }

        if (searchFilter.getDescription() != null) {
            if (result == null) {
                result = new ArrayList<>(albums.stream()
                        .filter(album -> album.getDescription()
                                .contains(searchFilter.getDescription()))
                        .toList());
            } else {
                result.retainAll(new ArrayList<>(albums.stream()
                        .filter(album -> album.getDescription()
                                .contains(searchFilter.getDescription()))
                        .toList()));
            }
        }

        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }
}
