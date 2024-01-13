package commands.monetization;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import entities.Library;
import entities.files.Song;
import entities.users.User;
import fileio.input.CommandInput;
import fileio.output.CommandOutput;
import org.antlr.v4.runtime.misc.Pair;
import utils.ListenedEntry;

import java.util.HashMap;
import java.util.Map;

public class CancelPremiumCommand extends Command {
    public CancelPremiumCommand(CommandInput commandInput) {
        this.command = commandInput.getCommand();
        this.username = commandInput.getUsername();
        this.timestamp = commandInput.getTimestamp();
    }

    public CancelPremiumCommand(String username, int timestamp) {
        this.username = username;
    }

    public ObjectNode execute() {
        User user = Library.getInstance().getUserByName(username);
        if (user == null) {
            return new CommandOutput(this, "The username " + username + " doesn't exist").convertToJSON();
        }

        if (!user.isPremium()) {
            return new CommandOutput(this, username + " is not a premium user.").convertToJSON();
        }

        user.getPlayer().update(user, timestamp);
        user.setPremium(false);

        HashMap<String, Integer> listenedArtists = new HashMap<>();
        HashMap<Song, Integer> listenedSongs = new HashMap<>();
        int totalListens = 0;
        for (Song song : Library.getInstance().getSongs()) {
            int listens = 0;
            for (Pair<User, Integer> pair : song.getPremiumTimesListened()) {
                if (pair.a.equals(user) && !Library.getInstance().getListenedEntries().contains(new ListenedEntry(song, user, pair.b))) {
                    Library.getInstance().getListenedEntries().add(new ListenedEntry(song, user, pair.b));
                    listens++;
                }
            }

            if (listens != 0) {
                listenedArtists.put(song.getArtist(),
                        listenedArtists.getOrDefault(song.getArtist(), 0) + listens);
                listenedSongs.put(song, listens);
                totalListens += listens;
            }
        }

        for (Map.Entry<Song, Integer> entry : listenedSongs.entrySet()) {
            Song song = entry.getKey();
            int listens = entry.getValue();
            song.setMoney(song.getMoney() + (double) 1000000 * listens / totalListens);
        }

        for (Map.Entry<String, Integer> entry : listenedArtists.entrySet()) {
            User artist = Library.getInstance().getUserByName(entry.getKey());
            artist.setSongMoney(artist.getSongMoney() + (double) 1000000 * entry.getValue() / totalListens);
        }

        for (Song song : Library.getInstance().getSongs()) {
            song.getPremiumTimesListened().removeIf(pair -> pair.a.equals(user));
        }

        return new CommandOutput(this, username + " cancelled the subscription successfully.").convertToJSON();

    }
}
