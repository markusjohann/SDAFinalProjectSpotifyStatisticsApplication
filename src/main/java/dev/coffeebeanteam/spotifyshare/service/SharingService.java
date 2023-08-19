package dev.coffeebeanteam.spotifyshare.service;

import dev.coffeebeanteam.spotifyshare.model.SharingStatus;
import dev.coffeebeanteam.spotifyshare.model.UserAccount;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharing;
import dev.coffeebeanteam.spotifyshare.model.UserAccountSharingKey;
import dev.coffeebeanteam.spotifyshare.repository.UserAccountPairingRepository;
import org.springframework.stereotype.Service;

@Service
public class SharingService {
    private final UserAccountPairingRepository userAccountPairingRepository;

    public SharingService(UserAccountPairingRepository userAccountPairingRepository) {
        this.userAccountPairingRepository = userAccountPairingRepository;
    }

    public SharingService requestSharing(UserAccount requester, UserAccount requestReceiver) {
        final UserAccountSharingKey requestId = new UserAccountSharingKey()
                .setUserAccountIdRequester(requester.getId())
                .setUserAccountIdReceiver(requestReceiver.getId());

        final UserAccountSharing pairing = new UserAccountSharing()
                .setId(requestId)
                .setRequester(requester)
                .setRequestReceiver(requestReceiver)
                .setStatus(SharingStatus.PENDING);

        userAccountPairingRepository.save(pairing);

        return this;
    }

    public SharingService acceptSharingRequest(UserAccount accepter, UserAccount requester) {
        final UserAccountSharingKey requestId = new UserAccountSharingKey()
                .setUserAccountIdRequester(requester.getId())
                .setUserAccountIdReceiver(accepter.getId());

        userAccountPairingRepository.findById(requestId).ifPresent(
                (UserAccountSharing request) -> {
                    userAccountPairingRepository.save(
                            new UserAccountSharing()
                                    .setId(
                                            new UserAccountSharingKey()
                                                    .setUserAccountIdRequester(request.getRequestReceiver().getId())
                                                    .setUserAccountIdReceiver(request.getRequester().getId()))
                                    .setRequester(request.getRequestReceiver())
                                    .setRequestReceiver(request.getRequester())
                                    .setStatus(SharingStatus.ACCEPTED)
                    );

                    userAccountPairingRepository.save(request.setStatus(SharingStatus.ACCEPTED));
                }
        );

        return this;
    }

    public SharingService rejectSharingRequest(UserAccount accepter, UserAccount requester) {
        final UserAccountSharingKey requestId = new UserAccountSharingKey()
                .setUserAccountIdRequester(requester.getId())
                .setUserAccountIdReceiver(accepter.getId());

        userAccountPairingRepository.findById(requestId).ifPresent(
                (UserAccountSharing request) -> userAccountPairingRepository.delete(request)
        );

        return this;
    }
}
