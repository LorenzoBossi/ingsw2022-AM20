package it.polimi.ingsw.model;

import java.util.*;

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
        //if (isAddCoin(students.get(indexColor) + 1))
            //System.out.println("Coin");
    }

    //Chiedere se serve effetivamente
    /**
     * Method addStudent add a student on the dining room
     *
     * @param indexColor select the table where the student will be placed
     */
    public void addStudent(int indexColor) {
        students.set(indexColor, students.get(indexColor) + 1);
        //if (isAddCoin(students.get(indexColor) + 1))
            //System.out.println("Coin");
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
}
