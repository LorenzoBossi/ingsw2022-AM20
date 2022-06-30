package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.characterCards.CharacterName;

/**
 * Message to activate one character card
 */
public class ActiveEffect extends CharacterCardMessage{
    private final CharacterName name;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param name the name of the character card
     */
    public ActiveEffect(String nickname, CharacterName name) {
        super(nickname);
        this.name = name;
    }

    /**
     * Gets the name of the character card
     * @return the name of the character card
     */
    public CharacterName getName() {
        return name;
    }
}
