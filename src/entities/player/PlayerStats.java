package entities.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlayerStats {
    private String name;
    private int remainedTime;
    private String repeat = "No Repeat";
    private boolean shuffle = false;
    private boolean paused = false;
    private boolean isOffline = false;

    /**
     * Convert the playerstats fields to JSON format.
     * @return the JSON representation of the playerstats
     */
    public ObjectNode convertToJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", this.name);
        objectNode.put("remainedTime", this.remainedTime);
        objectNode.put("repeat", this.repeat);
        objectNode.put("shuffle", this.shuffle);
        objectNode.put("paused", this.paused);
        return objectNode;
    }

    /**
     * Reset the playerstats fields to the initial values.
     */
    public void reset() {
        this.name = "";
        this.remainedTime = 0;
        this.repeat = "No Repeat";
        this.shuffle = false;
        this.paused = true;
    }
}
