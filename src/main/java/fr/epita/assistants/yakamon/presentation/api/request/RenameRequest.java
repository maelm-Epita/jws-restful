package fr.epita.assistants.yakamon.presentation.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenameRequest {
    private final String newNickname;

    public RenameRequest(String newNickname) {
        this.newNickname = newNickname;
    }
}
