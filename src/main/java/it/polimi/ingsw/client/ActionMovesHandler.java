package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class ActionMovesHandler calculates the available actions of the client
 */
public class ActionMovesHandler {
    private List<Integer> actions;
    private  String gameMode;
    private  Integer numberOfPlayer;

    private final int MOVE2P = 3;
    private final int MOVE3P = 4;

    /**
     * Constructor
     */
    public ActionMovesHandler() {
        this.actions = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
    }

    /**
     * Sets gameMode
     * @param gameMode the gameMode of the game
     */
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Sets the numberOfPlayer of the game
     * @param numberOfPlayer the number of player
     */
    public void setNumberOfPlayer(Integer numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    /**
     * Initializes the action of the client
     */
    public void initializeAction() {
        ActionMove moveStudents = ActionMove.MOVE_STUDENTS;
        ActionMove activateCard = ActionMove.ACTIVATE_CARD;

        if (gameMode.equals("experts")) {
            actions.set(activateCard.ordinal(), 1);
        }
        if (numberOfPlayer == 2)
            actions.set(moveStudents.ordinal(), MOVE2P);
        else if (numberOfPlayer == 3)
            actions.set(moveStudents.ordinal(), MOVE3P);
    }

    /**
     * Add the action to the available actions
     * @param move the action to add
     */
    private void addAction(ActionMove move) {
        int actionIndex = move.ordinal();

        actions.set(actionIndex, actions.get(move.ordinal()) + 1);
    }

    /**
     * Handle the error from the server
     * @param move the action that cause the error
     */
    public void handleError(ActionMove move) {
        addAction(move);
        calculateAvailableActions(move);
    }

    /**
     * Consumes the action
     * @param move the action to consume
     */
    public void consumeAction(ActionMove move) {
        int actionIndex = move.ordinal();

        actions.set(actionIndex, actions.get(move.ordinal()) - 1);
        calculateAvailableActions(move);
    }


    /**
     * Calculates the available actions
     * @param move the action to consume
     */
    private void calculateAvailableActions(ActionMove move) {
        int actionIndex = move.ordinal();
        switch (move) {
            case MOVE_STUDENTS:
                if (actions.get(actionIndex) == 0)
                    addAction(ActionMove.MOVE_MOTHER_NATURE);
                else {
                    actions.set(ActionMove.MOVE_MOTHER_NATURE.ordinal(), 0);
                }
                break;
            case MOVE_MOTHER_NATURE:
                if (actions.get(actionIndex) == 0) {
                    addAction(ActionMove.SELECT_CLOUD);
                } else {
                    actions.set(ActionMove.SELECT_CLOUD.ordinal(), 0);
                }
                break;
            case SELECT_CLOUD:
                if (actions.get(actionIndex) == 0) {
                    addAction(ActionMove.END_TURN);
                } else {
                    actions.set(ActionMove.END_TURN.ordinal(), 0);
                }
                break;
        }
    }

    /**
     * Get the client available actions
     * @return the available actions
     */
    public String getAvailableActions() {
        StringBuilder check = new StringBuilder();

        for(ActionMove action : ActionMove.values()) {
            int actionIndex = action.ordinal();
            if(actions.get(actionIndex) != 0) {
                check.append("/").append(action.getAbbreviation());
            }
        }
        check = new StringBuilder(check.substring(1, check.length()));
        return check.toString();
    }


}
