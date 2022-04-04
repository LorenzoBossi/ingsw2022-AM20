package it.polimi.ingsw.model;
import java.util.*;

public class Entrance {

    private List<Integer> students;

    public Entrance (){
        this.students = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    }

    /**
     * Method removeStudent remove a student from the entrance
     * @param student select which color of student need to be removed
     */
    public void removeStudent(Color student){
        int indexColor = student.ordinal();
        students.set(indexColor, students.get(indexColor) - 1);
    }

    /**
     * Method getNumberOfStudent given a color returns the number of students of that color
     * @param color chosen color
     * @return number of students
     */
    public int getNumberOfStudent(Color color){
        int indexColor = color.ordinal();
        return this.students.get(indexColor);
    }

    /**
     * Method addStudentFromCloud add in the entrance a list of students taken from one cloud
     * @param cloud specify from which cloud I'm taking the students
     */
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

    /**
     * Method addStudent add a student on the dining room
     * @param stud select the table where the student will be placed
     */
    public void addStudent(Color stud){
        int indexColor = stud.ordinal();
        students.set(indexColor,students.get(indexColor) +1);
    }

    /**
     * Method moveStudentToIsland moves a student from the entrance to an island
     * @param selectedIsland specify the island
     * @param selectedColor specify which student color to take
     */
    public void moveStudentToIsland(Island selectedIsland, Color selectedColor){
        if(isPresent(selectedColor)) {
            this.removeStudent(selectedColor);
            selectedIsland.addStudent(selectedColor);
        }
    }

    /**
     * Method isPresent notifies if there is at least one student of that color in the entrance
     * @param cstudent specify the chosen color
     * @return {@code true} if there is at least one,
     *         {@code false} else
     */
    public boolean isPresent(Color cstudent){
        int indexColor = cstudent.ordinal();
        return students.get(indexColor) >= 1;
    }

    /**
     * Method refillEntrance add students to the entrance from a list of color
     * @param student list of color
     */
    public void refillEntrance(List<Color> student){
        while(!student.isEmpty()){
            addStudent(student.remove(0));
        }
    }

    public List<Integer> getStudents() {
        return students;
    }

}
