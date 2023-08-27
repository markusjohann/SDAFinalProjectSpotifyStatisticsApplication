package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountServiceTest {

    private UserAccountService userAccountService;
    private UserAccountRepository userAccountRepository;
    private OAuth2AuthorizedClient authorizedClient;

    @BeforeEach
    void setUp() {
        userAccountRepository = mock(UserAccountRepository.class);
        authorizedClient = mock(OAuth2AuthorizedClient.class);
        userAccountService = new UserAccountService(userAccountRepository);
    }

    @Test
    void setAuthorizedClient() {
        userAccountService.setAuthorizedClient(authorizedClient);
        assertNotNull(userAccountService); // Ensure the service is not null
    }

    @Test
    void getLoggedInUserAccount_WithAuthorizedClient() {
        // Set the authorizedClient for this test
        userAccountService.setAuthorizedClient(authorizedClient);

        when(authorizedClient.getPrincipalName()).thenReturn("testPrincipal");
        UserAccount expectedAccount = new UserAccount();
        when(userAccountRepository.findBySpotifyPrincipalName(eq("testPrincipal"))).thenReturn(Optional.of(expectedAccount));

        UserAccount result = userAccountService.getLoggedInUserAccount();

        assertEquals(expectedAccount, result);
    }

    @Test
    void getLoggedInUserAccount_NoAuthorizedClient() {
        assertThrows(RuntimeException.class, () -> userAccountService.getLoggedInUserAccount());
    }

    // Implement searchUsersByDisplayName and other tests here...

}
