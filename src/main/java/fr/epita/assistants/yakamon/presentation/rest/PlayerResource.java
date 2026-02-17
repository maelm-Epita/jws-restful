package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.converter.PlayerConverter;
import fr.epita.assistants.yakamon.converter.YakamonConverter;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.domain.service.*;
import fr.epita.assistants.yakamon.presentation.api.request.MoveRequest;
import fr.epita.assistants.yakamon.presentation.api.request.StartRequest;
import fr.epita.assistants.yakamon.presentation.api.response.CollectResponse;
import fr.epita.assistants.yakamon.presentation.api.response.MoveResponse;
import fr.epita.assistants.yakamon.presentation.api.response.PlayerResponse;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.ErrorInfo;
import fr.epita.assistants.yakamon.utils.Point;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
    @Inject
    private GameService gameService;
    @Inject
    private PlayerService playerService;

    @Path("/player")
    @GET
    public Response player() {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        PlayerResponse playerResponse = PlayerConverter.modelToResponse(playerService.getPlayer());
        return Response.ok(playerResponse).build();
    }

    @Path("/catch")
    @POST
    public Response catchYakamon() {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        YakamonModel yakamonModel = playerService.catchYakamon();
        YakamonResponse yakamonResponse = YakamonConverter.modelToResponse(yakamonModel);
        return Response.ok(yakamonResponse).build();
    }

    @Path("/collect")
    @POST
    public Response collectItem() {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        TileType tile = playerService.collectItem();
        CollectResponse collectResponse = new CollectResponse(tile);
        return Response.ok(collectResponse).build();
    }

    @Path("/move")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response move(MoveRequest moveRequest) {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        if (moveRequest == null || moveRequest.getDirection() == null)
            ErrorCode.MOVE_DIRECTION_ERROR.throwException();
        Point position = playerService.movePlayer(moveRequest.getDirection());
        MoveResponse moveResponse = PlayerConverter.pointToMoveResponse(position);
        return Response.ok(moveResponse).build();
    }

}