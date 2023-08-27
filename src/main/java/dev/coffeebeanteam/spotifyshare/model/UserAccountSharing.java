package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class UserAccountSharing {

    @EmbeddedId
    private UserAccountSharingKey id;

    @ManyToOne
    @MapsId("requester")
    @JoinColumn(name="user_account_id_requester")
    private UserAccount requester;

    @ManyToOne
    @MapsId("requestReceiver")
    @JoinColumn(name="user_account_id_receiver")
    private UserAccount requestReceiver;

    @Enumerated(EnumType.STRING)
    private SharingStatus status;
}
