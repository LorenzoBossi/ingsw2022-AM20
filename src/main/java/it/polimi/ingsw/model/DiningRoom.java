package it.polimi.ingsw.model;
import java.util.*;

public class DiningRoom {

    private List<Integer> students;

    public DiningRoom(){
        this.students = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    }

    public void addStudent(Color color){
        int indexColor = color.ordinal();
        students.set(indexColor,students.get(indexColor) + 1);
        if(isAddCoin(students.get(indexColor) + 1))
            System.out.println("Coin");
    }
    public void addStudent(int indexColor){
        students.set(indexColor,students.get(indexColor) + 1 );
        if(isAddCoin(students.get(indexColor) + 1 ))
            System.out.println("Coin");
    }
    public int getNumberOfStudent(Color color){
        int indexColor = color.ordinal();
        return this.students.get(indexColor);
    }
    public int getNumberOfStudent(int index){
        return this.students.get(index);
    }

    private boolean isAddCoin(int numOfStudents){
        return numOfStudents % 3 == 0;
    }


    /**
     * removes a student of the specified color if present in the dining room
     * If there are not students of the specified color the dining room doesn't change
     */
    public void removeStudent(Color color){
        int index=color.ordinal();
        if(students.get(index)>0) {
            students.set(index,students.get(index)-1);
        }
    }
}
