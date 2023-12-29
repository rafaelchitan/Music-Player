package commands.searchbar;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Entity;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.SelectOutput;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Select extends Command {
    private int itemNumber;

    public Select(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.username = commandInput.getUsername();
        this.itemNumber = commandInput.getItemNumber();

    }

    /**
     * Executes the select command.
     * @return the JSON representation of the command output
     */
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        List<Entity> searchbarResults = user.getSearchbarResults();

        if (searchbarResults == null) {
            user.setSelectedFile(null);
            String message = "Please conduct a search before making a selection.";
            return new SelectOutput(this, message).convertToJSON();
        }
        if (itemNumber > searchbarResults.size()) {
            user.setSelectedFile(null);
            String message = "The selected ID is too high.";
            return new SelectOutput(this, message).convertToJSON();
        }

        user.setSelectedFile(searchbarResults.get(itemNumber - 1));
        if (user.getSelectedFile().objType().equals("user")) {
            user.setCurrentPage(((User) user.getSelectedFile()).getPublicPage());

            String message = "Successfully selected "
                    + searchbarResults.get(itemNumber - 1).getName()
                    + "'s page.";
            return new SelectOutput(this, message).convertToJSON();
        }

        String message = "Successfully selected "
                + searchbarResults.get(itemNumber - 1).getName()
                + ".";
        return new SelectOutput(this, message).convertToJSON();
    }
}
