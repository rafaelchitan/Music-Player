package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import fileio.input.CommandInput;
import fileio.output.MerchOutput;

public class SeeMerchCommand extends Command {
    public SeeMerchCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the SeeMerch Command and returns the result.
     * @return JSON ObjectNode containing the result
     */
    @Override
    public ObjectNode execute() {
        return new MerchOutput(this,
                Library.getInstance().getUserByName(username).getBoughtMerch()).convertToJSON();
    }
}
