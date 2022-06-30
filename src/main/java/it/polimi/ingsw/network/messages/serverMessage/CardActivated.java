package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.characterCards.CharacterName;

/**
 * Message sent by the client to notify the activation of one character card
 */
public class CardActivated implements UpdateMessage{
    private final String player;
    private final CharacterName name;

    /**
     * Constructor
     * @param player the nickname of the player that activated the character card
     * @param name the name of the character card
     */
    public CardActivated(String player, CharacterName name) {
        this.player = player;
        this.name = name;
    }

    /**
     * Gets the nickname of the player that activated the character card
     * @return the nickname of the player that activated the character card
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Gets the name of the character card
     * @return the name of the character card
     */
    public CharacterName getName() {
        return name;
    }
}
