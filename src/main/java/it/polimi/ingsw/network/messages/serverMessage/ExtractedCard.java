package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.List;

/**
 * Message sent by the server to notify the character card extracted
 */
public class ExtractedCard implements UpdateMessage{
    private final CharacterName name;
    private final int coinRequired;
    private final CharacterCardType type;
    private final List<Color> students;

    /**
     * Constructor
     * @param name the name of the character card extracted
     * @param coinRequired the coins required to activate the character card
     * @param students the students on the character card
     * @param type the type of the character card
     */
    public ExtractedCard(CharacterName name, int coinRequired, CharacterCardType type, List<Color> students) {
        this.name = name;
        this.coinRequired = coinRequired;
        this.type = type;
        this.students = students;
    }

    /**
     * Gets the name of the character card extracted
     * @return the name of the character card extracted
     */
    public CharacterName getName() {
        return name;
    }

    /**
     * Gets the coins required to activate the character card
     * @return the coins required to activate the character card
     */
    public int getCoinRequired() {
        return coinRequired;
    }

    /**
     * Gets the type of the character card
     * @return the type of the character card
     */
    public CharacterCardType getType() {
        return type;
    }

    /**
     * Gets the students on the character card
     * @return the students on the character card
     */
    public List<Color> getStudents() {
        return students;
    }
}
