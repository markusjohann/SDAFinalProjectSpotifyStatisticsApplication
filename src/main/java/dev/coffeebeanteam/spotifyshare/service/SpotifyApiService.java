package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.configuration.SpotifyApiConfiguration;
import org.springframework.stereotype.Service;

@Service
public class SpotifyApiService {
    private SpotifyApiConfiguration apiConf;

    public SpotifyApiService(SpotifyApiConfiguration apiConf) {
        this.apiConf = apiConf;
    }
}
