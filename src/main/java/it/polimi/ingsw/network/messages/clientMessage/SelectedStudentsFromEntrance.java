package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;

import java.util.List;

public class SelectedStudentsFromEntrance extends CharacterCardMessage {
    private List<Color> students;

    public SelectedStudentsFromEntrance(String nickname, List<Color> students) {
        super(nickname);
        this.students = students;
    }

    public List<Color> getStudents() {
        return students;
    }
}
