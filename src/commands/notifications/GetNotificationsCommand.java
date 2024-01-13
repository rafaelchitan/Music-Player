package commands.notifications;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.NotificationsOutput;
import notifications.NotificationTemplate;
import java.util.ArrayList;

public class GetNotificationsCommand extends Command {
    public GetNotificationsCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the GetNotifications Command and returns the result.
     * @return JSON ObjectNode containing the result
     */
    @Override
    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        ArrayList<NotificationTemplate> notifications = new ArrayList<>(user.getNotifications());
        user.getNotifications().clear();
        return new NotificationsOutput(this, notifications).convertToJSON();
    }
}
