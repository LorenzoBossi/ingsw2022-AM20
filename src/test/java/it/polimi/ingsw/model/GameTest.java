package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Phase.LOBBY;
import static org.junit.jupiter.api.Assertions.*;
/**
 * This class test the Game methods
 */
public class GameTest {
    Game game;

    @BeforeEach
    public void init(){
        game = new Game(3);
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
        assertEquals(3, game.getClouds().size());

        game= new Game(2);
        assertEquals(2, game.getClouds().size());
    }

    @Test
    public void getAssistantTest(){
        Assistant assistant = game.getAssistantByName(AssistantName.ASSISTANT1);
        assertEquals(AssistantName.ASSISTANT1, assistant.getAssistant());
    }

    @Test
    public void initCharacterTest(){
        game.getBag().fillBag(12);
        game.initCharacterCards();
        assertEquals(3, game.getCharacterCards().size());
        System.out.println(game.getCharacterCards().get(0).getName());
        System.out.println(game.getCharacterCards().get(1).getName());
        System.out.println(game.getCharacterCards().get(2).getName());
    }

    /**
     * Testing the the nextPlayer() function simulating 2 complete rounds (pianification + action)
     */
    @Test
    public void roundTest(){
        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.addPlayer("Marco");
        Player p1,p2,p3;
        p1=game.getPlayerByNickname("Aldo");
        p2= game.getPlayerByNickname("Giovanni");
        p3= game.getPlayerByNickname("Marco");

        assertEquals(Phase.LOBBY,game.getPhase());

        game.start();

        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Aldo",game.getCurrPlayer().getNickname());
        p1.setPlayerPriority(10);

        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Giovanni",game.getCurrPlayer().getNickname());
        p2.setPlayerPriority(9);

        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Marco",game.getCurrPlayer().getNickname());
        p3.setPlayerPriority(9);


        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        assertEquals("Giovanni",game.getCurrPlayer().getNickname());

        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        assertEquals("Marco",game.getCurrPlayer().getNickname());

        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        assertEquals("Aldo",game.getCurrPlayer().getNickname());



        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Giovanni",game.getCurrPlayer().getNickname());
        p2.setPlayerPriority(8);

        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Marco",game.getCurrPlayer().getNickname());
        p3.setPlayerPriority(5);

        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Aldo",game.getCurrPlayer().getNickname());
        p1.setPlayerPriority(4);


        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        assertEquals("Aldo",game.getCurrPlayer().getNickname());

        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        assertEquals("Marco",game.getCurrPlayer().getNickname());

        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        assertEquals("Giovanni",game.getCurrPlayer().getNickname());


        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Aldo",game.getCurrPlayer().getNickname());

        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Giovanni",game.getCurrPlayer().getNickname());

        game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        assertEquals("Marco",game.getCurrPlayer().getNickname());

    }

    @Test
    public void availableMovesTest(){
        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.addPlayer("Marco");
        Player p1,p2,p3;
        p1=game.getPlayerByNickname("Aldo");
        p2=game.getPlayerByNickname("Giovanni");
        p3=game.getPlayerByNickname("Marco");

        game.start();

        assertEquals(0,p1.getAvailableMoves());
        assertEquals(0,p2.getAvailableMoves());
        assertEquals(0,p3.getAvailableMoves());


        //Aldo
        p1.setPlayerPriority(3);

        game.nextPlayer();

        //Giovanni
        p2.setPlayerPriority(2);

        game.nextPlayer();

        //Marco
        p3.setPlayerPriority(1);

        game.nextPlayer();
        //ACTION PHASE

        //Marco (p3) starts
        assertEquals(4,p3.getAvailableMoves());
        assertEquals(0,p2.getAvailableMoves());
        assertEquals(0,p1.getAvailableMoves());

        p3.setAvailableMoves(0);

        game.nextPlayer();
        assertEquals(0,p3.getAvailableMoves());
        assertEquals(4,p2.getAvailableMoves());
        assertEquals(0,p1.getAvailableMoves());
    }

    /**
     * Testing if the constructor fills the clouds correctly
     */
    @Test
    public void fillCloudTest(){
        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.addPlayer("Marco");
        game.start();
        for(Cloud cloud : game.getClouds()){
            assertEquals(4,cloud.getStudents().size());
        }
        Game game2 = new Game(2);
        game2.getBag().fillBag(20);
        for(Cloud cloud : game2.getClouds()){
            assertEquals(3,cloud.getStudents().size());
        }
    }
}
