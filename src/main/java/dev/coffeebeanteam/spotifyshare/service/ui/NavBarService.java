package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.CollapsibleNavItemDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.DividerDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.HeadingDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.NavItemDto;
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
            add(new DividerDto().setCss("my-0"));
            add(new NavItemDto()
                    .setTitle("Dashboard")
                    .setIconCss("fas fa-fw fa-music")
            );
            add(new DividerDto());
            add(new HeadingDto().setTitle("Sharing"));
            add(new CollapsibleNavItemDto()
                    .setTitle("Other's Top Items")
                    .setSubTitle("Sharing with you")
                    .setIconCss("fas fa-users")
                    .addNavItem("User 1", "#", "")
                    .addNavItem("User 2", "#", "")
                    .addNavItem("User 3", "#", "")
            );
            add(new DividerDto().setCss("my-0"));
            add(new CollapsibleNavItemDto()
                    .setTitle("Other's Requests")
                    .setSubTitle("Waiting for your accept")
                    .setIconCss("fas fa-envelope")
                    .addNavItem("User 4", "#", "")
                    .addNavItem("User 5", "#", "")
                    .addNavItem("User 6", "#", "")
            );
            add(new DividerDto().setCss("my-0"));
            add(new CollapsibleNavItemDto()
                    .setTitle("Your Requests")
                    .setSubTitle("Waiting for accept")
                    .setIconCss("fas fa-envelope")
                    .addNavItem("User 7", "#", "")
                    .addNavItem("User 8", "#", "")
                    .addNavItem("User 9", "#", "")
            );
            add(new DividerDto());
        }};
    }

    public NavBarService populateViewModelWithNavBarItems(Model model) {
        model.addAttribute("navBarItems", getNavBarItems());

        return this;
    }
}
