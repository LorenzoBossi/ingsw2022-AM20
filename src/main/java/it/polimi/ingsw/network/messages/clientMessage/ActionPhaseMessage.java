package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Action phase message is sent by the client to notify an action during the action phase
 */
public class ActionPhaseMessage extends GameMessage{
    public ActionPhaseMessage(String nickname) {
        super(nickname);
    }
}
