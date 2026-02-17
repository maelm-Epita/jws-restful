package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.converter.ItemConverter;
import fr.epita.assistants.yakamon.domain.service.GameService;
import fr.epita.assistants.yakamon.domain.service.ItemService;
import fr.epita.assistants.yakamon.presentation.api.response.InventoryResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("/inventory")
public class InventoryResource {
    @Inject
    GameService gameService;
    @Inject
    ItemService itemService;

    @Path("/")
    @GET
    public Response inventory() {
        if (!gameService.hasGameStarted())
            ErrorCode.NOT_RUNNING_ERROR.throwException();
        InventoryResponse inventoryResponse = ItemConverter.modelsToResponse(itemService.getItems());
        return Response.ok(inventoryResponse).build();
    }
}
