package it.polimi.ingsw.model;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the IslandManager methods
 */
public class IslandsManagerTest {
    IslandsManager mng;

    @BeforeEach
    public void init(){
        mng = new IslandsManager();
    }

    @Test
    public void MotherNatureMovements(){
        Island isl = mng.moveMotherNature(8);
        assertSame(mng.getIsland(8), isl);
        assertEquals(8, mng.getMotherNature());
        isl = mng.moveMotherNature(7);
        assertSame(mng.getIsland(2), isl);
        assertEquals(2, mng.getMotherNature());
    }

    @Test
    public void CheckNearIsland(){
        List<Island> nearIslands = mng.getNeighbouringIslands(mng.getIsland(0));
        assertTrue (nearIslands.contains(mng.getIsland(11)));
        assertFalse(nearIslands.contains(mng.getIsland(6)));
        assertTrue(nearIslands.contains(mng.getIsland(1)));
        assertEquals(2, nearIslands.size());

        nearIslands = mng.getNeighbouringIslands(mng.getIsland(11));
        assertTrue(nearIslands.contains(mng.getIsland(0)));
        assertFalse(nearIslands.contains(mng.getIsland(6)));
        assertTrue(nearIslands.contains(mng.getIsland(10)));
        assertEquals(2, nearIslands.size());

        nearIslands = mng.getNeighbouringIslands(mng.getIsland(5));
        assertTrue(nearIslands.contains(mng.getIsland(6)));
        assertFalse(nearIslands.contains(mng.getIsland(9)));
        assertTrue(nearIslands.contains(mng.getIsland(4)));
        assertEquals(2, nearIslands.size());
    }

    @Test
    public void IslandsMergingTestWithSameOwner(){
        Player player1 = new Player("Paolo");

        mng.getIsland(2).setOwner(player1);
        mng.getIsland(2).addStudents(Color.YELLOW, 5);
        mng.getIsland(2).setNumberOfTowers(2);

        mng.getIsland(3).setOwner(player1);
        mng.getIsland(3).addStudents(Color.YELLOW, 5);
        mng.getIsland(3).setNumberOfTowers(1);

        mng.getIsland(4).setOwner(player1);
        mng.getIsland(4).addStudents(Color.YELLOW, 5);
        mng.getIsland(4).setBanCards(3);


        mng.mergeIslands(mng.getIsland(3));
        assertEquals(10, mng.getNumberOfIslands());
        assertEquals(3, mng.getIsland(mng.getMotherNature()).getBanCards());
        assertEquals(15, mng.getIsland(mng.getMotherNature()).getSelectedStudents(Color.YELLOW));
        assertEquals(0, mng.getIsland(mng.getMotherNature()).getSelectedStudents(Color.RED));
        assertEquals(3, mng.getIsland(mng.getMotherNature()).getNumberOfTowers());
    }

    @Test
    public void IslandsMergingTestWithDifferentOwner(){
        Player player1 = new Player("Paolo");
        Player player2 = new Player("Federico");

        mng.getIsland(11).setOwner(player1);
        mng.getIsland(11).addStudents(Color.RED, 5);

        mng.getIsland(0).setOwner(player1);
        mng.getIsland(0).addStudents(Color.RED, 2);

        mng.getIsland(10).setOwner(player2);
        mng.getIsland(10).addStudents(Color.RED, 1);

        mng.mergeIslands(mng.getIsland(11));
        assertEquals(7, mng.getIsland(mng.getMotherNature()).getSelectedStudents(Color.RED));
        assertEquals(11, mng.getNumberOfIslands());
    }

    @Test
    public void IslandsMergingWithNullOwners(){
        mng.mergeIslands(mng.getIsland(11));
        assertEquals(12, mng.getNumberOfIslands());
    }

    @Test
    public void getTotalBanCardsTest(){
        mng.getIsland(5).addBanCard();
        mng.getIsland(10).addBanCard();
        mng.getIsland(3).setBanCards(3);
        assertEquals(5, mng.getNumberOfBanCards());
    }

}
