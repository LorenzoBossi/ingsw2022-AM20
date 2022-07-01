package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Comparator;


import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the ProfessorManager methods
 */
public class ProfessorManagerTest {
    ProfessorManager professorManager;

    @BeforeEach
    public void init() {
        professorManager = new ProfessorManager();
    }

    @Test
    public void initializationTest(){
        Color[] colors= Color.values();
        Player player=new Player("John");

        for(Color c : colors){
            assertEquals(professorManager.getMaxStudentsOf(c),0);
        }

        assertTrue(professorManager.getProfessorsOwnedBy(player).isEmpty());
    }

    @Test
    public void comparatorTest(){
        Player player=new Player("John");
        Comparator<Integer> newComparator= (x,y) -> x >= y ? 1 : 0;

        assertFalse(professorManager.canTakeProfessor(player, Color.GREEN));

        professorManager.setComparator(newComparator);

        assertTrue(professorManager.canTakeProfessor(player, Color.GREEN));
    }

    @Test
    public void takeProfessorTest(){
        Player player=new Player("John");
        Player secondPlayer=new Player("Jimmy");
        assertTrue(professorManager.getProfessorsOwnedBy(player).isEmpty());

        professorManager.takeProfessor(player,Color.RED);
        assertTrue(professorManager.getProfessorsOwnedBy(player).contains(Color.RED));
        assertEquals(professorManager.getProfessorsOwnedBy(player).size(),1);

        professorManager.takeProfessor(secondPlayer,Color.RED);
        assertFalse(professorManager.getProfessorsOwnedBy(player).contains(Color.RED));
        assertEquals(professorManager.getProfessorsOwnedBy(player).size(),0);
        assertTrue(professorManager.getProfessorsOwnedBy(secondPlayer).contains(Color.RED));
        assertEquals(professorManager.getProfessorsOwnedBy(secondPlayer).size(),1);

    }

    @Test
    public void canTakeProfessorTest(){
        Player player1=new Player("John");
        Player player2=new Player("Jimmy");
        player1.getPlayerBoard().getDiningRoom().addStudent(Color.YELLOW);
        assertTrue(professorManager.canTakeProfessor(player1,Color.YELLOW));
        professorManager.takeProfessor(player1,Color.YELLOW);

        player1.getPlayerBoard().getDiningRoom().addStudent(Color.YELLOW);

        player2.getPlayerBoard().getDiningRoom().addStudent(Color.YELLOW);
        player2.getPlayerBoard().getDiningRoom().addStudent(Color.YELLOW);

        assertFalse(professorManager.canTakeProfessor(player2,Color.YELLOW));
    }
}

