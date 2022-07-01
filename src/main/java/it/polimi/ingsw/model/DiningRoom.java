package it.polimi.ingsw.model;

import java.util.*;

/**
 * Represents the dining room of a player.
 */
public class DiningRoom {

    private List<Integer> students;

    public DiningRoom() {
        this.students = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
    }

    /**
     * Method addStudent add a student on the dining room
     *
     * @param color select the table where the student will be placed
     */
    public void addStudent(Color color) {
        int indexColor = color.ordinal();
        students.set(indexColor, students.get(indexColor) + 1);
    }

    /**
     * Method addStudent add a student on the dining room
     *
     * @param indexColor select the table where the student will be placed
     */
    public void addStudent(int indexColor) {
        students.set(indexColor, students.get(indexColor) + 1);
    }

    /**
     * Method refillDining add students to the dining room from a list of color
     *
     * @param students list of color
     */
    public void refillDining(List<Color> students) {
        while (!students.isEmpty()) {
            addStudent(students.remove(0));
        }
    }

    /**
     * Method getNumberOfStudent given a color returns the number of students of that color
     *
     * @param color chosen color
     * @return number of students
     */
    public int getNumberOfStudent(Color color) {
        int indexColor = color.ordinal();
        return this.students.get(indexColor);
    }

    /**
     * Returns the number of students of the color corresponding to the given index in the entrance
     * @param index integer corresponding to a color
     * @return the number of students of the specified color in the entrance
     */
    public int getNumberOfStudent(int index) {
        return this.students.get(index);
    }

    /**
     * Method isAddCoin checks if the position on the dining room gives a coin to the player
     *
     * @param color the student's color that I want to check
     * @return {@code true} if is a coin position,
     * {@code false} if is not a coin position
     */
    public boolean isAddCoin(Color color) {
        int numOfStudents = getNumberOfStudent(color);
        return numOfStudents % 3 == 0;
    }


    /**
     * removes a student of the specified color if present in the dining room
     * If there are not students of the specified color the dining room doesn't change
     */
    public void removeStudent(Color color) {
        int index = color.ordinal();
        if (students.get(index) > 0) {
            students.set(index, students.get(index) - 1);
        }
    }

    /**
     * Method isPresent notifies if there is at least one student of that color in the diningRoom
     *
     * @param student specify the chosen color
     * @return {@code true} if there is at least one,
     * {@code false} else
     */
    public boolean isPresent(Color student) {
        int indexColor = student.ordinal();
        return students.get(indexColor) >= 1;
    }

    /**
     * Method isPresent notifies if there are at least the student in the input list in the diningRoom
     *
     * @param student specify the input list of color
     * @return {@code true} if there are,
     * {@code false} else
     */
    public boolean isPresent(List<Color> student) {
        List<Integer> stud = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
        int index = 0;

        for (Color color : student)
            stud.set(color.ordinal(), stud.get(color.ordinal()) + 1);

        for (int intcolor : stud) {
            if (this.students.get(index) < intcolor)
                return false;
            index += 1;
        }

        return true;

    }

    /**
     * Getter of the students in the entrance
     * @return a list of Integer which represents the number of students of each color
     */
    public List<Integer> getStudents() {
        return students;
    }
}