package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.files.Album;
import entities.files.Song;

import java.util.List;

public class ShowAlbumsOutput extends CommandOutput {
    private final List<Album> albums;
    public ShowAlbumsOutput(final Command command, final List<Album> albums) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.albums = albums;
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

        ArrayNode albumsNode = new ObjectMapper().createArrayNode();
        for (Album album: albums) {
            ObjectNode albumsObjectNode = new ObjectMapper().createObjectNode();
            ArrayNode songsArrayNode = new ObjectMapper().createArrayNode();
            for (Song song: album.getSongs()) {
                songsArrayNode.add(song.getName());
            }
            albumsObjectNode.put("name", album.getName());
            albumsObjectNode.set("songs", songsArrayNode);
            albumsNode.add(albumsObjectNode);
        }
        objectNode.set("result", albumsNode);

        return objectNode;
    }
}
