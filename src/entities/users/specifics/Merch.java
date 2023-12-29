package entities.users.specifics;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Merch {
    private String name;
    private int price;
    private String description;

    public Merch(final String name, final int price, final String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
