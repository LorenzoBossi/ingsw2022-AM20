package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.List;

public class Musician extends CharacterCard {

    /**
     * Constructor
     */
    public Musician() {
        super(CharacterName.MUSICIAN, 1);
    }


    /**
     * Swap the students selected from the entrance and from the dining room of the current player
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        Player currPlayer = game.getCurrPlayer();
        PlayerChoice playerChoice = currPlayer.getPlayerChoice();
        PlayerBoard playerBoard = currPlayer.getPlayerBoard();
        Color entrance;
        Color dining;
        int i;
        int numberOfStudents;

        List<Color> fromEntrance = playerChoice.getSelectedStudentFromEntrance();
        List<Color> fromDiningRoom = playerChoice.getSelectedStudents();

        numberOfStudents = fromEntrance.size();

        for (i = 0; i < numberOfStudents; i++) {
            entrance = fromEntrance.get(i);
            dining = fromDiningRoom.get(i);

            playerBoard.getEntrance().removeStudent(entrance);
            playerBoard.getDiningRoom().removeStudent(dining);

            playerBoard.getEntrance().addStudent(dining);
            playerBoard.getDiningRoom().addStudent(entrance);
        }

        notifyObserver(new MoveStudents("ENTRANCE", "DINING_ROOM", fromEntrance, currPlayer.getNickname(), currPlayer.getNickname()));
        notifyObserver(new MoveStudents("DINING_ROOM", "ENTRANCE", fromDiningRoom, currPlayer.getNickname(), currPlayer.getNickname()));
    }

}
