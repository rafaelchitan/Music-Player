package fileio.output;

import commands.Command;

public class LikeOutput extends CommandOutput {
    public LikeOutput(final Command command, final String message) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.user = command.getUsername();
        this.message = message;
    }
}
