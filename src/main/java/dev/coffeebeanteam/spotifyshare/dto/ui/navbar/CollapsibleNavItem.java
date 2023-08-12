package dev.coffeebeanteam.spotifyshare.dto.ui.navbar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class CollapsibleNavItem {
    private String title;
    private String iconCss;
    private String subTitle;

    private List<NavItem> navItems = new ArrayList<>();

    private String collapseElementId = UUID.randomUUID().toString();

    public CollapsibleNavItem addNavItem(String title, String link, String iconCss) {
        navItems.add(
                new NavItem()
                        .setTitle(title)
                        .setLink(link)
                        .setIconCss(iconCss)
        );

        return this;
    }
}
