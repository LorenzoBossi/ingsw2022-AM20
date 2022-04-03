package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Color;

import java.util.List;

public abstract class WithStudents extends CharacterCard {
    private int numberOfStudents;
    private int maxSelection;
    private List<Color> students;

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public WithStudents(int coinsRequired) {
        super(coinsRequired);
    }


    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    @Override
    public List<Color> getStudents() {
        return students;
    }

    protected void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    protected void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    public void setStudents(List<Color> students) {
        this.students = students;
    }
    public void remove(List<Color> students){
        this.students.removeAll(students);
    }
    public void add(List<Color> students){
        this.students.addAll(students);
    }
}
