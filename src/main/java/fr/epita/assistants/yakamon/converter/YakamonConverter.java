package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonResponse;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonTeamResponse;

import java.util.List;

public class YakamonConverter {
    public static YakamonResponse modelToResponse(YakamonModel yakamonModel) {
        if (yakamonModel == null)
            return null;
        return new YakamonResponse(yakamonModel.getUuid(),
                yakamonModel.getNickname(),
                yakamonModel.getYakadexEntry().getId(),
                yakamonModel.getEnergyPoints());
    }
    public static YakamonTeamResponse modelsToTeamResponse(List<YakamonModel> yakamonModelList) {
        return new YakamonTeamResponse(yakamonModelList.stream()
                .map(YakamonConverter::modelToResponse).toList());
    }
}
