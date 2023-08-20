package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.ui.content.TopItemsGalleryDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserTopItem;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import dev.coffeebeanteam.spotifyshare.service.UserTopItemsService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service @Getter @Setter @Accessors(chain = true)
public class TopItemsGalleryService {
    final UserTopItemsService topItemsService;
    final UserAccountService userAccountService;

    private OAuth2AuthorizedClient authorizedClient;

    public TopItemsGalleryService(
            UserTopItemsService topItemsService,
            UserAccountService userAccountService
            ) {
        this.topItemsService = topItemsService;
        this.userAccountService = userAccountService;
    }

    public TopItemsGalleryDto getLoggedInUserTopItemsGalleryDto() {
        final UserAccount userAccount = this.userAccountService
                .setAuthorizedClient(this.getAuthorizedClient())
                .getLoggedInUserAccount();

        return getUserTopItemsGalleryDto(userAccount);
    }

    public TopItemsGalleryDto getUserTopItemsGalleryDto(final UserAccount userAccount)
    {
        final List<UserTopItem> topItems = this.topItemsService.getTopItemsByUser(userAccount);

        final TopItemsGalleryDto topItemsGallery = new TopItemsGalleryDto();

        final Stream<UserTopItem> artists = topItems.stream().filter(artist -> artist.getType().equals("artist"));

        final Stream<UserTopItem> tracks = topItems.stream().filter(artist -> artist.getType().equals("track"));

        final Map<String, TopItemsGalleryDto.Artist> spotifyIdToArtist = new HashMap<>();

        artists.forEach(
                artist -> {
                    final TopItemsGalleryDto.Artist topItemsArtist =
                            new TopItemsGalleryDto.Artist()
                                    .setSpotifyId(artist.getSpotifyId())
                                    .setName(artist.getName())
                                    .setImages(new ArrayList<>(artist.getImages()));

                    topItemsGallery.getArtists().add(topItemsArtist);

                    spotifyIdToArtist.put(topItemsArtist.getSpotifyId(), topItemsArtist);
                }
        );

        tracks.forEach(
                track -> {
                    track.getArtistSpotifyIds().stream().forEach(
                            spotifyArtistId -> {
                                final TopItemsGalleryDto.Artist trackArtist = spotifyIdToArtist.get(spotifyArtistId);

                                if (trackArtist != null) {
                                    trackArtist.getTracks().add(
                                            new TopItemsGalleryDto.Track()
                                                    .setName(track.getName())
                                                    .setSpotifyId(track.getSpotifyId())
                                    );
                                }
                            }
                    );
                }
        );

        topItemsGallery.removeArtistsWithoutTracks();

        return topItemsGallery;
    }

    public TopItemsGalleryService populateModelViewWithTopItems(Model model) {
        model.addAttribute("topItems", getLoggedInUserTopItemsGalleryDto());

        return this;
    }

    public TopItemsGalleryService populateModelViewWithTopItems(Model model, UserAccount userAccount)
    {
        model.addAttribute("topItems", getUserTopItemsGalleryDto(userAccount));

        return this;
    }
}
