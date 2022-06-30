package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

import java.util.List;

/**
 * Message sent by the client to choose some students from the entrance
 */
public class SelectedStudentsFromEntrance extends CharacterCardMessage {
    private final List<Color> students;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param students the students chosen from the entrance
     */
    public SelectedStudentsFromEntrance(String nickname, List<Color> students) {
        super(nickname);
        this.students = students;
    }

    /**
     * Gets the students chosen from the entrance
     * @return the students chosen from the entrance
     */
    public List<Color> getStudents() {
        return students;
    }
}
