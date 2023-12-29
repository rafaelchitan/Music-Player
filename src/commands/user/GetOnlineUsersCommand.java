package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.UsersOutput;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GetOnlineUsersCommand extends Command {
    private final ArrayList<User> onlineUsers;
    public GetOnlineUsersCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        onlineUsers = Library.getInstance().getUsers().stream()
                .filter(user -> user.getType().equals("user"))
                .filter(User::isOnline)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets the list of online users
     * @return the list of online users
     */
    @Override
    public ObjectNode execute() {
        return new UsersOutput(this, onlineUsers).convertToJSON();
    }
}
