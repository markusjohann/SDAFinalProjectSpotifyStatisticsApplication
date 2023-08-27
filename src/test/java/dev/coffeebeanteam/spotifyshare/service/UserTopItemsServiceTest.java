package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.TopItemDto;
import dev.coffeebeanteam.spotifyshare.dto.TopItemsResponseDto;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserTopItem;
import dev.coffeebeanteam.spotifyshare.repository.UserTopItemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserTopItemsServiceTest {

    @Mock
    private UserTopItemsRepository userTopItemsRepository;

    @InjectMocks
    private UserTopItemsService userTopItemsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void clearUserTopItems() {
        UserAccount mockUserAccount = mock(UserAccount.class);

        userTopItemsService.clearUserTopItems(mockUserAccount);

        verify(userTopItemsRepository, times(1)).deleteByUserAccount(mockUserAccount);
    }

    @Test
    void syncUserTopItemsFromDto() {
        TopItemsResponseDto mockTopItems = mock(TopItemsResponseDto.class);
        UserAccount mockUserAccount = mock(UserAccount.class);
        TopItemDto mockTopItemDto = mock(TopItemDto.class);

        when(mockTopItems.getItems()).thenReturn(List.of(mockTopItemDto));
        when(mockTopItemDto.getId()).thenReturn("sampleId");
        when(mockTopItemDto.getName()).thenReturn("sampleName");

        userTopItemsService.syncUserTopItemsFromDto(mockTopItems, mockUserAccount);

        verify(userTopItemsRepository, times(1)).save(any(UserTopItem.class));
    }

    @Test
    void getTopItemsByUser() {
        UserAccount mockUserAccount = mock(UserAccount.class);
        List<UserTopItem> mockTopItems = List.of(mock(UserTopItem.class));

        when(userTopItemsRepository.findByUserAccount(mockUserAccount)).thenReturn(mockTopItems);

        List<UserTopItem> result = userTopItemsService.getTopItemsByUser(mockUserAccount);

        assertEquals(mockTopItems, result);
        verify(userTopItemsRepository, times(1)).findByUserAccount(mockUserAccount);
    }
}
