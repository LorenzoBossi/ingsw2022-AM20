package it.polimi.ingsw.model;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class PlayerBoard {

    private Entrance entrance;
    private DiningRoom diningRoom;
    private int numberTower;
    private List<Boolean> professor;
    private EndGameObserver endGameObserver;

    public PlayerBoard(){
        this.entrance = new Entrance();
        this.diningRoom = new DiningRoom();
        this.numberTower = 0;
        this.professor = new ArrayList<>(Arrays.asList(FALSE,FALSE,FALSE,FALSE, FALSE));

    }

    public Entrance getEntrance(){
        return this.entrance;
    }

    public DiningRoom getDiningRoom(){
        return this.diningRoom;
    }

    public int getNumberTower() { return this.numberTower; }

    /**
     * AddTowers add towers to the playerBoard
     * @param towersAdd number of towers to add at once
     */
    public void addTowers(int towersAdd){
        if(this.numberTower + towersAdd <=8)
            this.numberTower = this.numberTower + towersAdd;
    }

    /**
     * RemoveTowers remove towers from the playerBoard.
     * If the number of towers to remove is higher than the number of tower on the playerboard
     * it removes every tower on the playerboard
     * @param towersRemove number of towers to remove at once
     */
    public void removeTowers(int towersRemove){
        if(this.numberTower > towersRemove)
            this.numberTower = this.numberTower - towersRemove;
        else {
            this.numberTower=0;
        }
        if(numberTower==0){
            endGameObserver.notifyEndGame();
        }
    }

    /**
     * Method changeProfessor changes the boolean for the professor's presence
     * @param color chosen color of professor to change
     */
    public void changeProfessor(Color color){
        int indexColor = color.ordinal();
        if(professor.get(indexColor) == FALSE)
            professor.set(indexColor, TRUE);
        else
            professor.set(indexColor, FALSE);
    }

    /**
     * Method getProfessor return true if there is the professor of the chosen color on the playerBoard
     * @param color chosen color
     * @return true if there is, false else
     */
    public Boolean getProfessor(Color color){
        int indexColor = color.ordinal();
        return professor.get(indexColor);
    }

    /**
     * Method moveStudentFromEntranceToDiningRoom moves a selected student from the entrance to the diningRoom
     * @param selectedStudent chosen color of student to move.
     */
    public void moveStudentFromEntranceToDiningRoom(Color selectedStudent){
        if(entrance.isPresent(selectedStudent)) {
            entrance.removeStudent(selectedStudent);
            diningRoom.addStudent(selectedStudent);
        }
    }

    public void attach(EndGameObserver endGameObserver){
        this.endGameObserver=endGameObserver;
    }

}
