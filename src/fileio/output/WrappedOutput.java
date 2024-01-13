package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class WrappedOutput extends CommandOutput {
    private final ObjectNode objectNode;
    private boolean noData = true;

    public WrappedOutput(final Command command) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.objectNode = new ObjectMapper().createObjectNode();
    }

    /**
     * Adds a new field to the JSON object, containing the top 5 entities from a top.
     * @param type the type of output that will be displayed in the JSON
     * @param top the hashmap containing the top objects
     * @return an instance of the current object, with updated fields
     */
    public WrappedOutput addObject(final String type, final HashMap<Entity, Integer> top) {
        ArrayList<Entity> sorted = new ArrayList<>(top.keySet());
        sorted.sort((s1, s2) -> {
            if (top.get(s1).equals(top.get(s2))) {
                return s1.getName().compareTo(s2.getName());
            }
            return top.get(s2) - top.get(s1);
        });

        ObjectNode node = new ObjectMapper().createObjectNode();
        for (Entity result
                : sorted.subList(0, Math.min(Constants.MAX_COUNT, sorted.size()))) {
            node.put(result.getName(), top.get(result));
            noData = false;
        }
        objectNode.set(type, node);
        return this;
    }

    /**
     * Adds a new field to the JSON object, containing the top 5 strings from a top.
     * @param type the type of output that will be displayed in the JSON
     * @param top the hashmap containing the top objects
     * @return an instance of the current object, with updated fields
     */
    public WrappedOutput addString(final String type, final HashMap<String, Integer> top) {
        ArrayList<String> sorted = new ArrayList<>(top.keySet());
        sorted.sort((s1, s2) -> {
            if (top.get(s1).equals(top.get(s2))) {
                return s1.compareTo(s2);
            }
            return top.get(s2) - top.get(s1);
        });

        ObjectNode node = new ObjectMapper().createObjectNode();
        for (String result
                : sorted.subList(0, Math.min(Constants.MAX_COUNT, sorted.size()))) {
            node.put(result, top.get(result));
            noData = false;
        }
        objectNode.set(type, node);
        return this;
    }

    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("command", this.command);
        if (user != null) {
            node.put("user", this.user);
        }
        node.put("timestamp", this.timestamp);
        if (noData) {
            node.put("message", "No data to show for "
                    + Library.getInstance().getUserByName(user).getType()
                    + " " + user + ".");
            return node;
        }
        node.set("result", this.objectNode);
        return node;
    }

    /**
     * Adds a new field to the JSON object, containing a string.
     * @param type the type of output that will be displayed in the JSON
     * @param value the value string that will be displayed in the JSON
     * @return an instance of the current object, with updated fields
     */
    public WrappedOutput addField(final String type, final int value) {
        objectNode.put(type, value);
        return this;
    }

    /**
     * Adds a new field to the JSON object, containing a string and the top 5 fans.
     * @param type the type of output that will be displayed in the JSON
     * @param top the hashmap containing the top objects
     * @return an instance of the current object, with updated fields
     */
    public WrappedOutput addFans(final String type, final HashMap<Entity, Integer> top) {
        ArrayList<Entity> sorted = new ArrayList<>(top.keySet());
        sorted.sort((s1, s2) -> {
            if (top.get(s1).equals(top.get(s2))) {
                return s1.getName().compareTo(s2.getName());
            }
            return top.get(s2) - top.get(s1);
        });

        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (Entity
                result: sorted.subList(0, Math.min(Constants.MAX_COUNT, sorted.size()))) {
            arrayNode.add(result.getName());
        }

        this.objectNode.set(type, arrayNode);
        return this;
    }
}
