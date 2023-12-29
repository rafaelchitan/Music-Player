package fileio.output;

import commands.Command;

public class LoadOutput extends CommandOutput {
    public LoadOutput(final Command command, final String message) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.message = message;
    }
}
