package entities.users.specifics;

import entities.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Event {
    private User user;
    private String name;
    private String date;
    private String description;

    public Event(final User user, final String name, final String date, final String description) {
        this.user = user;
        this.name = name;
        this.date = date;
        this.description = description;
    }
}
