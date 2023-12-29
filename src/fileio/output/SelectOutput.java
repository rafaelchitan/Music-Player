package fileio.output;

import commands.Command;

public class SelectOutput extends CommandOutput {
    public SelectOutput(final Command command, final String message) {
        this.command = command.getCommand();
        this.user = command.getUsername();
        this.timestamp = command.getTimestamp();
        this.message = message;
    }


}
