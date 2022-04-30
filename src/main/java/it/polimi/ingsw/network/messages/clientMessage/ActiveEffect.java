package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.characterCards.CharacterName;

public class ActiveEffect extends CharacterCardMessage{
    private CharacterName name;

    public ActiveEffect(String nickname, CharacterName name) {
        super(nickname);
        this.name = name;
    }

    public CharacterName getName() {
        return name;
    }
}
