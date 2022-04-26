package it.polimi.ingsw.model;
import java.util.*;

public class Island {
    private final int COLOR_NUMBER = 5;
    private List<Integer> students;
    private Player owner;
    private int numberOfTowers;
    private int banCards;


    /**
     * Constructor
     */
    public Island() {
        students = new ArrayList<Integer>(Collections.nCopies(COLOR_NUMBER, 0));
        owner = null;
        numberOfTowers = 0;
        banCards = 0;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }


    /**
     * Method removeTowers remove all the towers on the island
     */
    public void removeTowers(){
        this.numberOfTowers = 0;
    }

    public int getBanCards() {
        return banCards;
    }

    public void setBanCards(int banCards) {
        this.banCards = banCards;
    }


    /**
     * Method addBanCard adds a banCard on the Island
     */
    public void addBanCard() {
        banCards = banCards + 1;
    }


    /**
     * Method removeBanCard removes a banCard on the Island
     */
    public void removeBanCard() {
        this.banCards = banCards - 1;
    }


    /**
     * Method isBanCardPresent checks if there are some banCard on the Island
     *
     * @return {@code true} if there are some banCard on the island,
     *         {@code false} if there aren't banCard on the island
     */
    public Boolean isBanCardPresent(){
        if(banCards == 0)
            return false;
        return true;
    }


    /**
     * Method addStudents adds students of the selected Color
     *
     * @param color the color of the students that I want to add
     * @param studentsNumber the number of the students that I want to add
     */
    public void addStudents (Color color, int studentsNumber) {
        students.set(color.ordinal(), students.get(color.ordinal()) + studentsNumber) ;
    }


    /**
     * Method addStudent add a students of the selected Color
     * @param student the color of the student that I want to add
     */
    public void addStudent(Color student) {
        students.set(student.ordinal(), students.get(student.ordinal()) + 1) ;
    }


    /**
     * Returns the number of students of the chosen color
     *
     * @param color the color of the students of which I want to know the number
     * @return the number of students of the chosen color
     */
    public int getSelectedStudents(Color color){
        return students.get(color.ordinal());
    }


    /**
     * Checks if the given island has the same owner
     *
     * @param isl island to be checked
     * @return {@code true} if they have the same owner,
     *         {@code false} if they have different owner or the island checked has no owner
     */
    public boolean hasSameOwner(Island isl){
        if(isl.getOwner() == null)
            return false;
        return owner.equals(isl.getOwner());
    }

}
