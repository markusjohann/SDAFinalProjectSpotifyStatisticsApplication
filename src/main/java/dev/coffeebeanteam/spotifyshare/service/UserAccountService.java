package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.ui.UserAccountDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccountService {
    private OAuth2AuthorizedClient authorizedClient;
    private final UserAccountRepository userAccountRepository;


    public UserAccountService(
            UserAccountRepository userAccountRepository
    ) {
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

    public List<UserAccountDto> searchUsersByDisplayName(final String search)
    {
        final UserAccount loggedInUserAccount = getLoggedInUserAccount();

        final Pageable maxTen = PageRequest.of(0, 10);

        final Page<UserAccount> foundAccountsPage =
                userAccountRepository.findBySpotifyUsernameContainingIgnoreCase(search, maxTen);

        final List<UserAccount> foundAccounts = foundAccountsPage.getContent();

        return foundAccounts.stream()
                .filter(account -> account.getId() != loggedInUserAccount.getId())
                .map(account -> {
                    UserAccountDto dto = new UserAccountDto();
                    dto.setUserId(account.getId());
                    dto.setDisplayName(account.getSpotifyUsername());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
