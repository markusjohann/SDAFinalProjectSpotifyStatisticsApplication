package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.CollapsibleNavItem;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.Divider;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.Heading;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.NavItem;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.ArrayList;

@Service
public class NavBarService
{
    public List<Object> getNavBarItems()
    {
        return new ArrayList() {{
            add(new Divider().setCss("my-0"));
            add(new NavItem()
                    .setTitle("Dashboard")
                    .setIconCss("fas fa-fw fa-music")
            );
            add(new Divider());
            add(new Heading().setTitle("Sharing"));
            add(new CollapsibleNavItem()
                    .setTitle("Other's Top Items")
                    .setSubTitle("Sharing with you")
                    .setIconCss("fas fa-users")
                    .addNavItem("User 1", "#", "")
                    .addNavItem("User 2", "#", "")
                    .addNavItem("User 3", "#", "")
            );
            add(new Divider().setCss("my-0"));
            add(new CollapsibleNavItem()
                    .setTitle("Other's Requests")
                    .setSubTitle("Waiting for your accept")
                    .setIconCss("fas fa-envelope")
                    .addNavItem("User 4", "#", "")
                    .addNavItem("User 5", "#", "")
                    .addNavItem("User 6", "#", "")
            );
            add(new Divider().setCss("my-0"));
            add(new CollapsibleNavItem()
                    .setTitle("Your Requests")
                    .setSubTitle("Waiting for accept")
                    .setIconCss("fas fa-envelope")
                    .addNavItem("User 7", "#", "")
                    .addNavItem("User 8", "#", "")
                    .addNavItem("User 9", "#", "")
            );
            add(new Divider());
        }};
    }

    public NavBarService populateViewModelWithNavBarItems(Model model) {
        model.addAttribute("navBarItems", getNavBarItems());

        return this;
    }
}
