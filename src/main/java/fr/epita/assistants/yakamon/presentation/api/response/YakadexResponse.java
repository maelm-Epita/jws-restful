package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.utils.ElementType;
import lombok.Getter;

import java.util.List;

@Getter
public class YakadexResponse {
    private final List<YakadexEntryResponse> entries;

    public YakadexResponse(List<YakadexEntryResponse> entries) {
        this.entries = entries;
    }
}
