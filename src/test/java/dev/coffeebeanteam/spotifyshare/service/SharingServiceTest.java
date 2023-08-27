package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.ui.UserAccountDto;
import dev.coffeebeanteam.spotifyshare.model.*;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountSharingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SharingServiceTest {

    @Mock
    private UserAccountSharingRepository userAccountSharingRepository;

    @InjectMocks
    private SharingService sharingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestSharing() {
        UserAccount requester = new UserAccount().setId(1L);
        UserAccount requestReceiver = new UserAccount().setId(2L);

        when(userAccountSharingRepository.findById(any())).thenReturn(Optional.empty());

        sharingService.requestSharing(requester, requestReceiver);

        verify(userAccountSharingRepository, times(1)).save(any());
    }

    @Test
    void testRequestSharing_doesNothingWhenRequestExists() {
        UserAccount requester = new UserAccount();
        requester.setId(1L);

        UserAccount requestReceiver = new UserAccount();
        requestReceiver.setId(2L);

        UserAccountSharing existingSharing = new UserAccountSharing();

        when(userAccountSharingRepository.findById(any()))
                .thenAnswer(invocation -> {
                    Optional<UserAccountSharing> optional = Optional.of(existingSharing);
                    optional.ifPresentOrElse(
                            (existing) -> {},
                            () -> {
                                fail("Save method should not have been called.");
                            }
                    );
                    return optional;
                });

        sharingService.requestSharing(requester, requestReceiver);

        verify(userAccountSharingRepository, never()).save(any());
    }

    @Test
    void acceptSharingRequest() {
        UserAccountSharing sharing = new UserAccountSharing();

        UserAccount requester = new UserAccount();
        UserAccount receiver = new UserAccount();

        sharing.setRequestReceiver(receiver);
        sharing.setRequester(requester);

        when(userAccountSharingRepository.findById(any())).thenReturn(Optional.of(sharing));

        sharingService.acceptSharingRequest(new UserAccount(), new UserAccount());

        verify(userAccountSharingRepository, times(2)).save(any());
    }

    @Test
    void testAcceptSharingRequest_doesNothingWhenRequestDoesNotExist() {
        UserAccount accepter = new UserAccount();
        accepter.setId(1L);

        UserAccount requester = new UserAccount();
        requester.setId(2L);

        when(userAccountSharingRepository.findById(any()))
                .thenReturn(Optional.empty());

        sharingService.acceptSharingRequest(accepter, requester);

        verify(userAccountSharingRepository, never()).save(any());
    }

    @Test
    void rejectSharingRequest() {
        UserAccountSharing sharing = new UserAccountSharing();
        when(userAccountSharingRepository.findById(any())).thenReturn(Optional.of(sharing));

        sharingService.rejectSharingRequest(new UserAccount(), new UserAccount());

        verify(userAccountSharingRepository, times(1)).delete(any());
    }

    @Test
    void testRejectSharingRequest_doesNothingWhenRequestDoesNotExist() {
        UserAccount accepter = new UserAccount();
        accepter.setId(1L);

        UserAccount requester = new UserAccount();
        requester.setId(2L);

        when(userAccountSharingRepository.findById(any()))
                .thenReturn(Optional.empty());

        sharingService.rejectSharingRequest(accepter, requester);

        verify(userAccountSharingRepository, never()).delete(any());
    }


    @Test
    void getListOfPendingRequests() {
        UserAccount requester = new UserAccount();
        UserAccount receiver = new UserAccount();
        receiver.setId(123L);

        UserAccountSharing sharing = new UserAccountSharing();
        sharing.setRequestReceiver(receiver);

        when(userAccountSharingRepository.findByRequesterAndStatus(eq(requester), eq(SharingStatus.PENDING)))
                .thenReturn(Arrays.asList(sharing));

        List<UserAccountDto> result = sharingService.getListOfPendingRequests(requester);

        assertNotNull(result);
        assertEquals(1, result.size());
    }


    @Test
    void getListOfToAcceptRequests() {
        UserAccount requestReceiver = new UserAccount();
        UserAccount requester = new UserAccount();
        requester.setId(123L);

        UserAccountSharing sharing = new UserAccountSharing();
        sharing.setRequester(requester);

        when(userAccountSharingRepository.findByRequestReceiverAndStatus(eq(requestReceiver), eq(SharingStatus.PENDING)))
                .thenReturn(Arrays.asList(sharing));

        assertNotNull(sharingService.getListOfToAcceptRequests(requestReceiver));
    }


    @Test
    void getListOfSharingUserAccounts() {
        UserAccount loggedInUser = new UserAccount();
        UserAccount sharedUser = new UserAccount();
        sharedUser.setId(123L);

        UserAccountSharing sharing = new UserAccountSharing();
        sharing.setRequestReceiver(sharedUser);

        when(userAccountSharingRepository.findByRequesterAndStatus(eq(loggedInUser), eq(SharingStatus.ACCEPTED)))
                .thenReturn(Arrays.asList(sharing));

        assertNotNull(sharingService.getListOfSharingUserAccounts(loggedInUser));
    }

    @Test
    void getUserSharing_ShouldReturnEmptyWhenEitherSideNotAccepted() {
        UserAccount userOne = new UserAccount().setId(1L);
        UserAccount userTwo = new UserAccount().setId(2L);

        UserAccountSharing sharing = new UserAccountSharing().setStatus(SharingStatus.PENDING);
        UserAccountSharing reverseSharing = new UserAccountSharing().setStatus(SharingStatus.ACCEPTED);

        when(userAccountSharingRepository.findById(any()))
                .thenReturn(Optional.of(sharing), Optional.of(reverseSharing));

        Optional<UserAccountSharing> result = sharingService.getUserSharing(userOne, userTwo);

        assertFalse(result.isPresent());
    }

    @Test
    void cancelUserSharing_ShouldDeleteBothRequests() {
        UserAccount userOne = new UserAccount().setId(1L);
        UserAccount userTwo = new UserAccount().setId(2L);

        sharingService.cancelUserSharing(userOne, userTwo);

        verify(userAccountSharingRepository, times(2)).deleteById(any());
    }
}

