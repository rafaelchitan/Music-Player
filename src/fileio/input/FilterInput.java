package fileio.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class FilterInput {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String owner;
    private String description;
}
