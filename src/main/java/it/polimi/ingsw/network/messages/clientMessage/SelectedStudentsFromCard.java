package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.List;

public class SelectedStudentsFromCard extends CharacterCardMessage{
    private List<Color> students;
    private CharacterName name;

    public SelectedStudentsFromCard(String nickname, List<Color> students, CharacterName name) {
        super(nickname);
        this.students = students;
        this.name = name;
    }

    public List<Color> getStudents() {
        return students;
    }

    public CharacterName getName() {
        return name;
    }
}
