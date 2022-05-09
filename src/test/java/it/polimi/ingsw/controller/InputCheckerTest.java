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
 * Class InputCheckerTest tests the InputChecker's methods
 */
public class InputCheckerTest {
    Game game;
    InputChecker inputChecker;

    @BeforeEach
    public void init() {
        game = new Game(2);
        game.addPlayer("Carlo");
        game.addPlayer("Simone");
        game.getPlayerByNickname("Carlo").getPlayerBoard().addTowers(8);
        game.getPlayerByNickname("Simone").getPlayerBoard().addTowers(8);
        List<Color> studentsToAdd = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.BLUE, Color.RED));
        game.getCurrPlayer().getPlayerBoard().getEntrance().refillEntrance(studentsToAdd);
        inputChecker = new InputChecker(game);
    }

    @Test
    public void checkAssistantPlayedTest() {
        Player currPlayer = game.getCurrPlayer();
        Assistant assistant = game.getAssistantByName(AssistantName.ASSISTANT1);

        assertTrue(inputChecker.checkAssistantPlayed(AssistantName.ASSISTANT1));
        currPlayer.playAssistant(assistant);
        assertFalse(inputChecker.checkAssistantPlayed(AssistantName.ASSISTANT1));
    }

    @Test
    public void checkValidTurn() {
       assertTrue(inputChecker.checkValidTurn("Carlo"));
       assertFalse(inputChecker.checkValidTurn("Simone"));
    }

    @Test
    public void checkStudentInEntranceTest() {
        assertTrue(inputChecker.checkStudentInEntrance(Color.BLUE));
        assertTrue(inputChecker.checkStudentInEntrance(Arrays.asList(Color.BLUE, Color.BLUE, Color.RED)));

        assertFalse(inputChecker.checkStudentInEntrance(Color.YELLOW));
        assertFalse(inputChecker.checkStudentInEntrance(Arrays.asList(Color.BLUE, Color.BLUE, Color.BLUE)));
    }

    @Test
    public void checkIslandPosition() {
        assertTrue(inputChecker.checkIslandPosition(11));
        assertTrue(inputChecker.checkIslandPosition(0));

        assertFalse(inputChecker.checkIslandPosition(12));
    }

    @Test
    public void checkMoveStudentToIslandTest() {
        assertTrue(inputChecker.checkMoveStudentToIsland(Color.BLUE, 2));

        assertFalse(inputChecker.checkMoveStudentToIsland(Color.BLUE, 12));
        assertFalse(inputChecker.checkMoveStudentToIsland(Color.PINK, 3));
    }

    @Test
    public void checkMotherNatureMove() {
        Player currPlayer = game.getCurrPlayer();
        currPlayer.playAssistant(game.getAssistantByName(AssistantName.ASSISTANT10));

        assertTrue(inputChecker.checkMotherNatureMove(3));
        assertFalse(inputChecker.checkMotherNatureMove(6));

        game.getBag().fillBag(20);
        while (game.getCharacterCardByName(CharacterName.POSTMAN) == null) {
            game.initCharacterCards();
        }

        game.getCharacterCardByName(CharacterName.POSTMAN).activateEffect(game);
        assertTrue(inputChecker.checkMotherNatureMove(6));
    }

    @Test
    public void checkSelectedCloudTest() {
        game = new Game(2);
        game.addPlayer("Simone");
        game.addPlayer("Franco");
        game.start();
        Player currPlayer = game.getCurrPlayer();
        inputChecker = new InputChecker(game);

        currPlayer.addStudentFromCloud(game.getClouds().get(1), 1);

        assertTrue(inputChecker.checkSelectedCloud(0));
        assertFalse(inputChecker.checkSelectedCloud(4));
        assertFalse(inputChecker.checkSelectedCloud(1));
    }
}
