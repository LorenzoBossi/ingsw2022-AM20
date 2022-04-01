package it.polimi.ingsw.model;

import java.util.List;

public class PlayerChoice {

    private List<Color> selectedStudents;
    private List<Color>selectedStudentFromEntrance;
    private int islandPosition;
    private Color selectedColor;


    public List<Color> getSelectedStudents(){
        return selectedStudents;
    }

    public int getIslandSelection(){
        return islandPosition;
    }
    public Color getSelectedColor(){
        return selectedColor;
    }

    public List<Color> getSelectedStudentFromEntrance(){
        return selectedStudentFromEntrance;
    }






    public void selectStudents(List<Color> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public void selectStudentFromEntrance(List<Color> selectedStudentFromEntrance) {
        this.selectedStudentFromEntrance = selectedStudentFromEntrance;
    }

    public void selectIslandPosition(int islandPosition) {
        this.islandPosition = islandPosition;
    }

    public void selectColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

}
