package commands.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.UsersOutput;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GetAllUsersCommand extends Command {
    private final ArrayList<User> users;
    public GetAllUsersCommand(final CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        users = Library.getInstance().getUsers();
    }

    /**
     * Returns the list of users in order: users, artists, hosts
     * @return the list of users
     */
    @Override
    public ObjectNode execute() {
        ArrayList<User> orderedUsers = users.stream()
                .filter(user -> user.getType().equals("user"))
                .collect(Collectors.toCollection(ArrayList::new));

        orderedUsers.addAll(users.stream()
                .filter(user -> user.getType().equals("artist"))
                .collect(Collectors.toCollection(ArrayList::new)));

        orderedUsers.addAll(users.stream()
                .filter(user -> user.getType().equals("host"))
                .collect(Collectors.toCollection(ArrayList::new)));
        return new UsersOutput(this, orderedUsers).convertToJSON();
    }
}
