package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class IslandView represents the island client side
 */
public class IslandView {
    private List<Color> students;
    private String owner;
    private int numberOfTower;
    private int banCards;

    /**
     * Constructor
     */
    public IslandView() {
        this.students = new ArrayList<>();
        this.owner = null;
        this.numberOfTower = 0;
        this.banCards = 0;
    }

    /**
     * Sets the island owner
     * @param owner the owner of the island
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Adds students to the islands
     * @param students the students to add to the island
     */
    public void addStudents(List<Color> students) {
        this.students.addAll(students);
    }

    /**
     * Removes students from the island
     * @param student the student to remove
     */
    public void removeStudent(Color student) {
        students.remove(student);
    }

    /**
     * Adds towers to the island
     * @param towersToAdd the number of towers to add
     */
    public void addTowers(int towersToAdd) {
        this.numberOfTower = numberOfTower + towersToAdd;
    }

    /**
     * Adds ban cards to the islands
     * @param banCardsToAdd the ban cards to add to the island
     */
    public void addBanCards(int banCardsToAdd) {
        banCards = banCards + banCardsToAdd;
    }

    /**
     * Removes ban cards to the island
     * @param banCardsToRemove the ban cards to remove from the island
     */
    public void removeBanCards(int banCardsToRemove) {
        banCards = banCards - banCardsToRemove;
    }

    /**
     * Gets the student from the island
     * @return the student from the island
     */
    public List<Color> getStudents() {
        return students;
    }

    /**
     * Gets the owner of the island
     * @return the owner of the island
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the number of tower on the island
     * @return the number of tower on the island
     */
    public int getNumberOfTower() {
        return numberOfTower;
    }

    /**
     * Gets the number of ban cards on the island
     * @return the number of ban cards on the island
     */
    public int getBanCards() {
        return banCards;
    }
}
