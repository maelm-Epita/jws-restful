package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.*;
import fr.epita.assistants.yakamon.presentation.api.request.StartRequest;
import fr.epita.assistants.yakamon.presentation.api.response.StartResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.util.List;

@Path("/start")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameResource {
    @Inject
    private GameService gameService;

    @Path("/")
    @POST
    public Response start(StartRequest request) {
        if (request == null || request.playerName == null || request.mapPath == null ||
                request.playerName.isEmpty() || !new File(request.mapPath).exists() ||
                request.playerName.length() > 20) {
            ErrorCode.START_ERROR.throwException();
        }
        List<List<TileType>> tiles = gameService.startGame(request.playerName, request.mapPath);
        return Response.ok(new StartResponse(tiles)).build();
    }
}