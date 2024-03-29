package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.dto.ui.UserAccountDto;
import dev.coffeebeanteam.spotifyshare.model.SharingStatus;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharing;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharingKey;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountSharingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SharingService {
    private final UserAccountSharingRepository userAccountSharingRepository;

    public SharingService(UserAccountSharingRepository userAccountSharingRepository) {
        this.userAccountSharingRepository = userAccountSharingRepository;
    }

    public SharingService requestSharing(UserAccount requester, UserAccount requestReceiver) {
        final UserAccountSharingKey requestId = new UserAccountSharingKey()
                .setUserAccountIdRequester(requester.getId())
                .setUserAccountIdReceiver(requestReceiver.getId());

        userAccountSharingRepository.findById(requestId).ifPresentOrElse(
                (existingSharing) -> {},
                () -> {
                    final UserAccountSharing pairing = new UserAccountSharing()
                            .setId(requestId)
                            .setRequester(requester)
                            .setRequestReceiver(requestReceiver)
                            .setStatus(SharingStatus.PENDING);

                    userAccountSharingRepository.save(pairing);
                }
        );

        return this;
    }

    public SharingService acceptSharingRequest(UserAccount accepter, UserAccount requester) {
        final UserAccountSharingKey requestId = new UserAccountSharingKey()
                .setUserAccountIdRequester(requester.getId())
                .setUserAccountIdReceiver(accepter.getId());

        userAccountSharingRepository.findById(requestId).ifPresent(
                (UserAccountSharing request) -> {
                    userAccountSharingRepository.save(
                            new UserAccountSharing()
                                    .setId(
                                            new UserAccountSharingKey()
                                                    .setUserAccountIdRequester(request.getRequestReceiver().getId())
                                                    .setUserAccountIdReceiver(request.getRequester().getId()))
                                    .setRequester(request.getRequestReceiver())
                                    .setRequestReceiver(request.getRequester())
                                    .setStatus(SharingStatus.ACCEPTED)
                    );

                    userAccountSharingRepository.save(request.setStatus(SharingStatus.ACCEPTED));
                }
        );

        return this;
    }

    public SharingService rejectSharingRequest(UserAccount accepter, UserAccount requester) {
        final UserAccountSharingKey requestId = new UserAccountSharingKey()
                .setUserAccountIdRequester(requester.getId())
                .setUserAccountIdReceiver(accepter.getId());

        userAccountSharingRepository.findById(requestId).ifPresent(
                (UserAccountSharing request) -> userAccountSharingRepository.delete(request)
        );

        return this;
    }

    public List<UserAccountDto> getListOfPendingRequests(UserAccount requester) {
        List<UserAccountSharing> pendingRequests = userAccountSharingRepository.findByRequesterAndStatus(
                requester,
                SharingStatus.PENDING
        );

        return pendingRequests.stream()
                .map(request -> new UserAccountDto()
                        .setUserId(request.getRequestReceiver().getId())
                        .setDisplayName(request.getRequestReceiver().getSpotifyUsername()))
                .collect(Collectors.toList());
    }

    public List<UserAccountDto> getListOfToAcceptRequests(UserAccount requestReceiver) {
        List<UserAccountSharing> toAcceptRequests = userAccountSharingRepository.findByRequestReceiverAndStatus(
                requestReceiver, SharingStatus.PENDING);

        return toAcceptRequests.stream()
                .map(request -> new UserAccountDto()
                        .setUserId(request.getRequester().getId())
                        .setDisplayName(request.getRequester().getSpotifyUsername()))
                .collect(Collectors.toList());
    }

    public List<UserAccountDto> getListOfSharingUserAccounts(UserAccount loggedInUser) {
        List<UserAccountSharing> acceptedRequests = userAccountSharingRepository.findByRequesterAndStatus(
                loggedInUser,
                SharingStatus.ACCEPTED
        );

        return acceptedRequests.stream()
                .map(request -> new UserAccountDto()
                        .setUserId(request.getRequestReceiver().getId())
                        .setDisplayName(request.getRequestReceiver().getSpotifyUsername()))
                .collect(Collectors.toList());

    }

    public Optional<UserAccountSharing> getUserSharing(UserAccount userOne, UserAccount userTwo) {
        Long userAccountOneId = userOne.getId();
        Long userAccountTwoId = userTwo.getId();

        final UserAccountSharingKey userAccountSharingKey = new UserAccountSharingKey()
                .setUserAccountIdReceiver(userAccountOneId)
                .setUserAccountIdRequester(userAccountTwoId);

        final UserAccountSharingKey userAccountSharingReverseKey = new UserAccountSharingKey()
                .setUserAccountIdReceiver(userAccountTwoId)
                .setUserAccountIdRequester(userAccountOneId);

        Optional<UserAccountSharing> userAccountSharingOpt = userAccountSharingRepository.findById(userAccountSharingKey);
        Optional<UserAccountSharing> userAccountSharingReverseOpt = userAccountSharingRepository.findById(userAccountSharingReverseKey);

        if (userAccountSharingOpt.isPresent() && userAccountSharingReverseOpt.isPresent()) {
            UserAccountSharing userAccountSharing = userAccountSharingOpt.get();
            UserAccountSharing userAccountSharingReverse = userAccountSharingReverseOpt.get();

            if (userAccountSharing.getStatus() == SharingStatus.ACCEPTED &&
                    userAccountSharingReverse.getStatus() == SharingStatus.ACCEPTED) {
                return userAccountSharingOpt;
            }
        }

        return Optional.empty();
    }
    public SharingService cancelUserSharing(UserAccount userOne, UserAccount userTwo)
    {
        Long userAccountOneId = userOne.getId();
        Long userAccountTwoId = userTwo.getId();

        final UserAccountSharingKey userAccountSharingKey = new UserAccountSharingKey()
                .setUserAccountIdReceiver(userAccountOneId)
                .setUserAccountIdRequester(userAccountTwoId);

        final UserAccountSharingKey userAccountSharingReverseKey = new UserAccountSharingKey()
                .setUserAccountIdReceiver(userAccountTwoId)
                .setUserAccountIdRequester(userAccountOneId);

        userAccountSharingRepository.deleteById(userAccountSharingKey);
        userAccountSharingRepository.deleteById(userAccountSharingReverseKey);

        return this;
    }
}
