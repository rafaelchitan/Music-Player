package fileio.output;

import commands.Command;
import fileio.input.CommandInput;

public class ErrorOutput extends CommandOutput {
    public ErrorOutput(final Command command, final String message) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.message = message;
    }
}
