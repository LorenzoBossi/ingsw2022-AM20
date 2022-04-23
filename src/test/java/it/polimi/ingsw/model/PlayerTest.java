package it.polimi.ingsw.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;



public class PlayerTest {
    private Player player = new Player("matteo");

    @Test
    public void getNickname() {
        assertEquals("matteo", player.getNickname());
    }

    @Test
    public void priority(){
        player.setPlayerPriority(34);
        assertEquals(34,player.getPlayerPriority());
    }

    @Test
    public void Mother(){
        player.setMotherNatureMaxMove(21);
        assertEquals(21, player.getMotherNatureMaxMove() );
    }
    @Test
    public void Board(){
        PlayerBoard board = player.getPlayerBoard();
        assertNotNull(board);
    }


    @Test
    public void Coins(){
        assertEquals(0,player.getCoins());
        player.setCoins(24);
        assertEquals(24,player.getCoins());
        player.addCoin();
        assertEquals(25,player.getCoins());
        player.useCoins(12);
        assertEquals(13,player.getCoins());
        player.useCoins(50);
        assertEquals(13, player.getCoins());
    }
    @Test
    public void MoveLegit(){
        player.setMotherNatureMaxMove(3);
        assertTrue(player.isMotherNatureMoveLegit(2));
        assertFalse(player.isMotherNatureMoveLegit(0));
        assertFalse(player.isMotherNatureMoveLegit(5));
    }
}