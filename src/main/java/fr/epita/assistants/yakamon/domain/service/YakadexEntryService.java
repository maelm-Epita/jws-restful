package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.data.repository.YakadexEntryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApplicationScoped
public class YakadexEntryService {
    @Inject
    YakadexEntryRepository yakadexEntryRepository;

    @Transactional
    public void reset() {
        yakadexEntryRepository.getAll().stream()
                .forEach(yakadexEntryModel -> yakadexEntryModel.setCaught(false));
    }

    public YakadexEntryModel getWithId(Integer yakadexId) {
        return yakadexEntryRepository.findById(Long.valueOf(yakadexId));
    }

    public List<YakadexEntryModel> getEntries() {
        return yakadexEntryRepository.listAll();
    }
}
