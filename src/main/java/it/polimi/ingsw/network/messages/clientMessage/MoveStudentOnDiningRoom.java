package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

public class MoveStudentOnDiningRoom extends ActionPhaseMessage {
    private Color student;

    public MoveStudentOnDiningRoom(String nickname, Color student) {
        super(nickname);
        this.student = student;
    }

    public Color getStudent() {
        return student;
    }
}
