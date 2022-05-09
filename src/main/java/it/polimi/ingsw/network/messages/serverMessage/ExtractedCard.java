package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.List;

public class ExtractedCard implements UpdateMessage{
    private CharacterName name;
    private int coinRequired;
    private CharacterCardType type;
    private List<Color> students;

    public ExtractedCard(CharacterName name, int coinRequired, CharacterCardType type, List<Color> students) {
        this.name = name;
        this.coinRequired = coinRequired;
        this.type = type;
        this.students = students;
    }

    public CharacterName getName() {
        return name;
    }

    public int getCoinRequired() {
        return coinRequired;
    }

    public CharacterCardType getType() {
        return type;
    }

    public List<Color> getStudents() {
        return students;
    }
}
