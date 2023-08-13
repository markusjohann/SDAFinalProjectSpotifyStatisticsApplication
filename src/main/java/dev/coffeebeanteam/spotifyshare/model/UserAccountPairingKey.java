package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Embeddable @Getter @Setter @NoArgsConstructor
public class UserAccountPairingKey implements Serializable {

    @Column(name="user_account_id_one")
    private long userAccountIdOne;
    @Column(name="user_account_id_two")
    private long userAccountIdTwo;



}
