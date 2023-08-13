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
import java.util.List;

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

    public TopItemsGalleryDto getTopItemsGalleryDto() {
        final UserAccount userAccount = this.userAccountService
                .setAuthorizedClient(this.getAuthorizedClient())
                .getLoggedInUserAccount();

        final List<UserTopItem> topItems = this.topItemsService.getTopItemsByUser(userAccount);

        final TopItemsGalleryDto topItemsGallery = new TopItemsGalleryDto();

        for (final UserTopItem item: topItems) {
            if (item.getType().equals("artist")) {
                final TopItemsGalleryDto.Artist artist = new TopItemsGalleryDto.Artist()
                        .setName(item.getName())
                        .setImages(new ArrayList<>(item.getImages()));

                topItemsGallery.getArtists().add(artist);
            }
        }

        return topItemsGallery;
    }

    public TopItemsGalleryService populateModelViewWithTopItems(Model model) {
        model.addAttribute("topItems", getTopItemsGalleryDto());

        return this;
    }
}
