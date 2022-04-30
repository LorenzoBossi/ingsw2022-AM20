package it.polimi.ingsw.network.messages.serverMessage;

public class StartActionPhase implements ServerMessage {
    private String targetPlayer;

    public StartActionPhase(String targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }
}
