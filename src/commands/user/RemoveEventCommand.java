package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class RemoveEventCommand extends Command {
    private String name;

    public RemoveEventCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.name = commandInput.getName();
    }

    /**
     * Removes an event from the user's list of events
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        if (!Library.getInstance().getUserByName(username).getType().equals("artist")) {
            return new CommandOutput(this, username
                    + " is not an artist.").convertToJSON();
        }

        if (Library.getInstance().getUserByName(username).getEventByName(name) == null) {
            return new CommandOutput(this, username
                    + " doesn't have an event with the given name.").convertToJSON();
        }

        Library.getInstance().getUserByName(username).getEvents()
                .remove(Library.getInstance().getUserByName(username).getEventByName(name));
        return new CommandOutput(this, username
                + " deleted the event successfully.").convertToJSON();
    }
}
