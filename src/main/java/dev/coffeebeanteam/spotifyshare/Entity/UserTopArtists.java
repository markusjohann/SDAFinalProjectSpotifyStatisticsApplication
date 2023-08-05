package dev.coffeebeanteam.spotifyshare.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table
@Data @Getter @Setter
 @NoArgsConstructor
public class UserTopArtists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String userTopArtists;

    //total playtime or the total number of times the user has listened to the top songs
    private long totalPlaytime;

    //The time range for which the top songs are calculated "short_term," "medium_term," "long_term" etc
    private String timeRange;

    //ability to show when were the stats first created and updated if wanted
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;




}
