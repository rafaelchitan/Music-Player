package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.AudioFile;
import entities.files.Album;
import entities.files.Playlist;
import entities.files.Song;
import entities.files.Podcast;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class DeleteUserCommand extends Command {
    public DeleteUserCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();

    }

    /**
     * Deletes a user from the library
     * @return the JSON result
     */
    @Override
    public ObjectNode execute() {
        if (testActive()) {
            return new CommandOutput(this, username + " can't be deleted.").convertToJSON();
        }

        User user = Library.getInstance().getUserByName(username);

        for (Playlist playlist: user.getFollowedPlaylists()) {
            playlist.setFollowers(playlist.getFollowers() - 1);
        }

        if (user.getType().equals("artist")) {
            for (Album album: user.getAlbums()) {
                for (Song song: album.getSongs()) {
                    for (User auxUser: Library.getInstance().getUsers()) {
                        auxUser.getLikedSongs().remove(song);
                    }
                    Library.getInstance().getSongs().remove(song);
                }
                Library.getInstance().getAlbums().remove(album);
            }
            Library.getInstance().getUsers().remove(user);
        }

        for (Playlist playlist: user.getPlaylists()) {
            for (User currentUser: Library.getInstance().getUsers()) {
                currentUser.getFollowedPlaylists().remove(playlist);
            }
        }

        return new CommandOutput(this, username + " was successfully deleted.").convertToJSON();
    }

    /**
     * Checks if any other user is currently using any of the user's resources
     * @return true if any other user is currently using any of the user's resources
     */
    private boolean testActive() {

        for (User user: Library.getInstance().getUsers()) {
            if (user.getName().equals(username)) {
                continue;
            }

            if (user.getCurrentPage().equals(Library.getInstance()
                    .getUserByName(username).getPublicPage())) {
                return true;
            }

            user.getPlayer().update(user, timestamp);
            AudioFile activeFile = user.getPlayer().getActiveFile();
            if (activeFile == null) {
                continue;
            }

            if (activeFile.objType().equals("song")) {
                if (((Song) activeFile).getArtist().equals(username)) {
                    return true;
                }
            } else if (activeFile.objType().equals("episode")) {
                if (((Podcast) user.getPlayer().getCurrentFile()).getOwner().equals(username)) {
                    return true;
                }
            }

            if (user.getPlayer().getCurrentFile().objType().equals("playlist")) {
                if (((Playlist) user.getPlayer().getCurrentFile())
                        .getOwner().getName().equals(username)) {
                    return true;
                }
            }

            if (user.getCurrentPage().getUser().getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
