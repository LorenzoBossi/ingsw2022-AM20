package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.AssistantName;

public class AssistantPlayed implements UpdateMessage {
    private String player;
    private String assistantName;
    private int playerPriority;
    private int maxMotherNatureMove;

    public AssistantPlayed(String player, AssistantName assistantName) {
        this.player = player;
        this.assistantName = assistantName.name();
        this.playerPriority = assistantName.getValue();
        this.maxMotherNatureMove = assistantName.getMotherNatureMove();
    }

    public String getPlayer() {
        return player;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public int getPlayerPriority() {
        return playerPriority;
    }

    public int getMaxMotherNatureMove() {
        return maxMotherNatureMove;
    }
}
