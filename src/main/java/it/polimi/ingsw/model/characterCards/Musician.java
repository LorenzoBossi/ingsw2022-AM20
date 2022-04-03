package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Musician extends CharacterCard {

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public Musician(int coinsRequired) {
        super(coinsRequired);
    }

    /**
     * Swap the students selected from the entrance and from the dining room of the current player
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){
        PlayerChoice playerChoice= game.getCurrPlayer().getPlayerChoice();
        PlayerBoard playerBoard= game.getCurrPlayer().getPlayerBoard();
        Color entrance;
        Color dining;
        int i;
        int numberOfStudents;

        List<Color> fromEntrance=  playerChoice.getSelectedStudentFromEntrance();
        List<Color> fromDiningRoom= playerChoice.getSelectedStudents();

        numberOfStudents= fromEntrance.size();

        for(i=0;i<numberOfStudents;i++){
            entrance= fromEntrance.get(i);
            dining=fromDiningRoom.get(i);

            playerBoard.getEntrance().removeStudent(entrance);
            playerBoard.getDiningRoom().removeStudent(dining);

            playerBoard.getEntrance().addStudent(dining);
            playerBoard.getDiningRoom().addStudent(entrance);
        }

    }


}
