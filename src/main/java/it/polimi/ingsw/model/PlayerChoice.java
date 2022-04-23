package it.polimi.ingsw.model;

import java.util.List;

public class PlayerChoice {

    private List<Color> selectedStudents;
    private List<Color> selectedStudentFromEntrance;
    private Island selectedIsland;
    private Color selectedColor;


    public List<Color> getSelectedStudents() {
        return selectedStudents;
    }

    public Island getSelectedIsland() {
        return selectedIsland;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public List<Color> getSelectedStudentFromEntrance() {
        return selectedStudentFromEntrance;
    }

    public void selectStudents(List<Color> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public void selectStudentFromEntrance(List<Color> selectedStudentFromEntrance) {
        this.selectedStudentFromEntrance = selectedStudentFromEntrance;
    }

    public void selectIsland(Island island) {
        this.selectedIsland = island;
    }

    public void selectColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

}
