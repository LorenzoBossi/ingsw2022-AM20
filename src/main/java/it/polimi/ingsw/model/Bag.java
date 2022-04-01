package it.polimi.ingsw.model;

import java.util.*;

public class Bag {
    List<Color> students;

    /**
     * Constructor
     */
    public Bag(){
        students = new ArrayList<Color>();
    }


    /**
     * Method fillBag fills the bag with students of different color
     *
     * @param numberOfStudentsPerColor number of students of each color that I want to add
     */
    public void fillBag(int numberOfStudentsPerColor){
        for(Color c : Color.values()){
            for(int i = 0; i < numberOfStudentsPerColor; i++)
                students.add(c);
        }
    }


    /**
     * Method getStudents takes randomly students from the Bag and removes them
     * @param numberOfStudents the number of students that I want to take from the bag
     * @return the students taken from the bag
     */
    public List<Color> getStudents(int numberOfStudents){
        Random rand = new Random();
        int rnd_int;
        List<Color> wantedStudents = new ArrayList<Color>();
        for(int i = 0; i < numberOfStudents; i++){
            rnd_int = rand.nextInt(students.size());
            wantedStudents.add(students.get(rnd_int));
            students.remove(rnd_int);
        }
        return wantedStudents;
    }


    /**
     * Method addStudents puts the selected students in the bag
     * @param studentsToAdd the students that I want to put in the bag
     */
    public void addStudents(List<Color> studentsToAdd){
        students.addAll(studentsToAdd);
    }


    /**
     * Method isEmpty checks if the bag is empty
     * @return {@code true} if the bag is empty,
     *         {@code false} if the bag is not empty
     */
    public boolean isEmpty(){
        return students.size() == 0;
    }
}
