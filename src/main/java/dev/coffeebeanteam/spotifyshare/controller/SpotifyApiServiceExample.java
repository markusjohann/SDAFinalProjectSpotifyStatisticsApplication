package dev.coffeebeanteam.spotifyshare.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coffeebeanteam.spotifyshare.dto.SpotifyUserDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import dev.coffeebeanteam.spotifyshare.service.SpotifyApiService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpotifyApiServiceExample {
    final private SpotifyApiService spotifyApiService;
    final private ObjectMapper objectMapper;

    public SpotifyApiServiceExample(
            SpotifyApiService spotifyApiService,
            ObjectMapper objectMapper
    ) {
        this.spotifyApiService = spotifyApiService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping("/")
    @ResponseBody
    public String spotifyApiRequestExample(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient
    ) throws JsonProcessingException {
        spotifyApiService.setAuthorizedClient(authorizedClient);

        final SpotifyUserDto spotifyUser = spotifyApiService.getUser();

        final TopItemsResponseDto topArtists = spotifyApiService.getTopArtists();

        final TopItemsResponseDto topTracks = spotifyApiService.getTopTracks();

        return "<pre>" +
                "USER DETAILS:\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(spotifyUser) +
                "\n\nTOP ARTISTS:\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topArtists) +
                "\n\nTOP TRACKS:\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topTracks) +
                "</pre>";
    }
}
