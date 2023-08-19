package dev.coffeebeanteam.spotifyshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TrackArtistDto {
    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;

    private String href;
    private String id;
    private String name;
    private String type;
    private String uri;
}
