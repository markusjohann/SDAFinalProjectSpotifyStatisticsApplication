package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.ui.UserAccountDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.service.SharingService;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NavBarServiceTest {

    @Mock
    private SharingService sharingService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private Model model;

    @InjectMocks
    private NavBarService navBarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNavBarItems() {
        UserAccount mockUser = new UserAccount();
        when(userAccountService.getLoggedInUserAccount()).thenReturn(mockUser);
        when(sharingService.getListOfSharingUserAccounts(mockUser)).thenReturn(Collections.emptyList());
        when(sharingService.getListOfToAcceptRequests(mockUser)).thenReturn(Collections.emptyList());
        when(sharingService.getListOfPendingRequests(mockUser)).thenReturn(Collections.emptyList());

        assertNotNull(navBarService.getNavBarItems());
    }

    @Test
    void populateViewModelWithNavBarItems() {
        UserAccount mockUser = new UserAccount();
        when(userAccountService.getLoggedInUserAccount()).thenReturn(mockUser);
        when(sharingService.getListOfSharingUserAccounts(mockUser)).thenReturn(Collections.emptyList());
        when(sharingService.getListOfToAcceptRequests(mockUser)).thenReturn(Collections.emptyList());
        when(sharingService.getListOfPendingRequests(mockUser)).thenReturn(Collections.emptyList());

        navBarService.populateViewModelWithNavBarItems(model);

        verify(model, times(1)).addAttribute(eq("navBarItems"), any());
    }
}
