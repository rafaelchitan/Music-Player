package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public abstract class Command {
    protected String username;
    protected int timestamp;
    protected String command;

    /**
     * Executes the command.
     * @return the JSON representation of the command output
     */
    public abstract ObjectNode execute();
}
