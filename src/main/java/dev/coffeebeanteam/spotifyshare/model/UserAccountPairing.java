package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserAccountPairing {

    @EmbeddedId
    private UserAccountPairingKey id;

    @ManyToOne
    @MapsId("userAccountOne")
    @JoinColumn(name="user_account_id_one")
    private UserAccount userAccountOne;

    @ManyToOne
    @MapsId("userAccountTwo")
    @JoinColumn(name="user_account_id_two")
    private UserAccount userAccountTwo;

    private boolean requestAcceptedOne;
    private boolean requestAcceptedTwo;

}
