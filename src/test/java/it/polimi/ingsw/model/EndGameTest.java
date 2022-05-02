package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EndGameTest {
    private Game game;
    private PlayerBoard board;

    @BeforeEach
    public void initGame(){
        game = new Game(3);
        game.addPlayer("Ada");
        game.addPlayer("Bob");
        game.addPlayer("Jack");
        game.start();
    }

    @Test
    public void endedAssistantTest(){

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 1
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 2
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 3
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 4
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 5
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 6
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 7
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 8
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION

        assertEquals(Phase.ENDED,game.getPhase());//bag is already empty!


        game= new Game(2);
        game.addPlayer("Chiara");
        game.addPlayer("Andrea");
        game.start();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();game.nextPlayer();

        assertEquals(Phase.ENDED,game.getPhase());
        game.nextPlayer();
        game.nextPlayer();
        assertEquals(Phase.ENDED,game.getPhase());
    }

    @Test
    public void emptyBagTest(){
        Bag bag= game.getBag();
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 1
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION
        bag.getStudents(119);
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        bag.getStudents(1000);
        assertEquals(Phase.PIANIFICATION,game.getPhase());
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 1
        assertEquals(Phase.ACTION,game.getPhase());
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //ACTION
        assertEquals(Phase.ENDED,game.getPhase());
    }
    @Test
    public void onlyThreeIslandsTest(){
        IslandsManager islandsManager= game.getArchipelago();
        assertEquals(12,islandsManager.getNumberOfIslands());
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 1

        islandsManager.getIsland(1).setOwner(game.getPlayerByNickname("Ada"));
        islandsManager.getIsland(2).setOwner(game.getPlayerByNickname("Ada"));
        islandsManager.getIsland(0).setOwner(game.getPlayerByNickname("Ada"));
        islandsManager.mergeIslands(islandsManager.getIsland(1));
        assertEquals(10,islandsManager.getNumberOfIslands());
        game.nextPlayer();

        islandsManager.getIsland(5).setOwner(game.getPlayerByNickname("Bob"));
        islandsManager.getIsland(4).setOwner(game.getPlayerByNickname("Bob"));
        islandsManager.getIsland(6).setOwner(game.getPlayerByNickname("Bob"));
        islandsManager.mergeIslands(islandsManager.getIsland(5));
        game.nextPlayer();
        assertEquals(8,islandsManager.getNumberOfIslands());

        islandsManager.getIsland(5).setOwner(game.getPlayerByNickname("Jack"));
        islandsManager.getIsland(6).setOwner(game.getPlayerByNickname("Jack"));
        islandsManager.getIsland(7).setOwner(game.getPlayerByNickname("Jack"));
        islandsManager.mergeIslands(islandsManager.getIsland(6));
        game.nextPlayer();
        assertEquals(6,islandsManager.getNumberOfIslands());

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 2

        assertEquals(Phase.ACTION,game.getPhase());

        islandsManager.getIsland(1).setOwner(game.getPlayerByNickname("Ada"));
        islandsManager.getIsland(2).setOwner(game.getPlayerByNickname("Ada"));
        islandsManager.getIsland(0).setOwner(game.getPlayerByNickname("Ada"));
        islandsManager.mergeIslands(islandsManager.getIsland(1));
        assertEquals(4,islandsManager.getNumberOfIslands());

        islandsManager.getIsland(1).setOwner(game.getPlayerByNickname("Ada"));
        islandsManager.mergeIslands(islandsManager.getIsland(1));
        assertEquals(3,islandsManager.getNumberOfIslands());

        assertEquals(Phase.ENDED,game.getPhase());
    }
    @Test
    public void finishedTowersTest(){
        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 1
        game.nextPlayer();
        game.getPlayerByNickname("Ada").getPlayerBoard().removeTowers(3);
        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        game.nextPlayer(); //ACTION

        game.nextPlayer();game.nextPlayer();game.nextPlayer(); //PIANIFICATION 1
        game.nextPlayer();
        game.getPlayerByNickname("Ada").getPlayerBoard().removeTowers(2);
        assertEquals(Phase.ACTION,game.getPhase());
        game.getPlayerByNickname("Ada").getPlayerBoard().removeTowers(1);
        assertEquals(Phase.ENDED,game.getPhase());
        game.nextPlayer();
        assertEquals(Phase.ENDED,game.getPhase());
        game.nextPlayer(); //ACTION


        game= new Game(2);
        game.addPlayer("Chiara");
        game.addPlayer("Andrea");
        game.start();

        game.nextPlayer();game.nextPlayer();
        game.nextPlayer();
        assertEquals(Phase.ACTION,game.getPhase());
        game.getPlayerByNickname("Chiara").getPlayerBoard().removeTowers(10);
        assertEquals(Phase.ENDED,game.getPhase());
    }

}
