package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Embeddable @Getter @Setter @NoArgsConstructor @Accessors(chain = true)
public class UserAccountSharingKey implements Serializable {

    @Column(name="user_account_id_requester")
    private long userAccountIdRequester;
    @Column(name="user_account_id_receiver")
    private long userAccountIdReceiver;
}
