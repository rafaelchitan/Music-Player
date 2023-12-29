package commands.audiofile.album;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.files.Album;
import entities.Library;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import fileio.output.CommandOutput;

import java.util.ArrayList;

public class AddAlbumCommand extends Command {
    private String name;
    private String artist;
    private int releaseYear;
    private String description;
    private ArrayList<SongInput> songInputs;

    public AddAlbumCommand(final CommandInput commandInput) {
        this.name = commandInput.getName();
        this.artist = commandInput.getUsername();
        this.username = commandInput.getUsername();
        this.command = commandInput.getCommand();
        this.timestamp = commandInput.getTimestamp();
        this.releaseYear = commandInput.getReleaseYear();
        this.description = commandInput.getDescription();
        this.songInputs = commandInput.getSongs();
    }

    /**
     * Adds a new album to the user's list of albums
     * @return the JSON representation of the command result
     */
    @Override
    public ObjectNode execute() {

        if (!Library.getInstance().getUserByName(username).getType().equals("artist")) {
            return new CommandOutput(this, username
                    + " is not an artist.").convertToJSON();
        }

        if (Library.getInstance().getUserByName(username).getAlbumByName(name) != null) {
            return new CommandOutput(this, username
                    + " has another album with the same name.").convertToJSON();
        }

        for (SongInput songInput : songInputs) {
            if (songInputs.stream().filter(song -> song.getName().equals(songInput.getName()))
                    .count() != 1) {
                return new CommandOutput(this, username
                        + " has the same song at least twice in this album.").convertToJSON();
            }
        }

        Album album = new Album(name, artist, releaseYear, description, songInputs);
        Library.getInstance().getAlbums().add(album);
        Library.getInstance().getUserByName(username).getAlbums().add(album);
        return new CommandOutput(this, username
                + " has added new album successfully.").convertToJSON();
    }
}
