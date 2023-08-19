package dev.coffeebeanteam.spotifyshare.dto.ui.navbar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class NavItemDto {
    private String iconCss;
    private String title;
    private String link;
}
