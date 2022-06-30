package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message send by the client to end the action phase
 */
public class EndActionPhase extends ActionPhaseMessage{
    public EndActionPhase(String nickname) {
        super(nickname);
    }
}
