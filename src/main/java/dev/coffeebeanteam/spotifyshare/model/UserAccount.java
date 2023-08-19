package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;


@Entity @NoArgsConstructor @Setter @Getter @Accessors(chain = true)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(unique = true)
    private String spotifyUsername;

    @Column(unique = true)
    private String spotifyPrincipalName;

    @Column(unique = true)
    private String spotifyEmail;

    @OneToMany(mappedBy = "userAccountOne")
    private Set<UserAccountPairing> requestAcceptedOne;
    @OneToMany(mappedBy = "userAccountTwo")
    private Set<UserAccountPairing> requestAcceptedTwo;


}
