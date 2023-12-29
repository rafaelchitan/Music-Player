package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import constants.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter @Setter
public class SearchOutput extends CommandOutput {
    private List<String> results;

    public SearchOutput(final Command command, final ArrayList<Entity> results) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.results = new ArrayList<>();

        for (int i = 0; i < Constants.MAX_SEARCH_RESULTS && i < results.size(); i++) {
            this.results.add(results.get(i).getName());
        }

        this.message = "Search returned "
                + Math.min(results.size(), Constants.MAX_SEARCH_RESULTS) + " results";
    }

    public SearchOutput(final Command command, final String message) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.results = new ArrayList<>();
        this.message = message;
    }


    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = super.convertToJSON();
        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (String result: results) {
            arrayNode.add(result);
        }
        objectNode.set("results", arrayNode);
        return objectNode;
    }
}
