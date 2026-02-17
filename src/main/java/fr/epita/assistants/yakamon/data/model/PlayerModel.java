package fr.epita.assistants.yakamon.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity @Table(name = "player")
public class PlayerModel {
    @Id @UuidGenerator @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID uuid;
    @Column(length = 20) private String name;
    @Column(name = "pos_x") private Integer posX;
    @Column(name = "pos_y") private Integer posY;
    @Column(name = "last_move") LocalDateTime lastMove;
    @Column(name = "last_catch") LocalDateTime lastCatch;
    @Column(name = "last_collect") LocalDateTime lastCollect;
    @Column(name = "last_feed") LocalDateTime lastFeed;
}
