package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SpotifyApiServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    private SpotifyApiService spotifyApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        spotifyApiService = new SpotifyApiService(webClientBuilder);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }


    @Test
    void getToken_whenTokenIsNullAndAuthorizedClientIsNotNull() {
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "testToken", null, null);
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);

        spotifyApiService.setAuthorizedClient(authorizedClient);
        spotifyApiService.setToken(null);

        String result = spotifyApiService.getToken();
        assertEquals("testToken", result);
    }

    @Test
    void getUser() {
        UserDto expectedUser = new UserDto(); // ... some setup code
        when(responseSpec.bodyToMono(UserDto.class)).thenReturn(Mono.just(expectedUser));

        UserDto result = spotifyApiService.getUser();

        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test

    void getTopArtists() {
        TopItemsResponseDto expectedResponse = new TopItemsResponseDto(); // ... some setup code
        when(responseSpec.bodyToMono(TopItemsResponseDto.class)).thenReturn(Mono.just(expectedResponse));

        TopItemsResponseDto result = spotifyApiService.getTopArtists();

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void getTopTracks() {
        TopItemsResponseDto expectedResponse = new TopItemsResponseDto(); // ... some setup code
        when(responseSpec.bodyToMono(TopItemsResponseDto.class)).thenReturn(Mono.just(expectedResponse));

        TopItemsResponseDto result = spotifyApiService.getTopTracks();

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void getTopItems() {
        TopItemsResponseDto expectedResponse = new TopItemsResponseDto(); // ... some setup code
        when(responseSpec.bodyToMono(TopItemsResponseDto.class)).thenReturn(Mono.just(expectedResponse));

        TopItemsResponseDto result = spotifyApiService.getTopItems("artists");

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void getAuthorizedClient() {
        OAuth2AuthorizedClient expectedAuthorizedClient = mock(OAuth2AuthorizedClient.class);
        spotifyApiService.setAuthorizedClient(expectedAuthorizedClient);

        OAuth2AuthorizedClient result = spotifyApiService.getAuthorizedClient();

        assertEquals(expectedAuthorizedClient, result);
    }

    @Test
    void setAuthorizedClient() {
        OAuth2AuthorizedClient expectedAuthorizedClient = mock(OAuth2AuthorizedClient.class);
        spotifyApiService.setAuthorizedClient(expectedAuthorizedClient);

        OAuth2AuthorizedClient result = spotifyApiService.getAuthorizedClient();

        assertEquals(expectedAuthorizedClient, result);
    }

    @Test
    void setToken() {
        String expectedToken = "testToken";
        spotifyApiService.setToken(expectedToken);

        String result = spotifyApiService.getToken();

        assertEquals(expectedToken, result);
    }
}
