package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

public class MoveStudentToIsland extends ActionPhaseMessage{
    private int islandPosition;
    private Color student;

    public MoveStudentToIsland(String nickname, int islandPosition, Color student) {
        super(nickname);
        this.islandPosition = islandPosition;
        this.student = student;
    }

    public int getIslandPosition() {
        return islandPosition;
    }

    public Color getStudent() {
        return student;
    }
}
