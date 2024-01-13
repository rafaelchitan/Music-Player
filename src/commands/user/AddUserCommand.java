package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.users.Artist;
import entities.users.Host;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class AddUserCommand extends Command {
    private String type;
    private int age;
    private String city;
    public AddUserCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();

        this.type = commandInput.getType();
        this.age = commandInput.getAge();
        this.city = commandInput.getCity();
    }

    /**
     * Adds a new user to the library
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {
        if (Library.getInstance().getUserByName(username) != null
                && !Library.getInstance().getUserByName(username).isFake()) {
            return new CommandOutput(this, "The username "
                    + username
                    + " is already taken.")
                    .convertToJSON();
        }
        if (Library.getInstance().getUserByName(username) != null) {
            Library.getInstance().getUsers().remove(Library.getInstance().getUserByName(username));
        }

        if (type.equals("artist")) {
            Library.getInstance().getUsers().add(new Artist(username, age, city, type));
        } else if (type.equals("host")) {
            Library.getInstance().getUsers().add(new Host(username, age, city, type));
        } else {
            Library.getInstance().getUsers().add(new User(username, age, city, type));
        }
        return new CommandOutput(this, "The username "
                + username
                + " has been added successfully.").convertToJSON();
    }
}
