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

public class Controller extends ObservableSubject {
    Game game;

    public Controller(Game game) {
        this.game = game;
    }


    public void playAssistant(AssistantName assistantName) {
        Player currPlayer = game.getCurrPlayer();
        Assistant assistant = game.getAssistantByName(assistantName);

        currPlayer.playAssistant(assistant);
        notifyObserver(new AssistantPlayed(currPlayer.getNickname(), assistantName));
    }


    public void moveStudentToDiningRoom(Color student, int islandPosition) {
        Player currPlayer = game.getCurrPlayer();
        PlayerBoard board = currPlayer.getPlayerBoard();
        String nickname = currPlayer.getNickname();
        ProfessorManager professorManager = game.getProfessorManager();
        List<Color> studentToAdd = new ArrayList<>();
        studentToAdd.add(student);

        board.moveStudentFromEntranceToDiningRoom(student);

        if (board.getDiningRoom().isAddCoin(student) && game.hasEnoughCoins(1)) {
            currPlayer.addCoin();
            notifyObserver(new PlayerCoinsEvent(1));
        }

        notifyObserver(new MoveStudents("ENTRANCE", "DINING_ROOM", studentToAdd, nickname, nickname));

        if (professorManager.canTakeProfessor(currPlayer, student)) {
            professorManager.takeProfessor(currPlayer, student);
        }
    }

    public void moveStudentToIsland(Color student, int islandPosition) {
        Player currPlayer = game.getCurrPlayer();
        IslandsManager islandsManager = game.getArchipelago();
        Island island = islandsManager.getIsland(islandPosition);
        PlayerBoard board = currPlayer.getPlayerBoard();
        List<Color> studentToAdd = new ArrayList<>();
        studentToAdd.add(student);

        board.getEntrance().removeStudent(student);
        island.addStudent(student);
        notifyObserver(new MoveStudents("ENTRANCE", "ISLAND", studentToAdd, currPlayer.getNickname(), islandPosition));
    }

    public void moveMotherNature(int motherNatureMove) {
        IslandsManager islandsManager = game.getArchipelago();
        Island currIsland = islandsManager.moveMotherNature(motherNatureMove);

        if (currIsland.isBanCardPresent()) {
            islandsManager.removeBanCardOnIsland(currIsland);
            return;
        }

        game.updateInfluence(currIsland);
    }

    public void selectCloud(int cloudID) {
        Player currPlayer = game.getCurrPlayer();
        Entrance entrance = currPlayer.getPlayerBoard().getEntrance();
        Cloud chosenCloud = game.getClouds().get(cloudID);
        List<Color> studentsToAdd;

        studentsToAdd = chosenCloud.getStudents();
        entrance.addStudentFromCloud(chosenCloud);
        notifyObserver(new MoveStudents("CLOUD", "ENTRANCE", studentsToAdd, cloudID, currPlayer.getNickname()));
    }

    public void useCharacterCard(CharacterName characterName) {
        CharacterCard card = game.getCharacterCardByName(characterName);
        int payment = card.getCoinsRequired();

        game.characterCardPayment(payment);
        notifyObserver(new PlayerCoinsEvent(-payment));
        card.activateEffect(game);
    }
}
