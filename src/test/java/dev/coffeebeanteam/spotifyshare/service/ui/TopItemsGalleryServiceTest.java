package dev.coffeebeanteam.spotifyshare.service.ui;

import dev.coffeebeanteam.spotifyshare.dto.ui.content.TopItemsGalleryDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserTopItem;
import dev.coffeebeanteam.spotifyshare.service.UserAccountService;
import dev.coffeebeanteam.spotifyshare.service.UserTopItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TopItemsGalleryServiceTest {

    @InjectMocks @Spy
    private TopItemsGalleryService topItemsGalleryService;

    @Mock
    private UserTopItemsService topItemsService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private Model model;

    @Mock
    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLoggedInUserTopItemsGalleryDtoTest() {
        UserAccount userAccount = new UserAccount();
        List<UserTopItem> topItems = new ArrayList<>(); // Modify the list as per your needs

        when(userAccountService.getLoggedInUserAccount()).thenReturn(userAccount);
        when(topItemsService.getTopItemsByUser(any())).thenReturn(topItems);
        when(userAccountService.setAuthorizedClient(any())).thenReturn(userAccountService);

        TopItemsGalleryDto result = topItemsGalleryService.getLoggedInUserTopItemsGalleryDto();
        assertNotNull(result);
    }

    @Test
    void getUserTopItemsGalleryDtoTest() {
        UserAccount userAccount = new UserAccount();
        List<UserTopItem> topItems = new ArrayList<>();

        when(topItemsService.getTopItemsByUser(any())).thenReturn(topItems);
        when(userAccountService.setAuthorizedClient(any())).thenReturn(userAccountService);

        TopItemsGalleryDto result = topItemsGalleryService.getUserTopItemsGalleryDto(userAccount);
        assertNotNull(result);
    }

    @Test
    public void populateModelViewWithTopItems_shouldAddAttribute() {
        final TopItemsGalleryDto dto = new TopItemsGalleryDto();

        when(userAccountService.setAuthorizedClient(any())).thenReturn(userAccountService);
        when(topItemsGalleryService.getUserTopItemsGalleryDto(any())).thenReturn(dto);

        topItemsGalleryService.populateModelViewWithTopItems(model);

        verify(model, times(1)).addAttribute("topItems", dto);
    }

    @Test
    public void populateModelViewWithTopItems_withUserAccount_shouldAddAttribute() {
        final TopItemsGalleryDto dto = new TopItemsGalleryDto();

        when(userAccountService.setAuthorizedClient(any())).thenReturn(userAccountService);
        doReturn(userAccountService).when(userAccountService).setAuthorizedClient(any());

        when(topItemsGalleryService.getUserTopItemsGalleryDto(any())).thenReturn(dto);

        topItemsGalleryService.populateModelViewWithTopItems(model, userAccount);

        verify(model, times(1)).addAttribute("topItems", dto);
    }
}
