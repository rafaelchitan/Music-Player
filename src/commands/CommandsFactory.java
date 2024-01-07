package commands;

import commands.audiofile.album.AddAlbumCommand;
import commands.audiofile.album.RemoveAlbumCommand;
import commands.audiofile.album.ShowAlbumsCommand;
import commands.audiofile.playlist.CreatePlaylistCommand;
import commands.audiofile.playlist.ShowPlaylistsCommand;
import commands.audiofile.playlist.SwitchVisibilityCommand;
import commands.audiofile.playlist.AddRemoveInPlaylistCommand;
import commands.audiofile.playlist.FollowCommand;
import commands.audiofile.podcast.AddPodcastCommand;
import commands.audiofile.podcast.RemovePodcastCommand;
import commands.audiofile.podcast.ShowPodcastsCommand;
import commands.monetization.AdBreakCommand;
import commands.monetization.BuyPremiumCommand;
import commands.monetization.CancelPremiumCommand;
import commands.notifications.GetNotificationsCommand;
import commands.notifications.SubscribeCommand;
import commands.pages.ChangePageCommand;
import commands.pages.PrintCurrentPageCommand;
import commands.player.LikeCommand;
import commands.player.NextPrevCommand;
import commands.player.PlayPauseCommand;
import commands.player.RepeatCommand;
import commands.player.ShuffleCommand;
import commands.player.StatusCommand;
import commands.player.ForwardBackwardCommand;
import commands.player.LoadCommand;
import commands.searchbar.Search;
import commands.searchbar.Select;
import commands.statistics.*;
import commands.statistics.listen.WrappedCommand;
import commands.user.AddUserCommand;
import commands.user.DeleteUserCommand;
import commands.user.ErrorUserCommand;
import commands.user.GetAllUsersCommand;
import commands.user.GetOnlineUsersCommand;
import commands.user.SwitchConnectionStatusCommand;
import commands.user.AddAnnouncementCommand;
import commands.user.AddEventCommand;
import commands.user.AddMerchCommand;
import commands.user.RemoveAnnouncementCommand;
import commands.user.RemoveEventCommand;
import entities.Library;
import fileio.input.CommandInput;
import lombok.Getter;

public final class CommandsFactory {

    @Getter
    private static final CommandsFactory instance = new CommandsFactory();

    private CommandsFactory() {
    }

    public Command createCommand(final CommandInput command) {

        if (command.getCommand().equals("addUser")) {
            return new AddUserCommand(command);
        }

        if (command.getUsername() != null && (Library.getInstance()
                .getUserByName(command.getUsername()) == null)) {
            return new ErrorUserCommand(command);
        }

        return switch (command.getCommand()) {
            case "search" -> new Search(command);
            case "select" -> new Select(command);
            case "load" -> new LoadCommand(command);
            case "playPause" -> new PlayPauseCommand(command);
            case "status" -> new StatusCommand(command);
            case "like" -> new LikeCommand(command);
            case "createPlaylist" -> new CreatePlaylistCommand(command);
            case "addRemoveInPlaylist" -> new AddRemoveInPlaylistCommand(command);
            case "showPlaylists" -> new ShowPlaylistsCommand(command);
            case "showPreferredSongs" -> new ShowPrefferedSongsCommand(command);
            case "repeat" -> new RepeatCommand(command);
            case "shuffle" -> new ShuffleCommand(command);
            case "switchVisibility" -> new SwitchVisibilityCommand(command);
            case "follow" -> new FollowCommand(command);
            case "getTop5Songs" -> new GetTopSongsCommand(command);
            case "getTop5Playlists" -> new GetTopPlaylistsCommand(command);
            case "forward", "backward" -> new ForwardBackwardCommand(command);
            case "next", "prev" -> new NextPrevCommand(command);
            case "switchConnectionStatus" -> new SwitchConnectionStatusCommand(command);
            case "getOnlineUsers" -> new GetOnlineUsersCommand(command);
            case "addAlbum" -> new AddAlbumCommand(command);
            case "printCurrentPage" -> new PrintCurrentPageCommand(command);
            case "showAlbums" -> new ShowAlbumsCommand(command);
            case "addEvent" -> new AddEventCommand(command);
            case "addMerch" -> new AddMerchCommand(command);
            case "getAllUsers" -> new GetAllUsersCommand(command);
            case "deleteUser" -> new DeleteUserCommand(command);
            case "addPodcast" -> new AddPodcastCommand(command);
            case "addAnnouncement" -> new AddAnnouncementCommand(command);
            case "removeAnnouncement" -> new RemoveAnnouncementCommand(command);
            case "showPodcasts" -> new ShowPodcastsCommand(command);
            case "removeAlbum" -> new RemoveAlbumCommand(command);
            case "changePage" -> new ChangePageCommand(command);
            case "removePodcast" -> new RemovePodcastCommand(command);
            case "removeEvent" -> new RemoveEventCommand(command);
            case "getTop5Albums" -> new GetTopAlbumsCommand(command);
            case "getTop5Artists" -> new GetTopArtistsCommand(command);
            case "wrapped" -> new WrappedCommand(command);
            case "buyPremium" -> new BuyPremiumCommand(command);
            case "cancelPremium" -> new CancelPremiumCommand(command);
            case "adBreak" -> new AdBreakCommand(command);
            case "subscribe" -> new SubscribeCommand(command);
            case "getNotifications" -> new GetNotificationsCommand(command);
            default -> null;
        };
    }

}
