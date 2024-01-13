package commands.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.monetization.CancelPremiumCommand;
import entities.Library;
import entities.files.Song;
import entities.users.User;
import utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class EndProgramCommand extends Command {
    public EndProgramCommand() {
    }

    /**
     * Executes the EndProgram Command and returns the result.
     * @return JSON ObjectNode containing the result
     */
    public ObjectNode execute() {
        for (User user : Library.getInstance().getUsers()) {
            if (user.isPremium()) {
                new CancelPremiumCommand(user.getName()).execute();
            }
        }

        int rank = 0;

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        HashMap<String, Integer> listenedArtists = new HashMap<>();
        for (Song song: Library.getInstance().getSongs()) {
            if (song.getListenByUser(null) != 0) {
                listenedArtists.put(song.getArtist(),
                        listenedArtists.getOrDefault(song.getArtist(), 0)
                        + song.getListenByUser(null));
            }
        }

        for (User user: Library.getInstance().getUsers()) {
            if (user.getMerchMoney() != 0) {
                listenedArtists.put(user.getName(),
                        listenedArtists.getOrDefault(user.getName(), 0));
            }
        }

        ArrayList<String> sorted = new ArrayList<>(listenedArtists.keySet());
        sorted.sort((s1, s2) -> {
            if (Library.getInstance().getUserByName(s2).getSongMoney()
                    + Library.getInstance().getUserByName(s2).getMerchMoney()
                    - Library.getInstance().getUserByName(s1).getSongMoney()
                    - Library.getInstance().getUserByName(s1).getMerchMoney() > 0) {
                return 1;
            } else if (Library.getInstance().getUserByName(s2).getSongMoney()
                    + Library.getInstance().getUserByName(s2).getMerchMoney()
                    - Library.getInstance().getUserByName(s1).getSongMoney()
                    - Library.getInstance().getUserByName(s1).getMerchMoney() < 0) {
                return -1;
            } else {
                return s1.compareTo(s2);
            }
        });

        for (String artist: sorted) {
            User user = Library.getInstance().getUserByName(artist);

            ObjectNode artistNode = new ObjectMapper().createObjectNode();
            artistNode.put("merchRevenue", user.getMerchMoney());
            artistNode.put("songRevenue",
                    Math.round(user.getSongMoney() * Constants.PRINT_FORMAT)
                            / Constants.PRINT_FORMAT);
            artistNode.put("ranking", ++rank);

            ArrayList<Song> songs = new ArrayList<>(Library.getInstance().getSongs().stream()
                    .filter(s -> s.getArtist().equals(artist)).toList());
            HashMap<String, Double> profitSongs = new HashMap<>();

            for (Song song: songs) {
                profitSongs.put(song.getName(), profitSongs.getOrDefault(song.getName(), 0.0)
                        + song.getMoney());
            }

            String mostProfitableSong = "N/A";
            double mostProfit = 0.0;
            for (String song: profitSongs.keySet()) {
                if (profitSongs.get(song) > mostProfit) {
                    mostProfit = profitSongs.get(song);
                    mostProfitableSong = song;
                } else if (profitSongs.get(song) == mostProfit && mostProfit != 0.0) {
                    if (song.compareTo(mostProfitableSong) < 0) {
                        mostProfitableSong = song;
                    }
                }
            }

            artistNode.put("mostProfitableSong", mostProfitableSong);
            objectNode.set(artist, artistNode);
        }

        ObjectNode output = new ObjectMapper().createObjectNode();
        output.put("command", "endProgram");
        output.set("result", objectNode);
        return output;
    }
}
