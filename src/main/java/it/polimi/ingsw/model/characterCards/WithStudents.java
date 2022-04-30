package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.List;

public abstract class WithStudents extends CharacterCard {
    //protected int numberOfStudents;
    //protected int maxSelection;
    private List<Color> students;

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public WithStudents(CharacterName name, int coinsRequired) {
        super(name, coinsRequired);
    }

    /*
    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public int getMaxSelection() {
        return maxSelection;
    }
    */

    @Override
    public List<Color> getStudents() {
        return students;
    }


    public void setStudents(List<Color> students) {
        this.students = students;
        //notifyObserver(new MoveStudents("BAG", "CARD", students, null, getName().name()));
    }


    public void remove(List<Color> students) {
        for (Color student : students) {
            this.students.remove(student);
        }
    }


   /*
    public void remove(Color student) {
        students.remove(student);
    }
    */

    public void add(List<Color> students) {
        this.students.addAll(students);
    }

}
