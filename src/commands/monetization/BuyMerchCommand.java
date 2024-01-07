package commands.monetization;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import entities.users.specifics.Merch;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;

public class BuyMerchCommand extends Command {
    private String name;

    public BuyMerchCommand(CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
        this.name = commandInput.getName();
    }

    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        User vendor = user.getCurrentPage().getUser();

        if (vendor == null || vendor.getType().equals("user")) {
            return new CommandOutput(this, "Cannot buy merch from this page.").convertToJSON();
        }

        Merch merch = vendor.getMerchByName(name);
        if (merch == null) {
            return new CommandOutput(this, "The merch " + name + " doesn't exist.").convertToJSON();
        }

        vendor.setMerchMoney(vendor.getMerchMoney() + merch.getPrice());
        user.getBoughtMerch().add(merch);
        return new CommandOutput(this,
                username + " has added new merch successfully.").convertToJSON();
    }
}
