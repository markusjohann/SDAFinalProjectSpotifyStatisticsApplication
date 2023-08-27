package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharing;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountRepository;
import dev.coffeebeanteam.spotifyshare.service.SharingService;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;

@SpringBootTest(properties = {"spotify.api.client.id=testClientId", "spotify.api.client.secret=testClientSecret"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SharingControllerTest extends BaseControllerTest {
    @MockBean
    protected UserAccountRepository userAccountRepository;

    @MockBean
    protected UserAccountService userAccountService;

    @MockBean
    protected SharingService sharingService;

    @BeforeEach
    public void sharingControllerTestSetup() {
        when(userAccountDetailsService.setAuthorizedClient(any())).thenReturn(userAccountDetailsService);
        when(userAccountDetailsService.populateViewModelWithUserDetails(any())).thenReturn(userAccountDetailsService);
        when(topItemsGalleryService.populateModelViewWithTopItems(any(), any())).thenReturn(topItemsGalleryService);
    }

    @Test
    public void testIndexWithNoLoggedInUser() throws Exception {
        mockMvc.perform(get("/sharing"))
                .andExpect(status().is(302));
    }

    @Test
    public void testSharingViewWithNoLoggedInUser() throws Exception {
        mockMvc.perform(get("/sharing/view?accountId=1"))
                .andExpect(status().is(302));
    }

    @Test
    public void testSharingViewWithValidUser() throws Exception {
        UserAccount loggedInUser = new UserAccount()
                .setId(1L);
        UserAccount sharingUserAccount = new UserAccount()
                .setId(2L)
                .setSpotifyUsername("testUser");

        OAuth2AuthorizedClient authorizedClient = authorizedClientService
                .loadAuthorizedClient("client1", "test-principal-name");
        OAuth2AuthenticationToken authentication = getOauth2AuthenticationToken(authorizedClient);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sharingUserAccount));
        when(userAccountService.getLoggedInUserAccount()).thenReturn(loggedInUser);
        when(sharingService.getUserSharing(any(UserAccount.class), any(UserAccount.class)))
                .thenReturn(
                        Optional.ofNullable(
                                new UserAccountSharing()
                                        .setRequester(loggedInUser)
                                        .setRequestReceiver(sharingUserAccount)
                        )
                );

        mockMvc.perform(get("/sharing/view?accountId=1")
                        .with(securityContext(securityContext)))
                .andExpect(status().isOk())
                .andExpect(view().name("default-page"))
                .andExpect(model().attribute("pageTitle", "Top Artists and Tracks of testUser"))
                .andExpect(model().attribute("contentTitle", "Top Artists and Tracks of testUser"));

        verify(navBarService, times(1)).populateViewModelWithNavBarItems(any());
        verify(userAccountDetailsService, times(1)).populateViewModelWithUserDetails(any());
        verify(topItemsGalleryService, times(1)).populateModelViewWithTopItems(any(), eq(sharingUserAccount));
    }

    @Test
    public void testSharingViewWithInvalidUser() throws Exception {
        UserAccount loggedInUser = new UserAccount()
                .setId(1L);

        OAuth2AuthorizedClient authorizedClient = authorizedClientService
                .loadAuthorizedClient("client1", "test-principal-name");
        OAuth2AuthenticationToken authentication = getOauth2AuthenticationToken(authorizedClient);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        when(userAccountService.getLoggedInUserAccount()).thenReturn(loggedInUser);
        when(userAccountRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/sharing/view?accountId=1")
                .with(securityContext(securityContext)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(
                        HttpStatus.NOT_FOUND,
                        ((ResponseStatusException) result.getResolvedException()).getStatusCode())
                );
    }
}
