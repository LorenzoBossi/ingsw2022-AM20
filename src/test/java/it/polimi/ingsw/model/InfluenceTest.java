package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.MorePointsInfluence;
import it.polimi.ingsw.model.characterCards.NoColorInfluence;
import it.polimi.ingsw.model.characterCards.NoTowerInfluence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the methods to calculate influence
 */
public class InfluenceTest {
    ProfessorManager professorManager;
    Player player1;
    Player player2;
    Player player3;
    Island island;

    @BeforeEach
    public void init(){
        professorManager = new ProfessorManager();
        player1 = new Player("Federico");
        player2 = new Player("Giacomo");
        player3 = new Player("Carlo");
        island = new Island();

        professorManager.takeProfessor(player1, Color.RED);
        professorManager.takeProfessor(player2, Color.YELLOW);
        professorManager.takeProfessor(player1, Color.GREEN);

        island.setOwner(player1);
        island.setNumberOfTowers(2);

        island.addStudents(Color.RED, 4);
        island.addStudents(Color.GREEN, 4);
        island.addStudents(Color.YELLOW, 2);
        island.addStudents(Color.PINK, 1);
        island.addStudents(Color.BLUE, 2);
    }

    @Test
    public void calculateStandardInfluenceTest() {
        InfluenceStrategy influenceStrategy = new StandardInfluence();

        assertEquals(10, influenceStrategy.calculateInfluence(player1, island, professorManager));
        assertEquals(0, influenceStrategy.calculateInfluence(player3, island, professorManager));
        assertEquals(2, influenceStrategy.calculateInfluence(player2, island, professorManager));

        professorManager.takeProfessor(player3, Color.BLUE);
        professorManager.takeProfessor(player3, Color.PINK);
        assertEquals(3, influenceStrategy.calculateInfluence(player3, island, professorManager));
    }

    @Test
    public void TwoMorePointsInfluenceTest() {
        InfluenceStrategy influenceStrategy = new MorePointsInfluence();
        influenceStrategy.setCurrPlayer(player3);
        professorManager.takeProfessor(player3, Color.BLUE);
        professorManager.takeProfessor(player3, Color.PINK);
        assertEquals(5, influenceStrategy.calculateInfluence(player3, island, professorManager));
        assertEquals(10, influenceStrategy.calculateInfluence(player1, island, professorManager));
    }

    @Test
    public void NoColorInfluenceTest() {
        InfluenceStrategy influenceStrategy = new NoColorInfluence();
        influenceStrategy.setCurrPlayer(player1);
        player1.getPlayerChoice().selectColor(Color.GREEN);

        assertEquals(6, influenceStrategy.calculateInfluence(player1, island, professorManager));

        island.setOwner(player3);
        assertEquals(2, influenceStrategy.calculateInfluence(player3, island, professorManager));
        assertEquals(2, influenceStrategy.calculateInfluence(player2, island, professorManager));
    }

    @Test
    public void NoTowerInfluenceTest() {
        InfluenceStrategy influenceStrategy = new NoTowerInfluence();

        assertEquals(8, influenceStrategy.calculateInfluence(player1, island, professorManager));
    }

}
