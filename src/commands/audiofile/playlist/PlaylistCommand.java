package commands.audiofile.playlist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;

public abstract class PlaylistCommand extends Command {
    protected String playlistName;
    protected int playlistId;

    /**
     * Executes the command - specialized in playlist commands.
     * @return the JSON representation of the command output
     */
    public abstract ObjectNode execute();
}
