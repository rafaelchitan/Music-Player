package commands.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.monetization.CancelPremiumCommand;
import entities.Library;
import entities.files.AudioFile;
import entities.files.Podcast;
import entities.files.Song;
import entities.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class EndProgramCommand extends Command {
    public EndProgramCommand() {
    }

    public ObjectNode execute() {
        for (User user : Library.getInstance().getUsers()) {
            if (user.isPremium()) {
                new CancelPremiumCommand(user.getName(), timestamp).execute();
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
                    - Library.getInstance().getUserByName(s1).getMerchMoney() > 0)
                return 1;
            else if (Library.getInstance().getUserByName(s2).getSongMoney()
                    + Library.getInstance().getUserByName(s2).getMerchMoney()
                    - Library.getInstance().getUserByName(s1).getSongMoney()
                    - Library.getInstance().getUserByName(s1).getMerchMoney() < 0)
                return -1;
            else
                return s1.compareTo(s2);
        });

        for (String artist: sorted) {
            User user = Library.getInstance().getUserByName(artist);

            ObjectNode artistNode = new ObjectMapper().createObjectNode();
            artistNode.put("merchRevenue", Math.round(user.getMerchMoney() * 100.0) / 100.0);
            artistNode.put("songRevenue", Math.round(user.getSongMoney() * 100.0) / 100.0);
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

//        for (Artist artist : Library.getInstance().getUsers().stream()
//                .filter(u -> u.getType().equals("artist"))
//                .toArray(Artist[]::new)) {
//            if (artist.getListenByUser(null) != 0) {
//                ObjectNode artistNode = new ObjectMapper().createObjectNode();
//                artistNode.put("songRevenue", Math.round(0 * 100.0) / 100.0);
//                artistNode.put("merchRevenue", Math.round(0 * 100.0) / 100.0);
//                artistNode.put("ranking", ++rank);
//                artistNode.put("mostProfitableSong", "N/A");
//
//                objectNode.set(artist.getName(), artistNode);
//            }
//        }

        ObjectNode output = new ObjectMapper().createObjectNode();
        output.put("command", "endProgram");
        output.set("result", objectNode);
        return output;
    }
}
