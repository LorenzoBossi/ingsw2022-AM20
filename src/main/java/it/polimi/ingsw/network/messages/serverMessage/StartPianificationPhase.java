package it.polimi.ingsw.network.messages.serverMessage;

public class StartPianificationPhase implements ServerMessage{
    private String targetPlayer;

    public StartPianificationPhase(String targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }
}
