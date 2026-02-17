package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.converter.YakamonConverter;
import fr.epita.assistants.yakamon.domain.service.GameService;
import fr.epita.assistants.yakamon.domain.service.PlayerService;
import fr.epita.assistants.yakamon.domain.service.YakamonService;
import fr.epita.assistants.yakamon.presentation.api.request.FeedRequest;
import fr.epita.assistants.yakamon.presentation.api.request.RenameRequest;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonResponse;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonTeamResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {
    @Inject
    private GameService gameService;
    @Inject
    private YakamonService yakamonService;

    @Path("/")
    @GET
    public Response team() {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        YakamonTeamResponse yakamonTeamResponse = YakamonConverter.modelsToTeamResponse(yakamonService.getTeam());
        return Response.ok(yakamonTeamResponse).build();
    }

    @Path("/{uuid}/rename")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rename(RenameRequest renameRequest, @PathParam("uuid") UUID uuid) {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        String newNickName = renameRequest.getNewNickname();
        if (newNickName == null || newNickName.isEmpty() || newNickName.isBlank() || newNickName.length() > 20)
            ErrorCode.YAKAMON_RENAME_NICKNAME_ERROR.throwException();
        YakamonResponse yakamonResponse = YakamonConverter
                .modelToResponse(yakamonService.renameYakamon(renameRequest.getNewNickname(), uuid));
        return Response.ok(yakamonResponse).build();
    }

    @Path("/{uuid}/release")
    @DELETE
    public Response release(@PathParam("uuid") UUID uuid) {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        yakamonService.releaseYakamon(uuid);
        return Response.status(204).build();
    }

    @Path("/{uuid}/feed")
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response feed(FeedRequest feedRequest, @PathParam("uuid") UUID uuid) {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        YakamonResponse yakamonResponse = YakamonConverter
                .modelToResponse(yakamonService.feedYakamon(feedRequest.getQuantity(), uuid));
        return Response.ok(yakamonResponse).build();
    }

    @Path("/{uuid}/evolve")
    @POST
    public Response evolve(@PathParam("uuid") UUID uuid) {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        YakamonResponse yakamonResponse = YakamonConverter
                .modelToResponse(yakamonService.evolveYakamon(uuid));
        return Response.ok(yakamonResponse).build();
    }
}
