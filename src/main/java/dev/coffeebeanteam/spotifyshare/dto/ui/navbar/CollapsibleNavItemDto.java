package dev.coffeebeanteam.spotifyshare.dto.ui.navbar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class CollapsibleNavItemDto {
    private String title;
    private String iconCss;
    private String subTitle;

    private List<NavItemDto> navItems = new ArrayList<>();

    // Massage the random ids to make them valid HTML element ids
    private String collapseElementId = "id-" + UUID.randomUUID().toString().replace("-", "X");

    public CollapsibleNavItemDto addNavItem(String title, String link, String iconCss) {
        navItems.add(
                new NavItemDto()
                        .setTitle(title)
                        .setLink(link)
                        .setIconCss(iconCss)
        );

        return this;
    }
}
