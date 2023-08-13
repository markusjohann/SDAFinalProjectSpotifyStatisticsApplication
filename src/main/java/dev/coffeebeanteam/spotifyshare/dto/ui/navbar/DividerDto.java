package dev.coffeebeanteam.spotifyshare.dto.ui.navbar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class DividerDto {
    private String title;
    private String css;
}
