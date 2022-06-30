package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.characterCards.CharacterName;

/**
 * Message sent by the server to notify an increase of the character card cost
 */
public class IncreaseCardPrice implements UpdateMessage {
    private final CharacterName characterName;

    /**
     * Constructor
     * @param characterName the name of the character card
     */
    public IncreaseCardPrice(CharacterName characterName) {
        this.characterName= characterName;
    }

    /**
     * Gets the character card name
     * @return the character card name
     */
    public CharacterName getCharacterName() {
        return characterName;
    }
}
