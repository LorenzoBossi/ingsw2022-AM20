package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.CharacterCardType;
import it.polimi.ingsw.model.characterCards.CharacterName;

import java.util.ArrayList;
import java.util.List;

/**
 * Class InputChecker checks that messages received from clients are valid
 */
public class InputChecker {
    private Game model;

    /**
     * Constructor
     * @param model the model of the game
     */
    public InputChecker(Game model) {
        this.model = model;
    }

    /**
     * Checks if the assistant that the client want to play is playable
     * @param name the name of the assistant that the client want to play
     * @return true if it is playable, false otherwise
     */
    public boolean checkAssistantPlayed(AssistantName name) {
        Player currPlayer = model.getCurrPlayer();
        Assistant assistant = model.getAssistantByName(name);
        return currPlayer.getPlayerHand().canPlay(assistant);
    }

    /**
     * Checks if it is the player's turn
     * @param player the player nickname
     * @return true if it is his turn, false otherwise
     */
    public boolean checkValidTurn(String player) {
        String currPlayer = model.getCurrPlayer().getNickname();
        return currPlayer.equals(player);
    }

    /**
     * Check if the student is in the player's entrance
     * @param student the color of the student
     * @return true if the student is present, false otherwise
     */
    public boolean checkStudentInEntrance(Color student) {
        Player currPlayer = model.getCurrPlayer();
        return currPlayer.getPlayerBoard().getEntrance().isPresent(student);
    }

    /**
     * Check if the students are in the player's entrance
     * @param students the colors of the student
     * @return true if the students are present, false otherwise
     */
    public boolean checkStudentInEntrance(List<Color> students) {
        Player currPlayer = model.getCurrPlayer();
        return currPlayer.getPlayerBoard().getEntrance().isPresent(students);
    }

    /**
     * Checks if the islandPosition is valid and if the student are in the player's entrance
     * @param student the color of the student
     * @param islandPosition the island position
     * @return true if the player can move the student from entrance to the island, false otherwise
     */
    public boolean checkMoveStudentToIsland(Color student, int islandPosition) {
        return checkStudentInEntrance(student) && checkIslandPosition(islandPosition);
    }

    /**
     * Checks if the islandPosition is valid
     * @param islandPosition the island
     * @return true if the islandPosition is valid, false otherwise
     */
    public boolean checkIslandPosition(int islandPosition) {
        int numberOfIslands = model.getArchipelago().getNumberOfIslands();
        return islandPosition >= 0 && islandPosition < numberOfIslands;
    }

    /**
     * Checks if the player can make this mother nature move
     * @param motherNatureMove how much the player want to move mother nature
     * @return true if the mother nature move is valid, false otherwise
     */
    public boolean checkMotherNatureMove(int motherNatureMove) {
        return model.getCurrPlayer().isMotherNatureMoveLegit(motherNatureMove);
    }

    /**
     * Check if the cloudId is valid
     * @param cloudId the id of the cloud chosen by the player
     * @return true if the cloudId is valid, false otherwise
     */
    public boolean checkSelectedCloud(int cloudId) {
        List<Cloud> clouds = model.getClouds();
        if (cloudId < 0 || cloudId > clouds.size()) {
            return false;
        }
        return !clouds.get(cloudId).isChosen();
    }

    /**
     * Checks if the client can activate the chosen character card
     * @param name the name of the character card chosen by the player
     * @return true if the character card is playable, false otherwise
     */
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

    /**
     * Checks if you can add the student to the dining room
     * @param student the color of the student that the player want to add
     * @return true if the player can add the student, false otherwise
     */
    public boolean checkStudentInTheDiningRoom(Color student) {
        DiningRoom diningRoom = model.getCurrPlayer().getPlayerBoard().getDiningRoom();
        int numberOfStudent = diningRoom.getNumberOfStudent(student);

        return numberOfStudent < 10;
    }

    /**
     * Checks if the player makes all the choices in order to activate the card
     * @param card the card that the player want to activate
     * @return true if the player already made all the choice, false otherwise
     */
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
                if (playerChoice.getSelectedStudentFromEntrance() == null || playerChoice.getSelectedStudents() == null)
                    result = false;
                else {
                    result = checkStudentInTheDiningRoom(playerChoice.getSelectedStudentFromEntrance().get(0));
                    if(playerChoice.getSelectedStudents().size() > 1) {
                        result = checkStudentInTheDiningRoom(playerChoice.getSelectedStudentFromEntrance().get(0)) && checkStudentInTheDiningRoom(playerChoice.getSelectedStudentFromEntrance().get(1));
                    }
                }
            case JESTER:
                if (playerChoice.getSelectedStudentFromEntrance() == null || playerChoice.getSelectedStudents() == null)
                    result = false;
                break;
            case PRINCESS:
                if (playerChoice.getSelectedStudents() == null)
                    result = false;
                else
                    result = checkStudentInTheDiningRoom(playerChoice.getSelectedStudents().get(0));
                break;
            case MONK:
                if (playerChoice.getSelectedStudents() == null || playerChoice.getSelectedIsland() == null)
                    result = false;
                break;
        }
        return result;
    }
}
