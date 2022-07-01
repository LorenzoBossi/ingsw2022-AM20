package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * This class tests the Entrance methods
 */
public class EntranceTest {
    private Entrance entrance = new Entrance();


    @Test
    public void fromCloud() {
        Cloud cloud = new Cloud();
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.RED, Color.GREEN, Color.YELLOW));
        assertTrue(cloud.isEmpty());
        cloud.fill(list);
        assertFalse(cloud.isEmpty());
        entrance.addStudentFromCloud(cloud);
        assertEquals(Arrays.asList(2, 0, 1, 1, 0), entrance.getStudents());
        assertTrue(cloud.isEmpty());
    }

    @Test
    public void refill() {
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.RED, Color.GREEN, Color.YELLOW));
        entrance.refillEntrance(list);
        assertEquals(Arrays.asList(2, 0, 1, 1, 0), entrance.getStudents());
        assertTrue(list.isEmpty());
        List<Color> list2 = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.BLUE));
        entrance.refillEntrance(list2);
        assertEquals(Arrays.asList(3, 1, 1, 1, 0), entrance.getStudents());
        assertTrue(list2.isEmpty());
    }

    @Test
    public void toIsland() {
        Island island = new Island();
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.RED, Color.GREEN, Color.YELLOW));
        entrance.refillEntrance(list);
        assertEquals(Arrays.asList(2, 0, 1, 1, 0), entrance.getStudents());
        entrance.moveStudentToIsland(island, Color.YELLOW);
        assertEquals(Arrays.asList(1, 0, 1, 1, 0), entrance.getStudents());
        assertEquals(1, island.getSelectedStudents(Color.YELLOW));
        assertEquals(0, island.getSelectedStudents(Color.RED));
        entrance.moveStudentToIsland(island, Color.RED);
        assertEquals(Arrays.asList(1, 0, 1, 0, 0), entrance.getStudents());
        assertEquals(1, island.getSelectedStudents(Color.YELLOW));
        assertEquals(1, island.getSelectedStudents(Color.RED));
        entrance.moveStudentToIsland(island, Color.RED);
        assertEquals(Arrays.asList(1, 0, 1, 0, 0), entrance.getStudents());
        assertEquals(1, island.getSelectedStudents(Color.YELLOW));
        assertEquals(0, island.getSelectedStudents(Color.BLUE));
        assertEquals(0, island.getSelectedStudents(Color.GREEN));
        assertEquals(1, island.getSelectedStudents(Color.RED));
        assertEquals(0, island.getSelectedStudents(Color.PINK));
    }

    @Test
    public void present() {
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE));
        entrance.refillEntrance(list);
        assertEquals(Arrays.asList(2, 1, 1, 1, 0), entrance.getStudents());

        List<Color> list2 = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW, Color.BLUE));
        assertTrue(entrance.isPresent(list2));
        List<Color> list3 = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.RED, Color.GREEN));
        assertTrue(entrance.isPresent(list3));
        List<Color> list4 = new ArrayList<>(Arrays.asList(Color.RED, Color.RED, Color.BLUE));
        assertFalse(entrance.isPresent(list4));
        List<Color> list5 = new ArrayList<>();
        assertTrue(entrance.isPresent(list5));
        List<Color> list6 = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.GREEN));
        assertFalse(entrance.isPresent(list6));
        List<Color> list7 = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.PINK));
        assertFalse(entrance.isPresent(list7));
    }


}