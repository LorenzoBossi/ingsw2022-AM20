package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Banker extends CharacterCard {

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public Banker(int coinsRequired) {
        super(coinsRequired);
    }

    /**
     *Removes from every dining room 3 or all the students of the color selected by the current player
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){

        Color selectedColor= game.getCurrPlayer().getPlayerChoice().getSelectedColor();
        Bag bag= game.getBag();
        List<Color> removedStudents= new ArrayList<>();
        int numberOfStudents;
        int i;
        DiningRoom diningRoom;
        List<Player> players = game.getPlayers();

        for(Player p: players){
            diningRoom=p.getPlayerBoard().getDiningRoom();
            numberOfStudents =diningRoom.getNumberOfStudent(selectedColor);

            if(numberOfStudents > 3)
                numberOfStudents=3;

            for(i=0;i<numberOfStudents;i++){
                diningRoom.removeStudent(selectedColor);
                removedStudents.add(selectedColor);
            }
            bag.addStudents(removedStudents);
            removedStudents.clear();
        }
    }
}
