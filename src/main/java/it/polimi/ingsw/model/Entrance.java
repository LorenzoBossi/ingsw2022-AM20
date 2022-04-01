package it.polimi.ingsw.model;
import java.util.*;

public class Entrance {

    private List<Integer> students;

    public Entrance (){
        this.students = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    }

    public void removeStudent(Color student){
        int indexColor = student.ordinal();
        students.set(indexColor, students.get(indexColor) - 1);
    }

    public int getNumberOfStudent(Color color){
        int indexColor = color.ordinal();
        return this.students.get(indexColor);
    }

    public void addStudentFromCloud(Cloud cloud){
        List<Color> student = cloud.getStudents();
        int index;
        Color color;
        while(!student.isEmpty()){
            color = student.remove(0);
            index = color.ordinal();
            students.set(index,students.get(index) +1);
        }

    }

    public void addStudent(Color stud){
        int indexColor = stud.ordinal();
        students.set(indexColor,students.get(indexColor) +1);
    }

    public void moveStudentToIsland(Island selectedIsland, Color selectedColor){
        if(isPresent(selectedColor)) {
            this.removeStudent(selectedColor);
            selectedIsland.addStudent(selectedColor);
        }
    }

    public boolean isPresent(Color cstudent){
        int indexColor = cstudent.ordinal();
        return students.get(indexColor) >= 1;
    }

    public void refillEntrance(List<Color> student){
        while(!student.isEmpty()){
            addStudent(student.remove(0));
        }
    }

    public List<Integer> getStudents() {
        return students;
    }

}
