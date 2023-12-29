package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class RemoveAnnouncementCommand extends Command {
    private String name;

    public RemoveAnnouncementCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.name = commandInput.getName();
    }

    /**
     * Removes an announcement from the user's list of announcements
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        if (!Library.getInstance().getUserByName(username).getType().equals("host")) {
            return new CommandOutput(this, username + " is not a host.").convertToJSON();
        }

        if (Library.getInstance().getUserByName(username).getAnnouncementByName(name) == null) {
            return new CommandOutput(this, username
                    + " has no announcement with the given name.").convertToJSON();
        }

        Library.getInstance().getUserByName(username).getAnnouncements()
                .remove(Library.getInstance().getUserByName(username).getAnnouncementByName(name));
        return new CommandOutput(this, username
                + " has successfully deleted the announcement.").convertToJSON();
    }
}
