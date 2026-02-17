package fr.epita.assistants.yakamon.data.repository;

import fr.epita.assistants.yakamon.data.model.PlayerModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<PlayerModel> {
}
