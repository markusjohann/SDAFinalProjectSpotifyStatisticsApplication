package dev.coffeebeanteam.spotifyshare.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration @Getter @NoArgsConstructor
public class SpotifyApiConfiguration {

    private String apiClientId;

    private String apiClientSecret;

    @Value("classpath:secrets/spotify_api_client_id")
    private Resource clientApiIdResource;

    @Value("classpath:secrets/spotify_api_client_secret")
    private Resource clientApiSecretResource;

    @PostConstruct
    public void init() throws IOException {
        this.apiClientId = FileCopyUtils.copyToString(
                new InputStreamReader(clientApiIdResource.getInputStream(), StandardCharsets.UTF_8));
        this.apiClientSecret = FileCopyUtils.copyToString(
                new InputStreamReader(clientApiSecretResource.getInputStream(), StandardCharsets.UTF_8));
    }
}
