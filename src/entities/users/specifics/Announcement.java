package entities.users.specifics;

import entities.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Announcement {
    private User user;
    private String name;
    private String description;

    public Announcement(final User user, final String name, final String description) {
        this.user = user;
        this.name = name;
        this.description = description;
    }
}
