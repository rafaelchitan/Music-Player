package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import utils.Constants;
import entities.users.specifics.Event;
import entities.Library;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;
import notifications.NotificationTemplate;

public class AddEventCommand extends Command {
    private String description;
    private String date;
    private String name;

    public AddEventCommand(final CommandInput commandInput) {

        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.description = commandInput.getDescription();
        this.date = commandInput.getDate();
        this.name = commandInput.getName();
    }

    /**
     * Adds a new event to the user's list of events
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        if (!Library.getInstance().getUserByName(username).getType().equals("artist")) {
            return new CommandOutput(this, username + " is not an artist.").convertToJSON();
        }

        if (Library.getInstance().getUserByName(username).getEventByName(name) != null) {
            return new CommandOutput(this, username
                    + "has another event with the same name.")
                    .convertToJSON();
        }

        String message = "Event for " + username + " does not have a valid date.";
        if (!date.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
            return new CommandOutput(this, message).convertToJSON();
        }

        if (date.substring(Constants.YEAR_FIRST_DIGIT, Constants.YEAR_LAST_DIGIT)
                .compareTo("2023") > 0
                || date.substring(Constants.YEAR_FIRST_DIGIT, Constants.YEAR_LAST_DIGIT)
                .compareTo("1900") < 0) {
            return new CommandOutput(this, message).convertToJSON();
        }

        if (date.substring(Constants.MONTH_FIRST_DIGIT, Constants.MONTH_LAST_DIGIT)
                .compareTo("12") > 0) {
            return new CommandOutput(this, message).convertToJSON();
        }

        if (date.substring(Constants.DAY_FIRST_DIGIT, Constants.DAY_LAST_DIGIT)
                .compareTo("31") > 0
                || (date.substring(Constants.DAY_FIRST_DIGIT, Constants.DAY_LAST_DIGIT)
                .compareTo("28") > 0
                && date.substring(Constants.MONTH_FIRST_DIGIT, Constants.MONTH_LAST_DIGIT)
                .equals("02"))) {
            return new CommandOutput(this, message).convertToJSON();
        }

        Event event = new Event(Library.getInstance()
                .getUserByName(username), name, date, description);
        Library.getInstance().getUserByName(username).getEvents().add(event);
        Library.getInstance().getEvents().add(event);

        Library.getInstance().getUserByName(username).getPublisher()
                .notify(new NotificationTemplate("New Event",
                        "New Event from " + username + "."));

        return new CommandOutput(this, username
                + " has added new event successfully.")
                .convertToJSON();
    }
}
