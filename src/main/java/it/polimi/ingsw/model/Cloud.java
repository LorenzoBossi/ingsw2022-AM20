package it.polimi.ingsw.model;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.*;
public class Cloud extends ObservableSubject {

    private boolean chosen;
    private List<Color> students;


    public Cloud(){
        chosen=false;
        students=new ArrayList<>();
    }


    /**
     * adds students on the cloud, without removing any present students from the cloud
     * @param studentsList specify which students to put on the cloud
     */
    public void fill(List<Color> studentsList) {
        this.students.addAll(studentsList);
        //notifyObserver(new AddStudentsOnCloud());
    }


    /**
     * Sets the state of the cloud to already chosen or not already chosen during the current turn
     * @param isChosen
     *                {@code true} sets the cloud to already chosen
     *                {@code false} sets the cloud to not already chosen
     */
    public void setChosen(boolean isChosen){
        chosen=isChosen;
    }

    /**
     * tells if the cloud has been already chosen
     */
    public boolean isChosen(){
        return chosen;
    }

    /**
     * removes a student of the specified color
     * @param student color of the student to remove
     */
    public void remove(Color student){
        students.remove(student);
    }

    /**
     * removes every student on the cloud
     */
    public void removeAll(){
        students.clear();
    }

    /**
     * tells if the cloud is empty
     */
    public boolean isEmpty(){
        return students.isEmpty();
    }

    /**
     * gets the students present on the cloud
     */
    public List<Color> getStudents() {
        return students;
    }
}
