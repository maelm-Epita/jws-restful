package fr.epita.assistants.yakamon.data.model;

import fr.epita.assistants.yakamon.utils.ElementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "yakadex_entry")
public class YakadexEntryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 20) private String name;
    private Boolean caught;
    @Enumerated(EnumType.STRING) @Column(name = "first_type") private ElementType firstType;
    @Enumerated(EnumType.STRING) @Column(name = "second_type") private ElementType secondType;
    private String description;
    @ManyToOne @JoinColumn(name = "evolution_id") private YakadexEntryModel evolution;
    @Column(name = "evolve_threshold") private Integer evolveThreshold;
}