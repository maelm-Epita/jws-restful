package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.data.repository.YakadexEntryRepository;
import fr.epita.assistants.yakamon.presentation.api.response.YakadexEntryResponse;
import fr.epita.assistants.yakamon.presentation.api.response.YakadexResponse;

import java.util.ArrayList;
import java.util.List;

public class YakadexEntryConverter {
    public static YakadexEntryResponse modelToResponse(YakadexEntryModel yakadexEntryModel) {
        if (yakadexEntryModel == null)
            return null;
        if (!yakadexEntryModel.getCaught()) {
            return new YakadexEntryResponse(
                    yakadexEntryModel.getId(),
                    yakadexEntryModel.getName(),
                    null,
                    null,
                    null,
                    null,
                    false,
                    null
            );
        } else {
            return new YakadexEntryResponse(
                    yakadexEntryModel.getId(),
                    yakadexEntryModel.getName(),
                    yakadexEntryModel.getFirstType(),
                    yakadexEntryModel.getSecondType(),
                    yakadexEntryModel.getEvolveThreshold(),
                    yakadexEntryModel.getEvolution() == null ? null : yakadexEntryModel.getEvolution().getId(),
                    true,
                    yakadexEntryModel.getDescription()
            );
        }
    }
    public static YakadexResponse modelsToResponse(List<YakadexEntryModel> yakadexEntryModelList, Boolean onlyMissing) {
        List<YakadexEntryResponse> entries = yakadexEntryModelList.stream()
                .map(YakadexEntryConverter::modelToResponse).toList();
        if (onlyMissing)
            entries = entries.stream()
                    .filter(yakadexEntryResponse -> !yakadexEntryResponse.getCaught()).toList();
        return new YakadexResponse(entries);
    }
}
