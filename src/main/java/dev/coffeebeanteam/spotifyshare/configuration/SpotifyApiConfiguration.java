package dev.coffeebeanteam.spotifyshare.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration @Getter
public class SpotifyApiConfiguration {
    private final String apiClientId;

    private final String apiClientSecret;
    public SpotifyApiConfiguration(
            @Value("${spotify.api.client.id}") String apiClientId,
            @Value("${spotify.api.client.secret}") String apiClientSecret) {
        this.apiClientId = apiClientId;
        this.apiClientSecret = apiClientSecret;
    }
}
