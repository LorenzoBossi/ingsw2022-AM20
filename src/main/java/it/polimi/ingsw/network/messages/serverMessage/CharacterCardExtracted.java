package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.List;

public class CharacterCardExtracted implements UpdateMessage {
    private CharacterName name;
    private int coinRequired;
    private List<Color> students;
    private CharacterCardType cardType;

    public CharacterCardExtracted(CharacterName name, int coinRequired, List<Color> students, CharacterCardType cardType) {
        this.name = name;
        this.coinRequired = coinRequired;
        this.students = students;
        this.cardType = cardType;
    }

    public CharacterName getName() {
        return name;
    }

    public int getCoinRequired() {
        return coinRequired;
    }

    public List<Color> getStudents() {
        return students;
    }

    public CharacterCardType getCardType() {
        return cardType;
    }
}
