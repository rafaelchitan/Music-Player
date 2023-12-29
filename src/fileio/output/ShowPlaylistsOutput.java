package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.files.Playlist;
import entities.files.Song;
import java.util.List;

public class ShowPlaylistsOutput extends CommandOutput {
    private final List<Playlist> playlists;
    public ShowPlaylistsOutput(final Command command, final List<Playlist> playlists) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.playlists = playlists;
    }

    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", this.command);
        objectNode.put("user", this.user);
        objectNode.put("timestamp", this.timestamp);

        ArrayNode playlistsArrayNode = new ObjectMapper().createArrayNode();
        for (Playlist playlist: playlists) {
            ObjectNode playlistObjectNode = new ObjectMapper().createObjectNode();
            ArrayNode songsArrayNode = new ObjectMapper().createArrayNode();
            for (Song song: playlist.getSongs()) {
                songsArrayNode.add(song.getName());
            }
            playlistObjectNode.put("name", playlist.getName());
            playlistObjectNode.set("songs", songsArrayNode);
            playlistObjectNode.put("visibility", playlist.getVisibility());
            playlistObjectNode.put("followers", playlist.getFollowers());
            playlistsArrayNode.add(playlistObjectNode);
        }
        objectNode.set("result", playlistsArrayNode);

        return objectNode;
    }
}
