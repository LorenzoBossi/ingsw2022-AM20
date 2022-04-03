package it.polimi.ingsw.model;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the methods of the characterCard
 */
public class CharacterCardTest {
    Player player1;
    Player player2;
    Game game;
    Island island;
    ProfessorManager professorManager;

    @BeforeEach
    public void init() {
        game = new Game();
        game.addPlayer("Paolo");
        game.addPlayer("Simone");
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);
        player1.getPlayerChoice().selectColor(Color.YELLOW);
        player1.getPlayerChoice().selectIslandPosition(0);
        island = game.getArchipelago().getIsland(0);
        island.addStudents(Color.YELLOW, 5);
        professorManager = game.getProfessorManager();
        professorManager.takeProfessor(player1, Color.YELLOW);
        island.setNumberOfTowers(2);
        game.setCurrPlayer(player1);

    }

    @Test
    public void VassalEffectTest() {
        CharacterCard card = new Vassal(3);
        assertNull(island.getOwner());
        card.activateEffect(game);
        assertEquals(player1, island.getOwner());
    }

    @Test
    public void BanCardEffectTest() {
        CharacterCard card = new BanCharacter(3);
        assertEquals(0, island.getBanCards());
        card.activateEffect(game);
        assertEquals(1, island.getBanCards());
    }

    @Test
    public void InfluenceCardNoTowerEffectTest() {
        CharacterCard card = new InfluenceCard(3, new NoTowerInfluence());

        island.setOwner(player1);
        professorManager.takeProfessor(player2, Color.PINK);
        island.addStudents(Color.PINK, 6);
        game.updateInfluence(island);
        assertEquals(player1, island.getOwner());
        card.activateEffect(game);
        game.updateInfluence(island);
        assertEquals(player2, island.getOwner());
    }

    @Test
    public  void InfluenceCardNoColorEffectTest() {
        CharacterCard card = new InfluenceCard(3, new NoColorInfluence());

        island.setOwner(player1);
        professorManager.takeProfessor(player2, Color.PINK);
        island.addStudents(Color.PINK, 3);
        game.updateInfluence(island);
        assertEquals(player1, island.getOwner());
        card.activateEffect(game);
        game.updateInfluence(island);
        assertEquals(player2, island.getOwner());
    }

    @Test
    public void InfluenceCard2MorePointsEffectTest() {
        CharacterCard card = new InfluenceCard(3, new MorePointsInfluence());

        island.setOwner(player2);
        professorManager.takeProfessor(player2, Color.PINK);
        island.addStudents(Color.PINK, 3);
        game.updateInfluence(island);
        assertEquals(player2, island.getOwner());
        card.activateEffect(game);
        game.updateInfluence(island);
        assertEquals(player1, island.getOwner());
    }

    @Test
    public void resetGameStrategyTest(){
        CharacterCard card = new InfluenceCard(3, new NoColorInfluence());

        island.setOwner(player1);
        professorManager.takeProfessor(player2, Color.PINK);
        island.addStudents(Color.PINK, 3);
        game.updateInfluence(island);
        assertEquals(player1, island.getOwner());
        card.activateEffect(game);
        game.updateInfluence(island);
        assertEquals(player2, island.getOwner());
        island.setNumberOfTowers(1);
        game.updateInfluence(island);
        assertEquals(player1, island.getOwner());
    }

    @Test
    public void IncreaseCoinTest() {
        CharacterCard card = new Vassal(3);
        assertTrue(card.isFirstTimePlayed());
        card.activateEffect(game);
        assertEquals(4, card.getCoinsRequired());
        card.activateEffect(game);
        assertEquals(4, card.getCoinsRequired());
    }
}
