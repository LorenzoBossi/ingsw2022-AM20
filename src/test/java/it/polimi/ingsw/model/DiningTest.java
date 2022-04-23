package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class DiningTest {
    private DiningRoom diningRoom = new DiningRoom();

    @Test
    public void addStudent(){
        int yellow=0,blue=1,green=0,red=1,pink=2;
        diningRoom.addStudent(Color.BLUE);
        diningRoom.addStudent(Color.PINK);
        diningRoom.addStudent(Color.PINK);

        diningRoom.addStudent(3);



        assertEquals(yellow,diningRoom.getNumberOfStudent(0));
        assertEquals(blue,diningRoom.getNumberOfStudent(1));
        assertEquals(green,diningRoom.getNumberOfStudent(2));
        assertEquals(red,diningRoom.getNumberOfStudent(3));
        assertEquals(pink,diningRoom.getNumberOfStudent(4));
    }

    @Test
    public void Money(){
        int pink=4;
        diningRoom.addStudent(Color.PINK);
        diningRoom.addStudent(Color.PINK);
        diningRoom.addStudent(Color.PINK);
        diningRoom.addStudent(Color.PINK);



        assertEquals(pink,diningRoom.getNumberOfStudent(4));
    }

    @Test
    public void present(){
        List<Color> list = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.RED,Color.GREEN,Color.YELLOW,Color.BLUE));
        diningRoom.refillDining(list);
        assertEquals(Arrays.asList(2,1,1,1,0) , diningRoom.getStudents());

        List<Color> list2 = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.YELLOW,Color.BLUE));
        assertTrue(diningRoom.isPresent(list2));
        List<Color> list3 = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.RED,Color.GREEN));
        assertTrue(diningRoom.isPresent(list3));
        List<Color> list4 = new ArrayList<>(Arrays.asList(Color.RED,Color.RED,Color.BLUE));
        assertFalse(diningRoom.isPresent(list4));
        List<Color> list5 = new ArrayList<>();
        assertTrue(diningRoom.isPresent(list5));
        List<Color> list6 = new ArrayList<>(Arrays.asList(Color.RED,Color.GREEN,Color.BLUE,Color.GREEN));
        assertFalse(diningRoom.isPresent(list6));
        List<Color> list7 = new ArrayList<>(Arrays.asList(Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.PINK));
        assertFalse(diningRoom.isPresent(list7));

        assertTrue(diningRoom.isPresent(Color.YELLOW));
        assertTrue(diningRoom.isPresent(Color.RED));
        assertFalse(diningRoom.isPresent(Color.PINK));
    }

}