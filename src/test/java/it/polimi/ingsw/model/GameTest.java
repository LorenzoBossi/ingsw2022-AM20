package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * This class test the Game methods
 */
public class GameTest {
    Game game;

    @BeforeEach
    public void init(){
        game = new Game();
    }

    @Test
    public void PlayersGameTest(){
        game.addPlayer("Paolo");
        assertEquals(1, game.getPlayers().size());
        assertFalse(game.addPlayer("Paolo"));
        assertEquals(1, game.getPlayers().size());
        game.addPlayer("Simone");
        assertEquals(2, game.getPlayers().size());
        Player player = game.getPlayerByNickname("Simone");
        assertEquals("Simone", player.getNickname());
        game.removePlayer("Simone");
        assertEquals(1, game.getPlayers().size());
    }

    @Test
    public void CloudTest(){
        assertEquals(0, game.getClouds().size());
        game.addClouds(3);
        assertEquals(3, game.getClouds().size());
        game.addClouds(3);
        assertEquals(6, game.getClouds().size());
    }

    @Test
    public void getAssistantTest(){
        Assistant assistant = game.getAssistantByName(AssistantName.ASSISTANT1);
        assertEquals(AssistantName.ASSISTANT1, assistant.getAssistant());
    }

}
