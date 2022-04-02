package it.polimi.ingsw.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Cloud methods
 */
public class CloudTest {
    Cloud cloud;

    @BeforeEach
    public void init(){
        cloud = new Cloud();
    }

    @Test
    public void fillCloudTest(){
        assertTrue(cloud.isEmpty());

        List<Color> students = new ArrayList<>();
        students.add(Color.RED);
        cloud.fill(students);
        assertEquals(cloud.getStudents(),students);

        students.add(Color.GREEN);
        cloud.fill(students);
        assertNotEquals(cloud.getStudents(),students);

        cloud.removeAll();
        cloud.fill(students);
        assertEquals(cloud.getStudents(),students);
    }

    @Test
    public void chosenTest(){
        assertFalse(cloud.isChosen());

        cloud.setChosen(true);
        assertTrue(cloud.isChosen());

        cloud.setChosen(false);
        assertFalse(cloud.isChosen());

    }

    @Test
    public void isEmptyTest(){
        assertTrue(cloud.isEmpty());

        List<Color> students = new ArrayList<>();
        students.add(Color.BLUE);
        students.add(Color.PINK);
        students.add(Color.YELLOW);

        cloud.fill(students);
        assertFalse(cloud.isEmpty());

        cloud.remove(Color.YELLOW);
        assertFalse(cloud.isEmpty());

        cloud.removeAll();
        assertTrue(cloud.isEmpty());
    }

}
