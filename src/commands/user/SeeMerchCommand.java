package commands.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.specifics.Merch;
import fileio.input.CommandInput;
import fileio.output.MerchOutput;

public class SeeMerchCommand extends Command {
    public SeeMerchCommand(CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    @Override
    public ObjectNode execute() {
        return new MerchOutput(this, Library.getInstance().getUserByName(username).getBoughtMerch()).convertToJSON();
    }
}
