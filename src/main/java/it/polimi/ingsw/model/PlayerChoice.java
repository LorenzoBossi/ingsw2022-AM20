package it.polimi.ingsw.model;

import java.util.List;

/**
 * Manages all the choices made by a player regarding the activation of character cards
 */
public class PlayerChoice {

    private List<Color> selectedStudents;
    private List<Color> selectedStudentFromEntrance;
    private Island selectedIsland;
    private Color selectedColor;

    /**
     * Gets the last students selection.
     * @return The list of the color of the selected students
     */
    public List<Color> getSelectedStudents() {
        return selectedStudents;
    }

    /**
     * Gets the selected island
     * @return the selected island
     */
    public Island getSelectedIsland() {
        return selectedIsland;
    }

    /**
     * Gets the selected color of students
     * @return the selected color
     */
    public Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * Gets the students selected from the player entrance
     * @return The list of the color of the selected students from the entrance
     */
    public List<Color> getSelectedStudentFromEntrance() {
        return selectedStudentFromEntrance;
    }

    /**
     * Selects a generic list of students
     * @param selectedStudents list of students to select
     */
    public void selectStudents(List<Color> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    /**
     * Selects students from the entrance of the player
     * @param selectedStudentFromEntrance list of students to select
     */
    public void selectStudentFromEntrance(List<Color> selectedStudentFromEntrance) {
        this.selectedStudentFromEntrance = selectedStudentFromEntrance;
    }

    /**
     * Select an island
     * @param island the island to select
     */
    public void selectIsland(Island island) {
        this.selectedIsland = island;
    }

    /**
     * Select a color of students
     * @param selectedColor the color to select
     */
    public void selectColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

}
