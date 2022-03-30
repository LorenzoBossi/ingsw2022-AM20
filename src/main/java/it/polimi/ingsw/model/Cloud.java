package it.polimi.ingsw.model;
import java.util.*;
public class Cloud {

    private boolean chosen;
    private List<Color> students;

    public void fill(List<Color> studentsList) {
        this.students.addAll(studentsList);
    }

    public void setChosen(boolean isChosen){
        chosen=isChosen;
    }

    public boolean isChosen(){
        return chosen;
    }

    public void remove(Color student){
        students.remove(student);
    }

    public boolean isEmpty(){
        return students.isEmpty();
    }

    public List<Color> getStudents() {
        return students;
    }
}
