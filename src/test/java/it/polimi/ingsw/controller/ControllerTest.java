package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class tests the Controller's method
 */
public class ControllerTest {
    Game game;
    Controller controller;

    @BeforeEach
    public void init() {
        game = new Game(2);
        game.addPlayer("Carlo");
        game.addPlayer("Simone");
        game.getPlayerByNickname("Carlo").getPlayerBoard().addTowers(8);
        game.getPlayerByNickname("Simone").getPlayerBoard().addTowers(8);
        List<Color> studentsToAdd = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.BLUE, Color.RED));
        game.getCurrPlayer().getPlayerBoard().getEntrance().refillEntrance(studentsToAdd);
        controller = new Controller(game);
    }

    @Test
    public void entranceInitializationForTest() {
        game = new Game(2);
        game.addPlayer("Carlo");
        Player currPlayer = game.getCurrPlayer();
        PlayerBoard board = currPlayer.getPlayerBoard();
        List<Color> studentsToAdd = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.BLUE, Color.RED));
        List<Integer> students = board.getEntrance().getStudents();
        int numberOfStudents = 0;

        board.getEntrance().refillEntrance(studentsToAdd);

        for (Color color : Color.values()) {
            numberOfStudents += students.get(color.ordinal());
        }
        assertEquals(4, numberOfStudents);
    }

    @Test
    public void moveStudentOnIslandTest() {
        Island selectedIsland;
        Entrance entrance = game.getCurrPlayer().getPlayerBoard().getEntrance();
        List<Color> studentsToCheck = new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE));

        assertTrue(entrance.isPresent(studentsToCheck));

        controller.moveStudentToIsland(Color.BLUE, 4);
        selectedIsland = game.getArchipelago().getIsland(4);

        assertEquals(1, selectedIsland.getSelectedStudents(Color.BLUE));
        assertTrue(entrance.isPresent(Color.RED));
        assertTrue(entrance.isPresent(Color.BLUE));
        assertFalse(entrance.isPresent(studentsToCheck));
    }

    @Test
    public void playerChoiceSettingTest() {
        Player currPlayer = game.getCurrPlayer();
        PlayerChoice choice = currPlayer.getPlayerChoice();
        List<Color> studentsFromEntrance = new ArrayList<>(Arrays.asList(Color.BLUE, Color.PINK));
        List<Color> studentsFromCard = new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN));

        controller.setColorSelection(Color.BLUE);
        controller.setIslandSelection(4);
        controller.setStudentsSelectedEntrance(studentsFromEntrance);

        assertEquals(choice.getSelectedColor(), Color.BLUE);

        assertEquals(game.getArchipelago().getIsland(4), choice.getSelectedIsland());

        assertTrue(studentsFromEntrance.containsAll(choice.getSelectedStudentFromEntrance()));
        assertTrue(choice.getSelectedStudentFromEntrance().containsAll(studentsFromEntrance));

        assertNull(choice.getSelectedStudents());

        controller.setStudentsSelectedFromCard(studentsFromCard);

        assertTrue(studentsFromCard.containsAll(choice.getSelectedStudents()));
        assertTrue(choice.getSelectedStudents().containsAll(studentsFromCard));
    }

    @Test
    public void useCharacterCardTest() {
        Player currPlayer = game.getCurrPlayer();
        Island island = game.getArchipelago().getIsland(4);

        game.getBag().fillBag(24);
        game.initCoins();
        game.initCharacterCards();
        game.addCoinsToPlayer("Carlo");
        game.addCoinsToPlayer("Carlo");
        game.addCoinsToPlayer("Carlo");

        assertEquals(4, game.getPlayerByNickname("Carlo").getCoins());
        assertEquals(15, game.getNumberOfCoins());

        while (game.getCharacterCardByName(CharacterName.VASSAL) == null) {
            game.initCharacterCards();
        }

        assertEquals(3, game.getCharacterCardByName(CharacterName.VASSAL).getCoinsRequired());

        currPlayer.getPlayerChoice().selectIsland(island);
        controller.useCharacterCard(CharacterName.VASSAL);

        assertEquals(18, game.getNumberOfCoins());
        assertEquals(1, currPlayer.getCoins());
        assertEquals(4, game.getCharacterCardByName(CharacterName.VASSAL).getCoinsRequired());
    }

    @Test
    public void MoveStudentsToDiningRoomTest() {
        Player currPlayer = game.getCurrPlayer();
        PlayerBoard board = currPlayer.getPlayerBoard();
        ProfessorManager professorManager = game.getProfessorManager();
        List<Color> professorsOwned;

        board.getEntrance().addStudent(Color.BLUE);
        game.initCoins();

        controller.moveStudentToDiningRoom(Color.BLUE);
        assertEquals(1, board.getDiningRoom().getNumberOfStudent(Color.BLUE));

        professorsOwned = professorManager.getProfessorsOwnedBy(currPlayer);
        assertEquals(1, professorsOwned.size());
        assertEquals(Color.BLUE, professorsOwned.get(0));


        controller.moveStudentToDiningRoom(Color.BLUE);
        controller.moveStudentToDiningRoom(Color.BLUE);

        assertEquals(3, board.getDiningRoom().getNumberOfStudent(Color.BLUE));
        assertEquals(2, currPlayer.getCoins());
    }

    @Test
    public void changeProfessorTest() {
        ProfessorManager professorManager = game.getProfessorManager();
        List<Color> professorsOwned;
        Player player = game.getPlayerByNickname("Simone");

        controller.moveStudentToDiningRoom(Color.BLUE);
        professorsOwned = professorManager.getProfessorsOwnedBy(game.getCurrPlayer());

        assertEquals(1, professorsOwned.size());
        assertEquals(Color.BLUE, professorsOwned.get(0));

        game.setCurrPlayer(player);
        List<Color> studentsToAdd = new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE));
        game.getCurrPlayer().getPlayerBoard().getEntrance().refillEntrance(studentsToAdd);

        controller.moveStudentToDiningRoom(Color.BLUE);
        assertEquals(0, professorManager.getProfessorsOwnedBy(player).size());
        controller.moveStudentToDiningRoom(Color.BLUE);
        assertEquals(Color.BLUE, professorManager.getProfessorsOwnedBy(player).get(0));
    }

    @Test
    public void selectCloudTest() {
        Entrance entrance = game.getCurrPlayer().getPlayerBoard().getEntrance();

        game.initGame();
        Cloud cloud = game.getClouds().get(1);
        List<Color> studentsOnCloud = new ArrayList<>(Arrays.asList(Color.PINK, Color.PINK, Color.PINK));

        cloud.fill(studentsOnCloud);
        controller.moveStudentToDiningRoom(Color.BLUE);
        controller.moveStudentToDiningRoom(Color.BLUE);
        controller.moveStudentToDiningRoom(Color.RED);

        controller.selectCloud(1);
        assertTrue(cloud.isEmpty());
        assertTrue(cloud.isChosen());

        assertTrue(entrance.isPresent(studentsOnCloud));
    }

    @Test
    public void MoveMotherNatureTest() {
        IslandsManager islandsManager = game.getArchipelago();
        Island island = islandsManager.getIsland(4);

        controller.moveStudentToDiningRoom(Color.BLUE);
        controller.moveStudentToIsland(Color.BLUE, 4);

        assertNull(island.getOwner());

        controller.moveMotherNature(4);
        assertEquals(game.getCurrPlayer(), island.getOwner());
    }

    @Test
    public void MoveMotherNatureBanCardTest() {
        IslandsManager islandsManager = game.getArchipelago();
        Island island = islandsManager.getIsland(4);

        islandsManager.addBanCardOnIsland(island);
        assertTrue(island.isBanCardPresent());

        controller.moveStudentToDiningRoom(Color.BLUE);
        controller.moveStudentToIsland(Color.BLUE, 4);

        assertNull(island.getOwner());

        controller.moveMotherNature(4);
        assertNull(island.getOwner());
        assertFalse(island.isBanCardPresent());
    }


}
