package entities.player;

import entities.Entity;
import entities.Library;
import entities.files.Album;
import entities.files.AudioFile;
import entities.files.Episode;
import entities.files.Podcast;
import entities.files.Song;
import entities.files.Playlist;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Setter @Getter
public class Player {
    private String playStatus; // playing, paused
    private int startTimestamp;
    private int initialTimestamp;

    private Entity currentFile;
    private PlayerStats stats = new PlayerStats();
    private ArrayList<Integer> queueIndexes = new ArrayList<>();

    private AudioFile activeFile;
    private ArrayList<AudioFile> queue = new ArrayList<>();
    private int queueIndex = 0;

    private int currentTimestamp;
    private int repeatThis = 0;
    private int repeatAllCount = 0;

    public Player() {
    }

    public Player(final int startTimestamp, final Entity currentFile,
                  final int currentTimestamp) {
        this.playStatus = "playing";
        queueIndexes = new ArrayList<>();
        stats = new PlayerStats();
        this.startTimestamp = startTimestamp;
        this.currentTimestamp = currentTimestamp;
        this.initialTimestamp = startTimestamp;
        this.currentFile = currentFile;
        int qMaxIndex = 0;
        switch (currentFile.objType()) {
            case "song":
                queueIndexes.add(qMaxIndex++);
                queue.add((Song) currentFile);
                break;
            case "podcast":
                for (Episode episode : ((Podcast) currentFile).getEpisodes()) {
                    queue.add(episode);
                    queueIndexes.add(qMaxIndex++);
                }
                break;
            case "playlist":
                for (Song song : ((Playlist) currentFile).getSongs()) {
                    queue.add(song);
                    queueIndexes.add(qMaxIndex++);
                }
                break;
            case "album":
                for (Song song : ((Album) currentFile).getSongs()) {
                    queue.add(song);
                    queueIndexes.add(qMaxIndex++);
                }
            default:
                break;
        }
    }

    /**
     * Updates the given user's player to the current state.
     * @param user the user who gives the command
     * @param timestamp the timestamp of the command
     */
    public void update(final User user, final int timestamp) {
        if (currentFile == null) {
            return;
        }

        if (!playStatus.equals("playing") || stats.isOffline()) {
            startTimestamp = timestamp;
            return;
        }

        currentTimestamp += timestamp - startTimestamp;
        startTimestamp = timestamp;
        int currentDuration = 0;
        int addedDuration = 0;
        int added = 0;
        repeatAllCount++;
        do {
            repeatAllCount--;
            for (int i = 0; i < queue.size(); i++) {
                AudioFile audioFile = queue.get(queueIndexes.get(i));
                audioFile.addListened(user, initialTimestamp + currentDuration);
                if (user.isPremium() && user.getPremiumStart() <= initialTimestamp + currentDuration) {
                    audioFile.premiumAddListened(user, initialTimestamp + currentDuration);
                }

                currentDuration += audioFile.getDuration(user);
                addedDuration += audioFile.getDuration(user);

                if (currentDuration > currentTimestamp) {
                    activeFile = audioFile;
                    stats.setRemainedTime(currentDuration - currentTimestamp);
                    stats.setName(audioFile.getName());
                    queueIndex = i;
                    if (currentDuration - currentTimestamp <= 10) {
                        activeFile.pass(user);
                    }
                    break;
                } else {
                    audioFile.pass(user);
                    if (repeatThis > 0 && i == queueIndex) {
                        startTimestamp += audioFile.getDuration(user);
                        added += audioFile.getDuration(user);
                        i--;
                        repeatThis--;
                        if (repeatThis == 0) {
                            stats.setRepeat("No Repeat");
                        }
                    }
                }
            }

            if (currentDuration <= currentTimestamp && repeatAllCount > 0) {
                queueIndex = 0;
                startTimestamp += addedDuration;
                addedDuration = 0;
            }
        } while (currentDuration <= currentTimestamp && repeatAllCount > 0);

        if (currentDuration <= currentTimestamp) {
            if (currentFile.objType().equals("podcast")) {
                user.getPodcastHistory().remove((Podcast) currentFile);
            }
            reset();
            return;
        }


        if (currentFile.objType().equals("podcast")) {
            user.getPodcastHistory().put((Podcast) currentFile, currentTimestamp);
        }

        startTimestamp -= added;
        currentTimestamp -= added;
    }

    /**
     * Resets the player to its initial state - unloads the current file.
     */
    public void reset() {
        playStatus = "paused";
        startTimestamp = 0;
        currentFile = null;
        currentTimestamp = 0;
        activeFile = null;
        queue.clear();
        stats.reset();
        repeatThis = 0;
        repeatAllCount = 0;
        queueIndex = 0;
    }

    /**
     * Calculates the total time from the initial state of the current player.
     * @return the total "playing" time from the last load
     */
    public int getCurrentElapsedTime(User user) {
        if (currentFile == null) {
            return 0;
        }
        int current = 0;
        for (int i = 0; i < queueIndex; i++) {
            current += queue.get(queueIndexes.get(i)).getDuration(user);
        }
        return currentTimestamp - current;
    }

    public void setAdBreak(User user, int price, int timestamp) {
        if (activeFile.objType().equals("song")) {
            ((Song) activeFile).getAdPrice().put(user, price);
            ((Song) activeFile).getAdTimestamp().put(user, timestamp);
            ((Song) activeFile).getHasAdBreak().add(user);
        }
    }
}
