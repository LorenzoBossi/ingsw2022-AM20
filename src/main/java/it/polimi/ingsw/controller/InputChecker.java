package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.ArrayList;
import java.util.List;

public class InputChecker {
    private Game model;

    public InputChecker(Game model) {
        this.model = model;
    }

    public boolean checkAssistantPlayed(AssistantName name) {
        Player currPlayer = model.getCurrPlayer();
        Assistant assistant = model.getAssistantByName(name);
        return currPlayer.getPlayerHand().canPlay(assistant);
    }

    public boolean checkValidTurn(String player) {
        String currPlayer = model.getCurrPlayer().getNickname();
        return currPlayer.equals(player);
    }

    public boolean checkStudentInEntrance(Color student) {
        Player currPlayer = model.getCurrPlayer();
        return currPlayer.getPlayerBoard().getEntrance().isPresent(student);
    }

    public boolean checkStudentInEntrance(List<Color> students) {
        Player currPlayer = model.getCurrPlayer();
        return currPlayer.getPlayerBoard().getEntrance().isPresent(students);
    }

    public boolean checkMoveStudentToIsland(Color student, int islandPosition) {
        return checkStudentInEntrance(student) && checkIslandPosition(islandPosition);
    }

    public boolean checkIslandPosition(int islandPosition) {
        int numberOfIslands = model.getArchipelago().getNumberOfIslands();
        return islandPosition >= 0 && islandPosition < numberOfIslands;
    }

    public boolean checkMotherNatureMove(int motherNatureMove) {
        return model.getCurrPlayer().isMotherNatureMoveLegit(motherNatureMove);
    }

    public boolean checkSelectedCloud(int cloudId) {
        List<Cloud> clouds = model.getClouds();
        if (cloudId < 0 || cloudId > clouds.size()) {
            return false;
        }
        return !clouds.get(cloudId).isChosen();
    }

    public boolean checkCharacterCardActivation(CharacterName name) {
        CharacterCard card = model.getCharacterCardByName(name);
        IslandsManager islandsManager = model.getArchipelago();

        if (card == null)
            return false;
        if (card.getCoinsRequired() > model.getCurrPlayer().getCoins())
            return false;
        if (!checkPlayerChoice(card))
            return false;
        if (card.getName() == CharacterName.HERBALIST && islandsManager.getNumberOfBanCards() == 4)
            return false;
        return card.checkRequirements(model.getCurrPlayer());
    }

    private boolean checkPlayerChoice(CharacterCard card) {
        PlayerChoice playerChoice = model.getCurrPlayer().getPlayerChoice();
        CharacterCardType type = card.getCharacterCardType();

        boolean result = true;

        switch (type) {
            case COLOR_SELECTION:
                if (playerChoice.getSelectedColor() == null)
                    result = false;
                break;
            case ISLAND_SELECTION:
                if (playerChoice.getSelectedIsland() == null)
                    result = false;
                break;
            case MUSICIAN:
            case JESTER:
                if (playerChoice.getSelectedStudentFromEntrance() == null || playerChoice.getSelectedStudents() == null)
                    result = false;
                break;
            case PRINCESS:
                if (playerChoice.getSelectedStudents() == null)
                    result = false;
                break;
            case MONK:
                if (playerChoice.getSelectedStudents() == null || playerChoice.getSelectedIsland() == null)
                    result = false;
                break;
        }
        return result;
    }
}
