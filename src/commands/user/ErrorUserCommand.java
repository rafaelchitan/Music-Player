package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import fileio.output.ErrorOutput;

public class ErrorUserCommand extends Command {
    private String type;
    private int age;
    private String city;
    public ErrorUserCommand(final CommandInput commandInput) {
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
        return new ErrorOutput(this,
                "The username " + username + " doesn't exist.").convertToJSON();
    }
}
