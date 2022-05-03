package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
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
        return islandPosition >= 0 && islandPosition <= numberOfIslands;
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

    public boolean checkStudentsOnCard(CharacterName name, List<Color> students) {
        CharacterCard card = model.getCharacterCardByName(name);
        if (card == null) {
            return false;
        }
        List<Color> studentsOnCard = new ArrayList<>(card.getStudents());
        for (Color student : students) {
            if (!studentsOnCard.contains(student))
                return false;
            studentsOnCard.remove(student);
        }
        return true;
    }
}
