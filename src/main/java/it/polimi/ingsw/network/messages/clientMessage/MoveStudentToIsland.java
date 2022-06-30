package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

/**
 * Message sent by the client to move one student to an island
 */
public class MoveStudentToIsland extends ActionPhaseMessage{
    private final int islandPosition;
    private final Color student;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param islandPosition the island position
     * @param student the color of the student
     */
    public MoveStudentToIsland(String nickname, int islandPosition, Color student) {
        super(nickname);
        this.islandPosition = islandPosition;
        this.student = student;
    }

    /**
     * Gets the island position
     * @return the island position
     */
    public int getIslandPosition() {
        return islandPosition;
    }

    /**
     * Gets the color of the student
     * @return the color of the student
     */
    public Color getStudent() {
        return student;
    }
}
