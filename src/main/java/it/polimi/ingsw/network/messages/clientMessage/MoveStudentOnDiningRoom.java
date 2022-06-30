package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

/**
 * Message sent by the client to move one student from the entrance to the dining room
 */
public class MoveStudentOnDiningRoom extends ActionPhaseMessage {
    private final Color student;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param student the color of the student
     */
    public MoveStudentOnDiningRoom(String nickname, Color student) {
        super(nickname);
        this.student = student;
    }

    /**
     * Gets the color of the student
     * @return the color of the student
     */
    public Color getStudent() {
        return student;
    }
}
