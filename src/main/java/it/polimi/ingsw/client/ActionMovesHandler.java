package it.polimi.ingsw.client;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionMovesHandler {
    private List<Integer> actions;
    private  String gameMode;
    private  Integer numberOfPlayer;

    private final int MOVE2P = 3;
    private final int MOVE3P = 4;

    public ActionMovesHandler() {
        this.actions = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setNumberOfPlayer(Integer numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

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

        //actions.set(ActionMove.END_TURN.ordinal(), 0);
    }

    private int actionIndex(ActionMove move) {
        return move.ordinal();
    }

    private void addAction(ActionMove move) {
        int actionIndex = move.ordinal();

        actions.set(actionIndex, actions.get(move.ordinal()) + 1);
    }

    public void handleError(ActionMove move) {
        addAction(move);
        calculateAvailableActions(move);
    }

    public void consumeAction(ActionMove move) {
        int actionIndex = move.ordinal();

        actions.set(actionIndex, actions.get(move.ordinal()) - 1);
        calculateAvailableActions(move);
    }


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
