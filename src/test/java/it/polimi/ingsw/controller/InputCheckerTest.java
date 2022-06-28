package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
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
    public void checkStudentInDiningRoomTest() {
        Player currPlayer = game.getCurrPlayer();
        List<Color> studentsDining = new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE));
        currPlayer.getPlayerBoard().getDiningRoom().refillDining(studentsDining);

        assertTrue(inputChecker.checkStudentInTheDiningRoom(Color.BLUE));

        currPlayer.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
        assertFalse(inputChecker.checkStudentInTheDiningRoom(Color.BLUE));
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

    @Test
    public void checkCardActivationCoinIslandTest() {
        CharacterCard card;
        game.initCoins();
        Player currPlayer = game.getCurrPlayer();

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.HERBALIST));

        game.getBag().fillBag(40);
        while (game.getCharacterCardByName(CharacterName.POSTMAN) == null || game.getCharacterCardByName(CharacterName.VASSAL) == null) {
            game.initCharacterCards();
        }

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.VASSAL));
        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.POSTMAN));

        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());
        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.VASSAL));
        currPlayer.getPlayerChoice().selectIsland(game.getArchipelago().getIsland(0));
        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.VASSAL));

    }

    @Test
    public void checkCardActivationBanCards() {
        CharacterCard card;
        game.initCoins();
        Player currPlayer = game.getCurrPlayer();

        game.getBag().fillBag(40);
        while (game.getCharacterCardByName(CharacterName.HERBALIST) == null) {
            game.initCharacterCards();
        }
        card = game.getCharacterCardByName(CharacterName.HERBALIST);

        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());
        currPlayer.getPlayerChoice().selectIsland(game.getArchipelago().getIsland(0));

        card.activateEffect(game);
        card.activateEffect(game);
        card.activateEffect(game);

        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.HERBALIST));

        card.activateEffect(game);

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.HERBALIST));
    }

    @Test
    public void checkJesterActivation() {
        CharacterCard card;
        game.initCoins();
        Player currPlayer = game.getCurrPlayer();

        game.getBag().fillBag(40);
        while (game.getCharacterCardByName(CharacterName.JESTER) == null) {
            game.initCharacterCards();
        }
        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());

        card = game.getCharacterCardByName(CharacterName.JESTER);
        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.JESTER));

        List<Color> studentsFromCard = new ArrayList<>(card.getStudents().subList(0, 2));
        List<Color> studentsFromEntrance = new ArrayList<>(Arrays.asList(Color.PINK, Color.PINK));
        currPlayer.getPlayerChoice().selectStudentFromEntrance(studentsFromEntrance);
        currPlayer.getPlayerChoice().selectStudents(studentsFromCard);
        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.JESTER));

        studentsFromEntrance = new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE));
        currPlayer.getPlayerChoice().selectStudentFromEntrance(studentsFromEntrance);
        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.JESTER));

        studentsFromEntrance = new ArrayList<>(Arrays.asList(Color.BLUE));
        currPlayer.getPlayerChoice().selectStudentFromEntrance(studentsFromEntrance);
        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.JESTER));
    }

    @Test
    public void checkBankerActivation() {
        CharacterCard card;
        game.initCoins();
        Player currPlayer = game.getCurrPlayer();

        game.getBag().fillBag(40);
        while (game.getCharacterCardByName(CharacterName.BANKER) == null) {
            game.initCharacterCards();
        }
        card = game.getCharacterCardByName(CharacterName.BANKER);
        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.BANKER));

        currPlayer.getPlayerChoice().selectColor(Color.YELLOW);
        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.BANKER));
    }

    @Test
    public void checkNormalCardActivation() {
        CharacterCard card;
        game.initCoins();
        Player currPlayer = game.getCurrPlayer();

        game.getBag().fillBag(40);
        while (game.getCharacterCardByName(CharacterName.PROF_CARD) == null) {
            game.initCharacterCards();
        }
        card = game.getCharacterCardByName(CharacterName.PROF_CARD);
        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());

        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.PROF_CARD));
    }

    @Test
    public void checkPrincessActivation() {
        CharacterCard card;
        game.initCoins();
        Player currPlayer = game.getCurrPlayer();

        game.getBag().fillBag(40);
        while (game.getCharacterCardByName(CharacterName.PRINCESS) == null) {
            game.initCharacterCards();
        }
        card = game.getCharacterCardByName(CharacterName.PRINCESS);
        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.PRINCESS));

        List<Color> studentsFromCard = new ArrayList<>(card.getStudents().subList(0, 2));
        currPlayer.getPlayerChoice().selectStudents(studentsFromCard);

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.PRINCESS));

        studentsFromCard = new ArrayList<>(card.getStudents().subList(0, 1));
        currPlayer.getPlayerChoice().selectStudents(studentsFromCard);

        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.PRINCESS));

        //Add 10 students of the same color in the dining room and try to activate the Princess
        studentsFromCard = new ArrayList<>(card.getStudents().subList(0, 1));
        List<Color> studentsDining = new ArrayList<>(Arrays.asList(studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0), studentsFromCard.get(0)));
        currPlayer.getPlayerBoard().getDiningRoom().refillDining(studentsDining);

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.PRINCESS));
    }

    @Test
    public void checkMonkActivation() {
        CharacterCard card;
        game.initCoins();
        Player currPlayer = game.getCurrPlayer();

        game.getBag().fillBag(40);
        while (game.getCharacterCardByName(CharacterName.MONK) == null) {
            game.initCharacterCards();
        }
        card = game.getCharacterCardByName(CharacterName.MONK);
        game.addCoinsToPlayer(currPlayer.getNickname());
        game.addCoinsToPlayer(currPlayer.getNickname());

        List<Color> studentsFromCard = new ArrayList<>(card.getStudents().subList(0, 1));
        currPlayer.getPlayerChoice().selectStudents(studentsFromCard);

        assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.MONK));

        currPlayer.getPlayerChoice().selectIsland(game.getArchipelago().getIsland(1));

        assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.MONK));
    }

    @Test
    public void checkMusicianActivation() {
            CharacterCard card;
            game.initCoins();
            Player currPlayer = game.getCurrPlayer();
            List<Color> studentsToAdd = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.BLUE, Color.RED));
            currPlayer.getPlayerBoard().getDiningRoom().refillDining(studentsToAdd);

            game.getBag().fillBag(40);
            while (game.getCharacterCardByName(CharacterName.MUSICIAN) == null) {
                game.initCharacterCards();
            }
            game.addCoinsToPlayer(currPlayer.getNickname());
            game.addCoinsToPlayer(currPlayer.getNickname());
            game.addCoinsToPlayer(currPlayer.getNickname());

            card = game.getCharacterCardByName(CharacterName.MUSICIAN);
            assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.MUSICIAN));

            List<Color> studentsFromDining = new ArrayList<>(Arrays.asList(Color.BLUE, Color.PINK));
            List<Color> studentsFromEntrance = new ArrayList<>(Arrays.asList(Color.PINK, Color.PINK));
            currPlayer.getPlayerChoice().selectStudentFromEntrance(studentsFromEntrance);
            currPlayer.getPlayerChoice().selectStudents(studentsFromDining);

            assertFalse(inputChecker.checkCharacterCardActivation(CharacterName.MUSICIAN));

            studentsFromDining.remove(Color.PINK);
            studentsFromEntrance = new ArrayList<>(Arrays.asList(Color.RED));
            currPlayer.getPlayerChoice().selectStudentFromEntrance(studentsFromEntrance);
            currPlayer.getPlayerChoice().selectStudents(studentsFromDining);

            assertTrue(inputChecker.checkCharacterCardActivation(CharacterName.MUSICIAN));
    }

}
