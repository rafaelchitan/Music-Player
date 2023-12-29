package entities;

public interface Entity {
    /**
     * @return the name of the object
     */
    String getName();

    /**
     * @return the type of the object
     */
    String objType();
    /**
     * @return the duration of the specific filetype
     */
    int getDuration();
}
