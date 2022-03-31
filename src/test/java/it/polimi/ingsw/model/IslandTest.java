package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Island methods
 */
public class IslandTest {

    @Test
    public void addStudentTest(){
        Island isl = new Island();
        isl.addStudent(Color.RED);
        isl.addStudent(Color.RED);
        isl.addStudent(Color.PINK);
        isl.addStudent(Color.BLUE);
        isl.addStudent(Color.GREEN);
        assertEquals(1, isl.getSelectedStudents(Color.PINK));
        assertEquals(2, isl.getSelectedStudents(Color.RED));
        assertEquals(1, isl.getSelectedStudents(Color.BLUE));
        assertEquals(0, isl.getSelectedStudents(Color.YELLOW));
        assertEquals(1, isl.getSelectedStudents(Color.GREEN));
        isl.addStudents(Color.YELLOW, 10);
        assertEquals(10, isl.getSelectedStudents(Color.YELLOW));
    }

    @Test
    public void BanCardsTest(){
        Island isl = new Island();
        assertEquals(0, isl.getBanCards());
        isl.addBanCard();
        assertEquals(1, isl.getBanCards());
        isl.removeBanCard();
        assertEquals(0, isl.getBanCards());
        isl.setBanCards(3);
        assertEquals(3, isl.getBanCards());
    }

    @Test
    public void IsSameOwnerTest(){
        Island isl1 = new Island();
        Island isl2 = new Island();

        Player player1 = new Player("Paolo");
        Player player2 = new Player("Federico");
        Player player3 = new Player("Paolo");

        isl1.setOwner(player1);
        isl2.setOwner(player2);
        assertFalse(isl1.isSameOwner(isl2));
        isl2.setOwner(player3);
        assertTrue(isl1.isSameOwner(isl2));
    }

    @Test
    public void IsSameOwnerNullTest(){
        Player player1 = new Player("Paolo");

        Island isl1 = new Island();
        Island isl2 = new Island();

        assertFalse(isl1.isSameOwner(isl2));
        isl1.setOwner(player1);
        assertFalse(isl1.isSameOwner(isl2));
    }

}
