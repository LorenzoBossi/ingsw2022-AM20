package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message used by the client in order to activate a character card
 */
public class CharacterCardMessage extends ActionPhaseMessage{
    public CharacterCardMessage(String nickname) {
        super(nickname);
    }
}
