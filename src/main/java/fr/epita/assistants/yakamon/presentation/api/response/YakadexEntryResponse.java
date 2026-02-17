package fr.epita.assistants.yakamon.presentation.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.epita.assistants.yakamon.utils.ElementType;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class YakadexEntryResponse {
    private final Integer id;
    private final String name;
    private final ElementType firstType;
    private final ElementType secondType;
    private final Integer evolveThreshold;
    private final Integer evolutionId;
    private final Boolean caught;
    private final String description;

    public YakadexEntryResponse(Integer id, String name, ElementType firstType, ElementType secondType, Integer evolveThreshold, Integer evolutionId, Boolean caught, String description) {
        this.id = id;
        this.name = name;
        this.firstType = firstType;
        this.secondType = secondType;
        this.evolveThreshold = evolveThreshold;
        this.evolutionId = evolutionId;
        this.caught = caught;
        this.description = description;
    }
}
