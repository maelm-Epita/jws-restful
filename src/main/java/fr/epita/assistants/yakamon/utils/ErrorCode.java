package fr.epita.assistants.yakamon.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.ws.rs.core.Response.Status;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    EXAMPLE_ERROR(Status.INTERNAL_SERVER_ERROR, "This is an error example"),
    START_ERROR(Status.BAD_REQUEST, "Invalid `path` or invalid `name` provided."),
    MOVE_DIRECTION_ERROR(Status.BAD_REQUEST, " Invalid direction or the game is not running."),
    MOVE_DELAY_ERROR(Status.TOO_MANY_REQUESTS, "Player has recently moved and must wait before moving again."),
    CATCH_INVALID_ERROR(Status.BAD_REQUEST, "No Yakamon at the current position, not enough Yakaballs, team is full or the game is not running."),
    CATCH_DELAY_ERROR(Status.TOO_MANY_REQUESTS, "Player has recently caught a yakamon and must wait."),
    COLLECT_INVALID_ERROR(Status.BAD_REQUEST, "Invalid tile or the game is not running."),
    COLLECT_DELAY_ERROR(Status.TOO_MANY_REQUESTS, "Player has recently collected and must wait before collecting again."),
    YAKADEX_NOT_EXIST_ERROR(Status.NOT_FOUND, "This yakamon does not exist."),
    YAKAMON_NOT_FOUND_ERROR(Status.NOT_FOUND, "The yakamon was not found."),
    YAKAMON_EVOLUTION_NOT_EXIST_ERROR(Status.NOT_FOUND, "This yakamon does not have an evolution."),
    YAKAMON_RENAME_NICKNAME_ERROR(Status.BAD_REQUEST, "The new nickname is invalid."),
    FFED_DELAY_ERROR(Status.TOO_MANY_REQUESTS, "Player has recently fed a yakamon and must wait before feeding one again."),
    FEED_INVALID_ERROR(Status.BAD_REQUEST, "Not enough Scrooge or invalid amount (.i.e <= 0)"),
    RELEASE_UNABLE_ERROR(Status.FORBIDDEN, "The player cannot release the last yakamon that can walk on the player's current tile."),
    EVOLVE_INVALID_ERROR(Status.BAD_REQUEST, "The yakamon needs more energy points to evolve."),
    NOT_RUNNING_ERROR(Status.BAD_REQUEST, "The game is not running.");

    private final Response.Status errorCode;

    private final String errorMessage;

    public WebApplicationException getException() {
        return new WebApplicationException(Response.status(errorCode).entity(new ErrorInfo(errorMessage)).build());
    }

    public void throwException() {
        throw getException();
    }

    public void throwException(String prefix) {
        throw new WebApplicationException(Response.status(errorCode).entity(new ErrorInfo(prefix + ": " + errorMessage)).build());
    }
}
