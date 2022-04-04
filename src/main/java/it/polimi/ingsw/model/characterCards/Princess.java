package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;

import java.util.List;

public class Princess extends WithStudents {

    /**
     * Constructor
     * @param bag the bag of the game to put students on the card
     */
    public Princess(Bag bag){
        super(2);
        setNumberOfStudents(4);
        setMaxSelection(1);
        setStudents(bag.getStudents(getNumberOfStudents()));
    }

    /**
     * Puts the selected student of the current player into the current player dining room
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){
        List<Color> student= game.getCurrPlayer().getPlayerChoice().getSelectedStudents();
        if(getStudents().contains(student)){
            remove(student);
        }
        add(game.getBag().getStudents(1));
        game.getCurrPlayer().getPlayerBoard().getDiningRoom().addStudent(student.get(0));
    }
}