package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.ImageDto;
import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserAccountDetailsServiceTest {

    @InjectMocks
    private UserAccountDetailsService userAccountDetailsService;

    @Mock
    private SpotifyApiService spotifyApiService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private Model model;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void populateViewModelWithUserDetails() {
        UserDto userDto = new UserDto();
        userDto.setDisplayName("Test User");
        userDto.setImages(new ImageDto[]{ new ImageDto().setUrl("https://test.url") });

        UserAccount userAccount = new UserAccount();
        userAccount.setApproved(true);

        when(spotifyApiService.setAuthorizedClient(any())).thenReturn(spotifyApiService);
        when(spotifyApiService.getUser()).thenReturn(userDto);

        when(userAccountService.setAuthorizedClient(any())).thenReturn(userAccountService);
        when(userAccountService.getLoggedInUserAccount()).thenReturn(userAccount);

        userAccountDetailsService.populateViewModelWithUserDetails(model);

        verify(model).addAttribute("userAccountName", "Test User");
        verify(model).addAttribute("userAccountImage", "https://test.url");
        verify(model).addAttribute("userIsNotApproved", false);
    }

    @Test
    void getSpotifyApiService() {
        assertEquals(spotifyApiService, userAccountDetailsService.getSpotifyApiService());
    }

    @Test
    void getUserAccountService() {
        assertEquals(userAccountService, userAccountDetailsService.getUserAccountService());
    }

    @Test
    void getAuthorizedClient() {
        userAccountDetailsService.setAuthorizedClient(authorizedClient);
        assertEquals(authorizedClient, userAccountDetailsService.getAuthorizedClient());
    }

    @Test
    void setAuthorizedClient() {
        userAccountDetailsService.setAuthorizedClient(authorizedClient);
        assertEquals(authorizedClient, userAccountDetailsService.getAuthorizedClient());
    }
}
