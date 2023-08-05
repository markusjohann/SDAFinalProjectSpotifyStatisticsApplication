package dev.coffeebeanteam.spotifyshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExplicitContentDto {
    @JsonProperty("filter_enabled")
    private boolean filterEnabled;

    @JsonProperty("filter_locked")
    private boolean filterLocked;
}
