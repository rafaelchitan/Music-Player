package fileio.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter @Setter
public class CommandInput {
    private String command;
    private String username;
    private int timestamp;
    private String type;
    private String playlistName;
    private FilterInput filters;
    private int itemNumber;
    private int seed;
    private int playlistId;
    private String nextPage;
    private int age;
    private String city;
    private String description;
    private ArrayList<SongInput> songs;
    private String name;
    private String date;
    private int price;
    private ArrayList<EpisodeInput> episodes;
    private int releaseYear;
    private String recommendationType;
}
