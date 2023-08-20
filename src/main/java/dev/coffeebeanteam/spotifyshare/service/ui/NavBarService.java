package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.ui.UserAccountDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.CollapsibleNavItemDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.DividerDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.HeadingDto;
import dev.coffeebeanteam.spotifyshare.dto.ui.navbar.NavItemDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.service.SharingService;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.ArrayList;

@Service
public class NavBarService
{
    final private SharingService sharingService;

    final private UserAccountService userAccountService;

    public NavBarService(
            SharingService sharingService,
            UserAccountService userAccountService
            ) {
        this.sharingService = sharingService;
        this.userAccountService = userAccountService;
    }

    public List<Object> getNavBarItems() {
        final UserAccount loggedInUser = userAccountService.getLoggedInUserAccount();

        final List<Object> list = new ArrayList<>();

        final DividerDto divider1 = new DividerDto().setCss("my-0");
        final NavItemDto dashboardItem = new NavItemDto()
                .setLink("/dashboard")
                .setTitle("Dashboard")
                .setIconCss("fas fa-fw fa-music");

        final DividerDto divider2 = new DividerDto();

        final HeadingDto heading = new HeadingDto().setTitle("Sharing");

        final CollapsibleNavItemDto othersTopItems = new CollapsibleNavItemDto()
                .setTitle("Other's Top Items")
                .setSubTitle("Sharing with you")
                .setIconCss("fas fa-users");

        final List<UserAccountDto> usersSharingWithYou = sharingService.getListOfSharingUserAccounts(loggedInUser);

        final List<UserAccountDto> usersWaitingForYourAccept = sharingService.getListOfToAcceptRequests(loggedInUser);

        final List<UserAccountDto> yourRequestsToOtherUsers = sharingService
                .getListOfPendingRequests(loggedInUser);

        usersSharingWithYou.forEach(
                userAccountDto -> othersTopItems.addNavItem(
                        userAccountDto.getDisplayName(),
                        "/sharing/view?accountId=" + userAccountDto.getUserId(),
                        ""
                )
        );

        final DividerDto divider3 = new DividerDto().setCss("my-0");

        final CollapsibleNavItemDto othersRequests = new CollapsibleNavItemDto()
                .setTitle("Other's Requests")
                .setSubTitle("Waiting for your accept")
                .setIconCss("fas fa-envelope");

        usersWaitingForYourAccept.forEach(
                userAccountDto -> othersRequests.addNavItem(
                        userAccountDto.getDisplayName(),
                        "/sharing/request/accept-view?accountId=" + userAccountDto.getUserId(),
                        ""
                )
        );

        final DividerDto divider4 = new DividerDto().setCss("my-0");

        final CollapsibleNavItemDto yourRequests = new CollapsibleNavItemDto()
                .setTitle("Your Requests")
                .setSubTitle("Waiting for accept")
                .setIconCss("fas fa-envelope");

        yourRequestsToOtherUsers.forEach(
                userAccountDto -> yourRequests.addNavItem(
                        userAccountDto.getDisplayName(),
                        "",
                        ""
                )
        );

        final DividerDto divider5 = new DividerDto();

        list.add(divider1);
        list.add(dashboardItem);
        list.add(divider2);
        list.add(heading);
        list.add(othersTopItems);
        list.add(divider3);
        list.add(othersRequests);
        list.add(divider4);
        list.add(yourRequests);
        list.add(divider5);

        return list;
    }

    public NavBarService populateViewModelWithNavBarItems(Model model) {
        model.addAttribute("navBarItems", getNavBarItems());

        return this;
    }
}
