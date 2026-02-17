package fr.epita.assistants.yakamon_testsuite.ServiceTests;

import fr.epita.assistants.yakamon.domain.service.YakadexEntryService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class YakadexEntryServiceTests {
    @Inject
    YakadexEntryService yakadexEntryService;

    @Transactional
    @BeforeEach
    public void setup() {
        yakadexEntryService.reset();
    }
}
