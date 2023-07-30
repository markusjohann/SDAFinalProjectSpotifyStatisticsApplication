package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @NoArgsConstructor @Setter @Getter
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    private String spotifyUsername;
}
