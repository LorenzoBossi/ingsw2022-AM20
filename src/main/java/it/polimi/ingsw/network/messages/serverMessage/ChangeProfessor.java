package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;

public class ChangeProfessor implements UpdateMessage{
    private String newOwner;
    private String oldOwner;
    private Color professorColor;

    public ChangeProfessor(String newOwner, String oldOwner, Color professorColor) {
        this.newOwner = newOwner;
        this.oldOwner = oldOwner;
        this.professorColor = professorColor;
    }

    public String getNewOwner() {
        return newOwner;
    }

    public String getOldOwner() {
        return oldOwner;
    }

    public Color getProfessorColor() {
        return professorColor;
    }
}
