package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class which represent the basic structure of every card containing students
 */
public abstract class WithStudents extends CharacterCard {


    private final int MAX_SELECTION;

    private List<Color> students;

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public WithStudents(CharacterName name, int coinsRequired, CharacterCardType type, int MAX_SELECTION) {
        super(name, coinsRequired, type);
        this.MAX_SELECTION = MAX_SELECTION;
    }

    /**
     * Gets the list of students on the card
     * @return The list of colors of the students on the card
     */
    @Override
    public List<Color> getStudents() {
        return students;
    }

    /**
     * Sets the students on the card
     * @param students a list of color to set as students on the card
     */
    public void setStudents(List<Color> students) {
        this.students = students;
        //notifyObserver(new MoveStudents("BAG", "CARD", students, null, getName().name()));
    }

    /**
     * Removes from the card the specified students
     * @param students list of colors of students to remove
     */
    public void remove(List<Color> students) {
        for (Color student : students) {
            this.students.remove(student);
        }
    }


    /**
     * Adds on the card the specified students
     * @param students list of colors of students to add
     */
    public void add(List<Color> students) {
        this.students.addAll(students);
    }

    /**
     * checks if every student in the list has a match with a student on the card
     *
     * @param students the list of students to check
     * @return true if very student in the list has a match with a student on the card,
     * false otherwise
     */
    public boolean containsEveryStudent(List<Color> students) {
        List<Color> list = new ArrayList<>(this.students);
        for (Color color : students) {
            if (!list.contains(color)) {
                return false;
            }
            list.remove(color);
        }
        return true;
    }


    /**
     * checks if the card contains at least the max selection number of students,
     * if the player selected the right number of students and if those students are on the card
     *
     * @param player the current player of the game
     * @return true if the requirements are satisfied
     * false otherwise
     */
    @Override
    public boolean checkRequirements(Player player) {
        List<Color> selectedStudents = player.getPlayerChoice().getSelectedStudents();

        if (getStudents().size() < MAX_SELECTION)
            return false;
        if (selectedStudents.size() > MAX_SELECTION || selectedStudents.size() <= 0)
            return false;
        if (!containsEveryStudent(selectedStudents))
            return false;
        return true;
    }
}
