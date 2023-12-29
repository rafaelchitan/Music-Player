package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

import static java.lang.Integer.MAX_VALUE;

public class RepeatCommand extends Command {
    public RepeatCommand(final CommandInput commandInput) {
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.command = commandInput.getCommand();

    }

    /**
     * Executes the repeat command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        user.getPlayer().update(user, timestamp);
        if (user.getPlayer().getCurrentFile() == null) {
            return new CommandOutput(this,
                    "Please load a source before setting the repeat status.").convertToJSON();
        }

        if (user.getPlayer().getCurrentFile().objType().equals("playlist")
                || user.getPlayer().getCurrentFile().objType().equals("album")) {
            if (user.getPlayer().getStats().getRepeat().equals("No Repeat")) {
                user.getPlayer().getStats().setRepeat("Repeat All");
                user.getPlayer().setRepeatThis(0);
                user.getPlayer().setRepeatAllCount(MAX_VALUE);
            } else if (user.getPlayer().getStats().getRepeat().equals("Repeat All")) {
                user.getPlayer().getStats().setRepeat("Repeat Current Song");
                user.getPlayer().setRepeatThis(MAX_VALUE);
                user.getPlayer().setRepeatAllCount(0);
            } else {
                user.getPlayer().getStats().setRepeat("No Repeat");
                user.getPlayer().setRepeatThis(0);
                user.getPlayer().setRepeatAllCount(0);
            }
        } else {
            if (user.getPlayer().getStats().getRepeat().equals("No Repeat")) {
                user.getPlayer().getStats().setRepeat("Repeat Once");
                user.getPlayer().setRepeatThis(1);
                user.getPlayer().setRepeatAllCount(0);
            } else if (user.getPlayer().getStats().getRepeat().equals("Repeat Once")) {
                user.getPlayer().getStats().setRepeat("Repeat Infinite");
                user.getPlayer().setRepeatThis(0);
                user.getPlayer().setRepeatAllCount(MAX_VALUE);
            } else {
                user.getPlayer().getStats().setRepeat("No Repeat");
                user.getPlayer().setRepeatThis(0);
                user.getPlayer().setRepeatAllCount(0);
            }
        }

        return new CommandOutput(this, "Repeat mode changed to "
                + user.getPlayer().getStats().getRepeat().toLowerCase() + ".").convertToJSON();
    }
}
