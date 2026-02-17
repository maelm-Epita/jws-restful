package fr.epita.assistants.yakamon.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "yakamon")
public class YakamonModel {
    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID uuid;
    @Column(length = 20) private String nickname;
    @Column(name = "energy_points") private Integer energyPoints;
    @ManyToOne @JoinColumn(name = "yakadex_entry_id") private YakadexEntryModel yakadexEntry;
}
