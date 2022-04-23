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

}