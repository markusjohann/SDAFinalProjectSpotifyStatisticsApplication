package dev.coffeebeanteam.spotifyshare.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserTopItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserAccount userAccount;

    @Column
    private String name;

    @ElementCollection
    private List<String> artistSpotifyIds;

    @Column
    private String spotifyId;

    @Column
    private Integer track_number;

    @Column
    private long totalPlaytime;

    @ElementCollection
    private List<String> images;

    @Column
    private String type;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
