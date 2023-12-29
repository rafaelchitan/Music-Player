package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.files.Episode;
import entities.files.Podcast;

import java.util.List;

public class ShowPodcastsOutput extends CommandOutput {
    private final List<Podcast> podcasts;
    public ShowPodcastsOutput(final Command command, final List<Podcast> podcasts) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.podcasts = podcasts;
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
        for (Podcast podcast: podcasts) {
            ObjectNode albumsObjectNode = new ObjectMapper().createObjectNode();
            ArrayNode episodesArrayNode = new ObjectMapper().createArrayNode();
            for (Episode episode: podcast.getEpisodes()) {
                episodesArrayNode.add(episode.getName());
            }
            albumsObjectNode.put("name", podcast.getName());
            albumsObjectNode.set("episodes", episodesArrayNode);
            albumsNode.add(albumsObjectNode);
        }
        objectNode.set("result", albumsNode);

        return objectNode;
    }
}
