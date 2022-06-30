package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;

/**
 * Message send by the server to notify the change of a professor
 */
public class ChangeProfessor implements UpdateMessage{
    private final String newOwner;
    private final String oldOwner;
    private final Color professorColor;

    /**
     * Constructor
     * @param newOwner the new owner
     * @param oldOwner the old owner
     * @param professorColor the color of the professor
     */
    public ChangeProfessor(String newOwner, String oldOwner, Color professorColor) {
        this.newOwner = newOwner;
        this.oldOwner = oldOwner;
        this.professorColor = professorColor;
    }

    /**
     * Gets the new owner
     * @return the new owner
     */
    public String getNewOwner() {
        return newOwner;
    }

    /**
     * Gets the old owner
     * @return the old owner
     */
    public String getOldOwner() {
        return oldOwner;
    }

    /**
     * Gets the professor color
     * @return the professor color
     */
    public Color getProfessorColor() {
        return professorColor;
    }
}
