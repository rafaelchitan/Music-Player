package commands.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import constants.Constants;
import entities.Library;
import entities.player.Player;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class ForwardBackwardCommand extends Command {
    public ForwardBackwardCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();

        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
    }

    /**
     * Executes the forward/backward command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        Player player = Library.getInstance().getUserByName(username).getPlayer();
        player.update(Library.getInstance().getUserByName(username), timestamp);
        if (player.getCurrentFile() == null) {
            if (command.equals("forward")) {
                return new CommandOutput(this,
                        "Please load a source before attempting to forward.").convertToJSON();
            }
            return new CommandOutput(this,
                    "Please select a source before rewinding.").convertToJSON();
        }

        if (!player.getCurrentFile().objType().equals("podcast")) {
            return new CommandOutput(this,
                    "The loaded source is not a podcast.").convertToJSON();
        }

        if (command.equals("forward")) {
            return forward();
        }
        return backward();
    }

    /**
     * Rewinds the current podcast by 90 seconds.
     * @return the JSON representation of the command output
     */
    public ObjectNode backward() {
        Player player = Library.getInstance().getUserByName(username).getPlayer();
        if (player.getCurrentElapsedTime() < Constants.BACKWARD_DURATION) {
            player.setCurrentTimestamp(player.getCurrentTimestamp()
                    - player.getCurrentElapsedTime());
        } else {
            player.setCurrentTimestamp(player.getCurrentTimestamp() - Constants.BACKWARD_DURATION);
        }
        return new CommandOutput(this, "Rewound successfully.").convertToJSON();
    }

    /**
     * Skips forward the current podcast by 90 seconds.
     * @return the JSON representation of the command output
     */

    public ObjectNode forward() {
        Player player = Library.getInstance().getUserByName(username).getPlayer();
        if (player.getActiveFile().getDuration() - player.getCurrentElapsedTime()
                < Constants.FORWARD_DURATION) {
            player.setCurrentTimestamp(player.getCurrentTimestamp()
                    + player.getActiveFile().getDuration()
                    - player.getCurrentElapsedTime());
        } else {
            player.setCurrentTimestamp(player.getCurrentTimestamp() + Constants.FORWARD_DURATION);
        }
        return new CommandOutput(this, "Skipped forward successfully.").convertToJSON();
    }
}
