package dev.coffeebeanteam.spotifyshare.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class UserAccountSearchResultDto {
    public long userId;
    public String displayName;
}
