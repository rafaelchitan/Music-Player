package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.users.specifics.Announcement;
import entities.Library;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class AddAnnouncementCommand extends Command {
    private String description;
    private String name;

    public AddAnnouncementCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.description = commandInput.getDescription();
        this.name = commandInput.getName();
    }

    /**
     * Adds a new announcement to the user's list of announcements
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        if (!Library.getInstance().getUserByName(username).getType().equals("host")) {
            return new CommandOutput(this, username + " is not a host.").convertToJSON();
        }

        if (Library.getInstance().getUserByName(username).getAnnouncementByName(name) != null) {
            return new CommandOutput(this, username
                    + " has already added an announcement with this name.").convertToJSON();
        }

        Announcement announcement = new Announcement(Library.getInstance()
                .getUserByName(username), name, description);
        Library.getInstance().getUserByName(username).getAnnouncements().add(announcement);

        return new CommandOutput(this, username
                + " has successfully added new announcement.").convertToJSON();
    }
}
