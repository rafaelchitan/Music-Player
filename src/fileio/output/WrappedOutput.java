package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.files.Album;
import entities.files.AudioFile;
import entities.files.Song;

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

    public WrappedOutput addObject(String type, HashMap<Entity, Integer> top) {
        ArrayList<Entity> sorted = new ArrayList<>(top.keySet());
        sorted.sort((s1, s2) -> {
            if (top.get(s1).equals(top.get(s2))) {
                return s1.getName().compareTo(s2.getName());
            }
            return top.get(s2) - top.get(s1);
        });

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        for (Entity result: sorted.subList(0, Math.min(5, sorted.size()))) {
            objectNode.put(result.getName(), top.get(result));
            noData = false;
        }
        this.objectNode.set(type, objectNode);
        return this;
    }

    public WrappedOutput addString(String type, HashMap<String, Integer> top) {
        ArrayList<String> sorted = new ArrayList<>(top.keySet());
        sorted.sort((s1, s2) -> {
            if (top.get(s1).equals(top.get(s2))) {
                return s1.compareTo(s2);
            }
            return top.get(s2) - top.get(s1);
        });

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        for (String result: sorted.subList(0, Math.min(5, sorted.size()))) {
            objectNode.put(result, top.get(result));
            noData = false;
        }
        this.objectNode.set(type, objectNode);
        return this;
    }

    /**
     * Converts the object to JSON format.
     * @return the JSON representation of the object
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", this.command);
        if (user != null) {
            objectNode.put("user", this.user);
        }
        objectNode.put("timestamp", this.timestamp);
        if (noData) {
            objectNode.put("message", "No data to show for " + Library.getInstance().getUserByName(user).getType() + " " + user + ".");
            return objectNode;
        }
        objectNode.set("result", this.objectNode);
        return objectNode;
    }

    public WrappedOutput addField(String type, int value) {
        objectNode.put(type, value);
        return this;
    }

    public WrappedOutput addFans(String type, HashMap<Entity, Integer> top) {
        ArrayList<Entity> sorted = new ArrayList<>(top.keySet());
        sorted.sort((s1, s2) -> {
            if (top.get(s1).equals(top.get(s2))) {
                return s1.getName().compareTo(s2.getName());
            }
            return top.get(s2) - top.get(s1);
        });

        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (Entity result: sorted.subList(0, Math.min(5, sorted.size()))) {
            arrayNode.add(result.getName());
        }

        this.objectNode.set(type, arrayNode);
        return this;
    }
}
