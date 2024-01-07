package commands.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.monetization.CancelPremiumCommand;
import entities.Library;
import entities.files.Podcast;
import entities.files.Song;
import entities.users.User;

import java.util.ArrayList;
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
                    .filter(s -> s.getArtist().equals(artist))
                    .sorted((s1, s2) -> {
                        if (s2.getMoney() - s1.getMoney() > 0)
                            return 1;
                        else if (s2.getMoney() - s1.getMoney() < 0)
                            return -1;
                        else
                            return 0;
                    })
                    .toList());
            if (songs.size() != 0 && songs.get(0).getMoney() != 0)
                artistNode.put("mostProfitableSong", songs.get(0).getName());
            else
                artistNode.put("mostProfitableSong", "N/A");

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
