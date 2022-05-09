package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.characterCards.CharacterName;

public class CardActivated implements UpdateMessage{
    String player;
    CharacterName name;

    public CardActivated(String player, CharacterName name) {
        this.player = player;
        this.name = name;
    }

    public String getPlayer() {
        return player;
    }

    public CharacterName getName() {
        return name;
    }
}
