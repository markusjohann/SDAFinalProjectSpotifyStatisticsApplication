package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
    private OAuth2AuthorizedClient authorizedClient;
    private UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccountService setAuthorizedClient(OAuth2AuthorizedClient authorizedClient) {
        this.authorizedClient = authorizedClient;
        return this;
    }

    public UserAccount getLoggedInUserAccount() {
        if (this.authorizedClient == null) {
            throw new RuntimeException("No authorizedClient set.");
        }

        final String principalName = this.authorizedClient.getPrincipalName();

        return userAccountRepository.findBySpotifyPrincipalName(principalName).orElseThrow();
    }
}
