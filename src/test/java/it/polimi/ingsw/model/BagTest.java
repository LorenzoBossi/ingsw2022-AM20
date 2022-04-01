package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Bag methods
 */
public class BagTest {
    Bag bag;

    @BeforeEach
    public void init(){
        bag = new Bag();
    }

    @Test
    public void fillBagTest(){
        assertTrue(bag.isEmpty());
        bag.fillBag(3);
        assertFalse(bag.isEmpty());
        List<Color>std = bag.getStudents(15);
        assertTrue(bag.isEmpty());
        assertEquals(15, std.size());
        bag.addStudents(std);
        assertFalse(bag.isEmpty());
    }

    @Test
    public void getStudentsTest(){
        bag.fillBag(10000);
        List<Color> std = bag.getStudents(10000);
        bag.addStudents(std);
        int yellow = 0, blue = 0, green = 0, red = 0, pink = 0;
        for(Color c : std){
            if(c == Color.YELLOW)
                yellow++;
            else if(c == Color.BLUE)
                blue++;
            else if(c == Color.GREEN)
                green++;
            else if(c == Color.RED)
                red++;
            else if(c == Color.PINK)
                pink++;
        }
        assertEquals(20000, yellow + blue + green + red + pink + std.size());
    }
}
