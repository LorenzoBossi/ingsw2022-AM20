package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.ActionMove;
import it.polimi.ingsw.client.ActionMovesHandler;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class ActionMovesTest tests the ActionMovesHandler's method
 */
public class ActionMovesTest {

    @Test
    public void initializesTest() {
        ActionMovesHandler actionMovesHandler = new ActionMovesHandler();
        actionMovesHandler.setGameMode("experts");
        actionMovesHandler.setNumberOfPlayer(2);
        actionMovesHandler.initializeAction();
        assertEquals("ms/ac", actionMovesHandler.getAvailableActions());
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        assertEquals("mmn/ac", actionMovesHandler.getAvailableActions());
        String check = actionMovesHandler.getAvailableActions();
    }

    @Test
    public void consumeTest() {
        ActionMovesHandler actionMovesHandler = new ActionMovesHandler();
        actionMovesHandler.setGameMode("experts");
        actionMovesHandler.setNumberOfPlayer(2);
        actionMovesHandler.initializeAction();

        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.handleError(ActionMove.MOVE_STUDENTS);
        assertEquals("ms/ac", actionMovesHandler.getAvailableActions());

        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        assertEquals("mmn/ac", actionMovesHandler.getAvailableActions());
        actionMovesHandler.consumeAction(ActionMove.MOVE_MOTHER_NATURE);
        assertEquals("ac/sc", actionMovesHandler.getAvailableActions());
        actionMovesHandler.handleError(ActionMove.MOVE_MOTHER_NATURE);
        assertEquals("mmn/ac", actionMovesHandler.getAvailableActions());
    }

    @Test
    public void endGameActionTest() {
        ActionMovesHandler actionMovesHandler = new ActionMovesHandler();
        actionMovesHandler.setGameMode("experts");
        actionMovesHandler.setNumberOfPlayer(2);
        actionMovesHandler.initializeAction();

        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.consumeAction(ActionMove.MOVE_STUDENTS);
        actionMovesHandler.consumeAction(ActionMove.MOVE_MOTHER_NATURE);
        actionMovesHandler.consumeAction(ActionMove.SELECT_CLOUD);
        assertEquals("ac/et", actionMovesHandler.getAvailableActions());

        actionMovesHandler.handleError(ActionMove.SELECT_CLOUD);
        assertEquals("ac/sc", actionMovesHandler.getAvailableActions());
    }

    @Test
    public void MapTest() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "Simone");
        map.replace(0, "Paolo");
        System.out.println(map.get(0));

    }
}
