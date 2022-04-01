package it.polimi.ingsw.model;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class PlayerBoard {

    private Entrance entrance;
    private DiningRoom diningRoom;
    private int numberTower;
    private List<Boolean> professor;

    public PlayerBoard(){
        this.entrance = new Entrance();
        this.diningRoom = new DiningRoom();
        this.numberTower = 8;
        this.professor = new ArrayList<>(Arrays.asList(FALSE,FALSE,FALSE,FALSE, FALSE));

    }

    public Entrance getEntrance(){
        return this.entrance;
    }

    public DiningRoom getDiningRoom(){
        return this.diningRoom;
    }

    public int getNumberTower() { return this.numberTower; }

    public void addTowers(int towersAdd){
        this.numberTower = this.numberTower + towersAdd;
    }

    public void removeTowers(int towersRemove){
        if(this.numberTower - towersRemove >= 0)
            this.numberTower = this.numberTower - towersRemove;
        /*else
            System.out.println("errore");*/
    }
    public void changeProfessor(Color color){
        int indexColor = color.ordinal();
        if(professor.get(indexColor) == FALSE)
            professor.set(indexColor,TRUE);
        else
            professor.set(indexColor, FALSE);
    }
    public Boolean getProfessor(Color color){
        int indexColor = color.ordinal();
        return professor.get(indexColor);
    }
    public void moveStudentFromEntranceToDiningRoom(Color selectedStudent){
        if(entrance.isPresent(selectedStudent)) {
            entrance.removeStudent(selectedStudent);
            diningRoom.addStudent(selectedStudent);
        }
    }


}
