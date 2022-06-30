package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.List;

/**
 * Message sent by the client to select some students from the card
 */
public class SelectedStudentsFromCard extends CharacterCardMessage{
    private final List<Color> students;
    private final CharacterName name;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param students the students chosen by the player
     * @param name the name of the character card
     */
    public SelectedStudentsFromCard(String nickname, List<Color> students, CharacterName name) {
        super(nickname);
        this.students = students;
        this.name = name;
    }

    /**
     * Gets the students chosen by the player
     * @return the name of the character card
     */
    public List<Color> getStudents() {
        return students;
    }

    /**
     * Gets the name of the character card
     * @return the name of the character card
     */
    public CharacterName getName() {
        return name;
    }
}
