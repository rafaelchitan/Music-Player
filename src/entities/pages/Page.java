package entities.pages;

import entities.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Page {
    protected User user;

    public abstract String toString();

    public abstract void update();
}
