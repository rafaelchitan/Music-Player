package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.specifics.Merch;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;
import notifications.NotificationTemplate;

public class AddMerchCommand extends Command {
    private String name;
    private int price;
    private String description;

    public AddMerchCommand(final CommandInput commandInput) {
        this.name = commandInput.getName();
        this.username = commandInput.getUsername();
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.description = commandInput.getDescription();
        this.price = commandInput.getPrice();
    }

    /**
     * Adds a new merchandise to the user's list of merchandise
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {

        if (!Library.getInstance().getUserByName(username).getType().equals("artist")) {
            return new CommandOutput(this, username
                    + " is not an artist.").convertToJSON();
        }

        if (Library.getInstance().getUserByName(username).getMerchByName(name) != null) {
            return new CommandOutput(this, username
                    + " has merchandise with the same name.").convertToJSON();
        }

        if (price < 0) {
            return new CommandOutput(this,
                    "Price for merchandise can not be negative.").convertToJSON();
        }

        Library.getInstance().getUserByName(username).getMerch()
                .add(new Merch(name, price, description));

        Library.getInstance().getUserByName(username).getPublisher()
                .notify(new NotificationTemplate("New Merchandise",
                        "New Merchandise from " + username + "."));

        return new CommandOutput(this, username
                + " has added new merchandise successfully.").convertToJSON();
    }
}
