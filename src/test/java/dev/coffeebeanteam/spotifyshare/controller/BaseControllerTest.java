package dev.coffeebeanteam.spotifyshare.controller;

import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.content.TopItemsGalleryDto;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import dev.coffeebeanteam.spotifyshare.service.ui.NavBarService;
import dev.coffeebeanteam.spotifyshare.service.ui.TopItemsGalleryService;
import dev.coffeebeanteam.spotifyshare.service.ui.UserAccountDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

import java.time.Instant;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spotify.api.client.id=testClientId", "spotify.api.client.secret=testClientSecret"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected NavBarService navBarService;

    @MockBean
    protected UserAccountDetailsService userAccountDetailsService;

    @MockBean
    protected TopItemsGalleryService topItemsGalleryService;

    @MockBean
    protected OAuth2AuthorizedClientService authorizedClientService;

    @MockBean
    protected SpotifyApiService spotifyApiService;

    @BeforeEach
    public void setup() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("client1")
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .tokenUri("http://localhost:8080/auth/token")
                .authorizationUri("http://localhost:8080/auth/authorize")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/redirect")
                .scope("read")
                .build();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "fake-access-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Collections.singleton("read")
        );

        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                clientRegistration,
                "test-principal-name",
                accessToken
        );

        Mockito.when(authorizedClientService.loadAuthorizedClient(
                anyString(),
                anyString()
        )).thenReturn(authorizedClient);

        UserDto mockUserDto = new UserDto();
        TopItemsGalleryDto mockTopItemsGalleryDto = new TopItemsGalleryDto();

        when(spotifyApiService.setAuthorizedClient(any())).thenReturn(spotifyApiService);
        when(spotifyApiService.getUser()).thenReturn(mockUserDto);

        when(topItemsGalleryService.setAuthorizedClient(any())).thenReturn(topItemsGalleryService);
        when(topItemsGalleryService.getUserTopItemsGalleryDto(any())).thenReturn(mockTopItemsGalleryDto);
    }

    protected OAuth2AuthenticationToken getOauth2AuthenticationToken(OAuth2AuthorizedClient authorizedClient) {
        OAuth2User principal = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                Collections.singletonMap("name", "testName"),
                "name"
        );

        return new OAuth2AuthenticationToken(
                principal,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                authorizedClient.getClientRegistration().getRegistrationId()
        );
    }
}
