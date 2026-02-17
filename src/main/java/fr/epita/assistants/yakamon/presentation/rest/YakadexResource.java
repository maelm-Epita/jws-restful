package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.converter.YakadexEntryConverter;
import fr.epita.assistants.yakamon.domain.service.GameService;
import fr.epita.assistants.yakamon.domain.service.YakadexEntryService;
import fr.epita.assistants.yakamon.presentation.api.response.YakadexEntryResponse;
import fr.epita.assistants.yakamon.presentation.api.response.YakadexResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Produces(MediaType.APPLICATION_JSON)
@Path("/yakadex")
public class YakadexResource {
    @Inject
    GameService gameService;
    @Inject
    YakadexEntryService yakadexEntryService;

    @Path("/")
    @GET
    public Response yakadex(@QueryParam("only_missing") Boolean onlyMissing) {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        if (onlyMissing == null)
            onlyMissing = false;
        YakadexResponse yakadexResponse = YakadexEntryConverter.modelsToResponse(yakadexEntryService.getEntries(), onlyMissing);
        return Response.ok(yakadexResponse).build();
    }

    @Path("/{id}")
    @GET
    public Response yakadexWithId(@PathParam("id") String id) {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        YakadexEntryResponse yakadexEntryResponse = YakadexEntryConverter.modelToResponse(yakadexEntryService.getWithId(Integer.parseInt(id)));
        if (yakadexEntryResponse == null)
            ErrorCode.YAKADEX_NOT_EXIST_ERROR.throwException();
        return Response.ok(yakadexEntryResponse).build();
    }
}
