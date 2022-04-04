package it.polimi.ingsw.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


public class EntranceTest {
    private Entrance entrance = new Entrance();


    @Test
    public void fromCloud(){
        Cloud cloud = new Cloud();
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.RED,Color.GREEN,Color.YELLOW));
        assertTrue(cloud.isEmpty());
        cloud.fill(list);
        assertFalse(cloud.isEmpty());
        entrance.addStudentFromCloud(cloud);
        assertEquals(Arrays.asList(2,0,1,1,0) , entrance.getStudents());
        assertTrue(cloud.isEmpty());
    }

    @Test
    public void refill(){
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.RED,Color.GREEN,Color.YELLOW));
        entrance.refillEntrance(list);
        assertEquals(Arrays.asList(2,0,1,1,0) , entrance.getStudents());
        assertTrue(list.isEmpty());
        List<Color> list2 = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.BLUE));
        entrance.refillEntrance(list2);
        assertEquals(Arrays.asList(3,1,1,1,0) , entrance.getStudents());
        assertTrue(list2.isEmpty());
    }
    @Test
    public void toIsland(){
        Island island = new Island();
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.RED,Color.GREEN,Color.YELLOW));
        entrance.refillEntrance(list);
        assertEquals(Arrays.asList(2,0,1,1,0) , entrance.getStudents());
        entrance.moveStudentToIsland(island,Color.YELLOW);
        assertEquals(Arrays.asList(1,0,1,1,0) , entrance.getStudents());
        assertEquals(1 , island.getSelectedStudents(Color.YELLOW));
        assertEquals(0 , island.getSelectedStudents(Color.RED));
        entrance.moveStudentToIsland(island, Color.RED);
        assertEquals(Arrays.asList(1,0,1,0,0) , entrance.getStudents());
        assertEquals(1 , island.getSelectedStudents(Color.YELLOW));
        assertEquals(1 , island.getSelectedStudents(Color.RED));
        entrance.moveStudentToIsland(island, Color.RED);
        assertEquals(Arrays.asList(1,0,1,0,0) , entrance.getStudents());
        assertEquals(1 , island.getSelectedStudents(Color.YELLOW));
        assertEquals(0 , island.getSelectedStudents(Color.BLUE));
        assertEquals(0 , island.getSelectedStudents(Color.GREEN));
        assertEquals(1 , island.getSelectedStudents(Color.RED));
        assertEquals(0 , island.getSelectedStudents(Color.PINK));
    }


}