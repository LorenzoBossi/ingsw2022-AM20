package it.polimi.ingsw.network.messages.serverMessage;


/**
 * Message sent by the server to notify to the current player that can start the action phase
 */
public class StartActionPhase implements ServerMessage {
    private final String targetPlayer;

    /**
     * Constructor
     * @param targetPlayer the nickname of the target player
     */
    public StartActionPhase(String targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    /**
     * Gets the nickname of the target player
     * @return the nickname of the target player
     */
    public String getTargetPlayer() {
        return targetPlayer;
    }
}
