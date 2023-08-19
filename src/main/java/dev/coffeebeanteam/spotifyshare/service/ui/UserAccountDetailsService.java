package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service @Setter @Getter @Accessors(chain = true)
public class UserAccountDetailsService {
    final private SpotifyApiService spotifyApiService;
    private OAuth2AuthorizedClient authorizedClient;

    public UserAccountDetailsService(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    public UserAccountDetailsService populateViewModelWithUserDetails(Model model)
    {
        final UserDto spotifyUser = spotifyApiService.setAuthorizedClient(authorizedClient).getUser();

        model.addAttribute("userAccountName", spotifyUser.getDisplayName());
        model.addAttribute("userAccountImage",
                spotifyUser.getImages().length == 0 ?
                        "" :
                        spotifyUser.getImages()[0].getUrl()
        );

        return this;
    }
}
