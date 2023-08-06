package dev.coffeebeanteam.spotifyshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
    private String country;

    @JsonProperty("display_name")
    private String displayName;

    private String email;

    @JsonProperty("explicit_content")
    private ExplicitContentDto explicitContent;

    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;

    private FollowersDto followers;
    private String href;
    private String id;
    private ImageDto[] images;
    private String product;
    private String type;
    private String uri;
}

