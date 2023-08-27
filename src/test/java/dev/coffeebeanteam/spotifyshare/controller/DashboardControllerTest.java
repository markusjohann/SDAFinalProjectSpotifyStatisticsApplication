package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.content.TopItemsGalleryDto;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import dev.coffeebeanteam.spotifyshare.service.ui.NavBarService;
import dev.coffeebeanteam.spotifyshare.service.ui.TopItemsGalleryService;
import dev.coffeebeanteam.spotifyshare.service.ui.UserAccountDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.time.Instant;
import java.util.Collections;


@SpringBootTest(properties = {"spotify.api.client.id=testClientId", "spotify.api.client.secret=testClientSecret"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DashboardControllerTest extends BaseControllerTest {
    @Test
    public void testIndexWithNoLoggedInUser() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser
    public void testIndexWithLoggedInUser() throws Exception {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("client1", "test-principal-name");
        OAuth2AuthenticationToken authentication = getOauth2AuthenticationToken(authorizedClient);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        when(userAccountDetailsService.setAuthorizedClient(any()))
                .thenReturn(userAccountDetailsService);

        mockMvc.perform(get("/dashboard")
                        .with(SecurityMockMvcRequestPostProcessors.securityContext(securityContext)))
                .andExpect(status().isOk())
                .andExpect(view().name("default-page"))
                .andExpect(model().attribute("pageTitle", "Dashboard"))
                .andExpect(model().attribute("contentTitle", "Dashboard"));

        verify(navBarService, times(1)).populateViewModelWithNavBarItems(any());
        verify(userAccountDetailsService, times(1)).populateViewModelWithUserDetails(any());
        verify(topItemsGalleryService, times(1)).populateModelViewWithTopItems(any());
    }
}
