package commands.audiofile.album;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.files.Album;
import entities.files.Playlist;
import entities.files.Song;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class RemoveAlbumCommand extends Command {
    private String name;

    public RemoveAlbumCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.name = commandInput.getName();
    }

    /**
     * Removes an album from the user's list of albums
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);

        if (!user.getType().equals("artist")) {
            return new CommandOutput(this, username
                    + " is not an artist.").convertToJSON();
        }

        if (user.getAlbumByName(name) == null) {
            return new CommandOutput(this, username
                    + " doesn't have an album with the given name.").convertToJSON();
        }

        if (testActive()) {
            return new CommandOutput(this, username
                    + " can't delete this album.").convertToJSON();
        }

        for (Song song: user.getAlbumByName(name).getSongs()) {
            for (User currentUser: Library.getInstance().getUsers()) {
                currentUser.getLikedSongs().remove(song);
                for (Playlist playlist: currentUser.getPlaylists()) {
                    playlist.getSongs().remove(song);
                }
            }

            Library.getInstance().getSongs().remove(song);
        }

        Library.getInstance().getAlbums().remove(user.getAlbumByName(name));
        user.getAlbums().remove(user.getAlbumByName(name));
        return new CommandOutput(this, username
                + " deleted the album successfully.").convertToJSON();
    }

    /**
     * Checks if the album is active
     * @return true if the album is active, false otherwise
     */
    private boolean testActive() {

        for (User user: Library.getInstance().getUsers()) {
            if (user.getName().equals(username)) {
                continue;
            }

            user.getPlayer().update(user, timestamp);
            Entity currentFile = user.getPlayer().getCurrentFile();

            if (currentFile == null) {
                continue;
            }

            switch (currentFile.objType()) {
                case "song":
                    if (((Song) currentFile).getArtist().equals(username)) {
                        return true;
                    }
                    break;
                case "album":
                    if (((Album) currentFile).getArtist().equals(username)) {
                        return true;
                    }
                    break;
                case "playlist":
                    for (Song song: ((Playlist) currentFile).getSongs()) {
                        if (song.getArtist().equals(username)) {
                            return true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

}
