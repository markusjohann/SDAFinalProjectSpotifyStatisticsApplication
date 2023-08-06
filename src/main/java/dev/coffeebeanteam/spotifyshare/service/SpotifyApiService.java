package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.UserDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Setter
@Getter
@Accessors(chain = true)
public class SpotifyApiService {
    private OAuth2AuthorizedClient authorizedClient;
    private String token;
    private final WebClient webClient;

    @Autowired
    public SpotifyApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.spotify.com")
                .build();
    }

    public String getToken() {
        if (token == null && authorizedClient != null) {
            setToken(authorizedClient.getAccessToken().getTokenValue());
        }

        return token;
    }

    public UserDto getUser() {
        return webClient
                .get()
                .uri("/v1/me")
                .headers(headers -> headers.setBearerAuth(getToken()))
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public TopItemsResponseDto getTopArtists() {
        return getTopItems("artists");
    }

    public TopItemsResponseDto getTopTracks() {
        return getTopItems("tracks");
    }

    protected TopItemsResponseDto getTopItems(final String type) {
        return webClient
                .get()
                .uri("/v1/me/top/" + type + "?time_range=long_term&limit=50&offset=0")
                .headers(headers -> headers.setBearerAuth(getToken()))
                .retrieve()
                .bodyToMono(TopItemsResponseDto.class)
                .block();
    }
}

