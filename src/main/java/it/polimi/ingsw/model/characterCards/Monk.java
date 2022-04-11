package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;


import java.util.List;

public class Monk  extends WithStudents {

    /**
     * Constructor
     * @param bag the bag of the game to put students on the card
     */
    public Monk(Bag bag){
        super(1);
        numberOfStudents=4;
        maxSelection=1;
        setStudents(bag.getStudents(getNumberOfStudents()));
    }

    /**
     *Puts the selected student on the selected Island
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){
        List<Color> student= game.getCurrPlayer().getPlayerChoice().getSelectedStudents();
        if(getStudents().contains(student.get(0))){
            remove(student);
        }
        add(game.getBag().getStudents(1));
        Island island= game.getCurrPlayer().getPlayerChoice().getSelectedIsland();
        island.addStudent(student.get(0));
    }
}
