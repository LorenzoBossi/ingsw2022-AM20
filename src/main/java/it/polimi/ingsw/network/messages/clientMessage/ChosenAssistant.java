package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.AssistantName;

public class ChosenAssistant extends GameMessage{
    private AssistantName name;

    public ChosenAssistant(String nickname, AssistantName name) {
        super(nickname);
        this.name = name;
    }

    public AssistantName getName() {
        return name;
    }
}
