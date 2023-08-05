package dev.coffeebeanteam.spotifyshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class TopItemsResponseDto {
    private String href;
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int total;
    @JsonProperty("items")
    private List<TopItemDto> artists;
}
