package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.CharacterCard;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.serverMessage.AssistantPlayed;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;
import it.polimi.ingsw.network.messages.serverMessage.PlayerCoinsEvent;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Controller calls the methods of the model to modify it
 */
public class Controller {
    private Game game;

    /**
     * Constructor
     *
     * @param game the current game
     */
    public Controller(Game game) {
        this.game = game;
    }

    /**
     * Method playAssistant plays the assistant chosen by the player
     *
     * @param assistantName the assistant chosen by the player
     */
    public void playAssistant(AssistantName assistantName) {
        Player currPlayer = game.getCurrPlayer();
        Assistant assistant = game.getAssistantByName(assistantName);

        currPlayer.playAssistant(assistant);
    }

    /**
     * Method moveStudentToDiningRoom takes the student in the entrance chosen by the player and moves it to the DiningRoom
     *
     * @param student the student in the entrance chosen by the player
     */
    public void moveStudentToDiningRoom(Color student) {
        Player currPlayer = game.getCurrPlayer();
        PlayerBoard board = currPlayer.getPlayerBoard();
        String nickname = currPlayer.getNickname();
        ProfessorManager professorManager = game.getProfessorManager();
        currPlayer.moveStudentToDiningRoom(student);

        if (board.getDiningRoom().isAddCoin(student) && game.hasEnoughCoins()) {
            game.addCoinsToPlayer(nickname);
        }

        if (professorManager.canTakeProfessor(currPlayer, student)) {
            professorManager.takeProfessor(currPlayer, student);
        }
    }

    /**
     * Method moveStudentToIsland takes the student selected by the player in the entrance and moves it to the selected island
     *
     * @param student        the student selected from the entrance
     * @param islandPosition the island where the player wants to move the student from the entrance
     */
    public void moveStudentToIsland(Color student, int islandPosition) {
        Player currPlayer = game.getCurrPlayer();
        IslandsManager islandsManager = game.getArchipelago();
        PlayerBoard board = currPlayer.getPlayerBoard();

        board.getEntrance().removeStudent(student);
        islandsManager.addStudentOnIsland(student, islandPosition, currPlayer.getNickname());
    }

    /**
     * Method moveMotherNature moves MotherNature and update the influence on the island where she stops
     *
     * @param motherNatureMove how much the player wants to move MotherNature
     */
    public void moveMotherNature(int motherNatureMove) {
        IslandsManager islandsManager = game.getArchipelago();
        Island currIsland = islandsManager.moveMotherNature(motherNatureMove);

        if (currIsland.isBanCardPresent()) {
            islandsManager.removeBanCardOnIsland(currIsland);
            return;
        }

        game.updateInfluence(currIsland);
        game.getCurrPlayer().setPlayerChoice(new PlayerChoice());
    }

    /**
     * Method selectCloud moves the students from the selected cloud on the entrance
     *
     * @param cloudID the cloudId selected by the player
     */
    public void selectCloud(int cloudID) {
        Player currPlayer = game.getCurrPlayer();
        Entrance entrance = currPlayer.getPlayerBoard().getEntrance();
        Cloud chosenCloud = game.getClouds().get(cloudID);

        currPlayer.addStudentFromCloud(chosenCloud, cloudID);
    }

    /**
     * Method useCharacterCard actives the effect of the selected characterCard and adds the coins to the game
     *
     * @param characterName the character card that the player want to activate
     */
    public void useCharacterCard(CharacterName characterName) {
        CharacterCard card = game.getCharacterCardByName(characterName);
        int payment = card.getCoinsRequired();

        game.characterCardPayment(payment);
        card.activateEffect(game);
    }

    /**
     * Method setColorSelection sets the playerChoice with color selected by the player
     *
     * @param color the color selected by the player
     */
    public void setColorSelection(Color color) {
        Player currPlayer = game.getCurrPlayer();

        currPlayer.getPlayerChoice().selectColor(color);
    }

    /**
     * Method setIslandSelection sets the playerChoice with island selected by the player
     *
     * @param islandSelection the island selected by the player
     */
    public void setIslandSelection(int islandSelection) {
        Player currPlayer = game.getCurrPlayer();
        Island selectedIsland;
        IslandsManager islandsManager = game.getArchipelago();

        selectedIsland = islandsManager.getIsland(islandSelection);
        currPlayer.getPlayerChoice().selectIsland(selectedIsland);
    }

    /**
     * Method setStudentsSelectedFromCard set the playerChoice with the students selected by the player on the character card
     *
     * @param students the students on the character card selected by the player
     */
    public void setStudentsSelectedFromCard(List<Color> students) {
        Player currPlayer = game.getCurrPlayer();

        currPlayer.getPlayerChoice().selectStudents(students);
    }

    /**
     * Method setStudentsSelectedFromCard set the playerChoice with the students selected by the player in the entrance
     *
     * @param students the students in the entrance selected by the player
     */
    public void setStudentsSelectedEntrance(List<Color> students) {
        Player currPlayer = game.getCurrPlayer();

        currPlayer.getPlayerChoice().selectStudentFromEntrance(students);
    }
}
