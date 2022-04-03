package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the updateInfluence method
 */
public class UpdateInfluenceTest {
    Game game;
    List<Player> players;
    ProfessorManager professorManager;
    IslandsManager archipelago;
    Player player1;
    Player player2;
    Player player3;

    @BeforeEach
    public void init() {
        game = new Game();
        game.addPlayer("Federico");
        game.addPlayer("Giacomo");
        game.addPlayer("Carlo");
        players = game.getPlayers();
        archipelago = game.getArchipelago();
        professorManager = game.getProfessorManager();
        player1 = players.get(0);
        player2 = players.get(1);
        player3 = players.get(2);
        professorManager.takeProfessor(player1, Color.YELLOW);
        professorManager.takeProfessor(player1, Color.BLUE);
        professorManager.takeProfessor(player2, Color.PINK);
        professorManager.takeProfessor(player3, Color.GREEN );
    }

    @Test
    public void normalUpdateInfluenceTest() {
        Island island = archipelago.getIsland(0);
        island.setOwner(player2);
        island.addStudents(Color.YELLOW, 5);
        island.addStudents(Color.PINK, 4);
        game.updateInfluence(island);
        assertEquals(player1, island.getOwner());
    }

    @Test
    public void sameInfluenceUpdateTest() {
        Island island = archipelago.getIsland(0);
        island.setOwner(player2);
        island.addStudents(Color.YELLOW, 5);
        island.addStudents(Color.PINK, 5);
        game.updateInfluence(island);
        assertEquals(player2, island.getOwner());
    }

    @Test
    public void sameInfluenceUpdate3PlayersTest() {
        Island island = archipelago.getIsland(0);
        island.setOwner(player2);
        island.addStudents(Color.YELLOW, 5);
        island.addStudents(Color.PINK, 5);
        island.addStudents(Color.GREEN, 6);
        game.updateInfluence(island);
        assertEquals(player3, island.getOwner());
    }

    @Test
    public void IslandWithNoOwnerTest() {
        Island island = archipelago.getIsland(0);
        island.addStudents(Color.YELLOW, 5);
        island.addStudents(Color.PINK, 6);
        game.updateInfluence(island);
        assertEquals(player2, island.getOwner());
    }

    @Test
    public void IslandWithNoOwnerSameInfluenceTest() {
        Island island = archipelago.getIsland(0);
        island.addStudents(Color.YELLOW, 5);
        island.addStudents(Color.PINK, 5);
        island.addStudents(Color.GREEN, 5);
        game.updateInfluence(island);
        assertNull(island.getOwner());
    }

    @Test
    public void IslandNoOwnerTowerTest() {
        Island island = archipelago.getIsland(0);
        island.addStudents(Color.YELLOW, 5);
        island.addStudents(Color.PINK, 6);
        game.updateInfluence(island);
        assertEquals(7, player2.getPlayerBoard().getNumberTower());
    }

    @Test
    public void MoveTowersTest() {
        Island island = archipelago.getIsland(0);
        island.setOwner(player2);
        island.setNumberOfTowers(3);
        island.addStudents(Color.PINK, 6);
        island.addStudents(Color.YELLOW, 10);
        game.updateInfluence(island);
        assertEquals(11, player2.getPlayerBoard().getNumberTower());
        assertEquals(5, player1.getPlayerBoard().getNumberTower());
    }

    @Test void MergeUpdateInfluenceTest() {
        Island island = archipelago.getIsland(0);
        Island island1 = archipelago.getIsland(11);
        Island island2 = archipelago.getIsland(1);
        island2.addStudents(Color.GREEN, 10);
        island2.setOwner(player1);
        island1.setOwner(player1);
        island.setOwner(player2);
        island.setNumberOfTowers(3);
        island.addStudents(Color.PINK, 6);
        island.addStudents(Color.YELLOW, 10);
        game.updateInfluence(island);
        assertEquals(10, archipelago.getNumberOfIslands());
        assertEquals(3, island.getNumberOfTowers());
        assertEquals(10, island.getSelectedStudents(Color.GREEN));
    }

}
