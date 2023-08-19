package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service @Setter @Getter @Accessors(chain = true)
public class UserAccountDetailsService {
    final private SpotifyApiService spotifyApiService;
    final private UserAccountService userAccountService;
    private OAuth2AuthorizedClient authorizedClient;

    public UserAccountDetailsService(
            SpotifyApiService spotifyApiService,
            UserAccountService userAccountService
    ) {
        this.spotifyApiService = spotifyApiService;
        this.userAccountService = userAccountService;
    }

    public UserAccountDetailsService populateViewModelWithUserDetails(Model model)
    {
        final UserDto spotifyUser = spotifyApiService.setAuthorizedClient(authorizedClient).getUser();
        final UserAccount userAccount = userAccountService.setAuthorizedClient(authorizedClient)
                .getLoggedInUserAccount();

        model.addAttribute("userAccountName", spotifyUser.getDisplayName());
        model.addAttribute("userAccountImage",
                spotifyUser.getImages().length == 0 ?
                        "" :
                        spotifyUser.getImages()[0].getUrl()
        );

        model.addAttribute("userIsNotApproved", !userAccount.isApproved());

        return this;
    }
}
