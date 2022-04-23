package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.characterCards.CharacterName;

public class IncreaseCardPrice implements UpdateMessage {
    CharacterName characterName;

    public IncreaseCardPrice(CharacterName characterName) {
        this.characterName= characterName;
    }

    public CharacterName getCharacterName() {
        return characterName;
    }
}
