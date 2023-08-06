package dev.coffeebeanteam.spotifyshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class TopItemDto {
    @JsonProperty("external_urls")
    private ExternalUrlsDto externalUrls;
    private FollowersDto followers;
    private List<String> genres;
    private String href;
    private String id;
    private List<ImageDto> images;
    private String name;
    private int popularity;
    private String type;
    private String uri;
}
